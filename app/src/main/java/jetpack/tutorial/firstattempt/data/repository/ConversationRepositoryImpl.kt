package jetpack.tutorial.firstattempt.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.data.dto.main.FireStoreConversationDto
import jetpack.tutorial.firstattempt.data.dto.main.FireStoreUserDto
import jetpack.tutorial.firstattempt.data.dto.main.MessageDto
import jetpack.tutorial.firstattempt.data.mapper.toDto
import jetpack.tutorial.firstattempt.data.mapper.toModel
import jetpack.tutorial.firstattempt.data.util.Constant
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import jetpack.tutorial.firstattempt.domain.model.main.UserModel
import jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair.UserParam
import jetpack.tutorial.firstattempt.domain.usecase.main.get_users.GetUsersParam
import jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation.ConversationParam
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.MessageParam
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ConversationRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ConversationDataSource{

    override fun getAllUsers(param: GetUsersParam): Flow<ResultModel<List<UserModel>>> {
        return flow {
            val result = try {
                val querySnapshot: QuerySnapshot
                if(param.userIds.isEmpty()) {
                    querySnapshot = db.collection(Constant.USER_COLLECTION).get().await()
                } else {
                    querySnapshot = db.collection(Constant.CONVERSATION_COLLECTION)
                        .whereArrayContains(Constant.USERS_FIELD, param.userIds)
                        .get()
                        .await()
                }
                val users = mutableListOf<UserModel>()
                for (document in querySnapshot.documents) {
                    val user = document.toObject<FireStoreUserDto>()
                    user?.let {
                        users.add(user.toModel())
                    }
                }
                ResultModel.Success(users)
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

    override fun getUser(param: String): Flow<ResultModel<UserModel>> {
        return flow {
            val result = try {
                val querySnapshot = db
                    .collection(Constant.USER_COLLECTION)
                    .whereEqualTo(Constant.ID_FIELD, param)
                    .get()
                    .await()
                val doc = querySnapshot.documents.first()
                val userDto = doc.toObject<FireStoreUserDto>()
                ResultModel.Success(userDto!!.toModel())
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

    override fun getCurrentUser(): Flow<ResultModel<UserModel>> {
        return flow {
            val result = try {
                val currentUser = auth.currentUser
                val querySnapshot = db
                    .collection(Constant.USER_COLLECTION)
                    .whereEqualTo(Constant.ID_FIELD, currentUser!!.uid)
                    .get()
                    .await()
                val doc = querySnapshot.documents.first()
                val userDto = doc.toObject<FireStoreUserDto>()
                ResultModel.Success(userDto!!.toModel())
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

    override fun updateConversation(param: ConversationParam): Flow<ResultModel<ConversationModel>> {
        val conversationDto = FireStoreConversationDto(
                    id = param.id,
                    lastMessage = param.lastMessage,
                    users = param.users
                )

        val addDocumentFlow : Flow<String>
        if (param.id == null) {
            addDocumentFlow = callbackFlow {
                val documentReference = db.collection(Constant.CONVERSATION_COLLECTION)
                    .add(conversationDto)
                    .addOnSuccessListener { reference ->
                        val documentId = reference.id
                        trySend(documentId)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        close(e)
                    }
                awaitClose()
            }
        } else {
            addDocumentFlow = flow {
                emit(param.id)
            }
        }
        return callbackFlow {
            addDocumentFlow.collect { documentId ->
                val dto = conversationDto.copy(id = documentId)
                db.collection(Constant.CONVERSATION_COLLECTION)
                    .document(documentId)
                    .set(dto)
                    .addOnSuccessListener {
                        val conversationModel = ResultModel.Success(
                            dto.toModel(
                                messages = param.messages.map {
                                    it.toDto()
                                }
                            )
                        )
                        trySend(conversationModel)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        trySend(ResultModel.Error(e))
                    }
            }
        }
    }


    override fun updateMessage(messageParam: MessageParam): Flow<ResultModel<MessageModel>>{
        val messageDto = messageParam.toDto()
        val addDocumentFlow = callbackFlow {
            val documentReference = db
                .collection(Constant.CONVERSATION_COLLECTION)
                .document(messageParam.conversationId!!)
                .collection(Constant.MESSAGE_COLLECTION)
                .add(messageDto)
                .addOnSuccessListener { reference ->
                    val documentId = reference.id
                    trySend(documentId)
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    close(e)
                }
            awaitClose()
        }
        return callbackFlow {
            addDocumentFlow.collect { documentId ->
                val dto = messageDto.copy(id = documentId)
                db.collection(Constant.CONVERSATION_COLLECTION)
                    .document(messageParam.conversationId!!)
                    .collection(Constant.MESSAGE_COLLECTION)
                    .document(documentId)
                    .set(dto)
                    .addOnSuccessListener {
                        val messageModel = ResultModel.Success(
                            dto.toModel()
                        )
                        trySend(messageModel)
                    }
                    .addOnFailureListener { e ->
                        e.printStackTrace()
                        trySend(ResultModel.Error(e))
                    }
            }
        }
    }

    override fun getAllConversations(param: String): Flow<ResultModel<List<ConversationModel>>> {
        return flow {
            val result = try {
                val conversations = getConversations(userId = param)
                ResultModel.Success(conversations)
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

    private suspend fun getConversationFromFirebase(conversationId: String) : ConversationModel?{
        val querySnapshot = db
            .collection(Constant.CONVERSATION_COLLECTION)
            .whereEqualTo(Constant.ID_FIELD, conversationId)
            .get()
            .await()
        val doc = querySnapshot.documents.first()
        val conversationDto = doc.toObject<FireStoreConversationDto>()
        return conversationDto?.toModel(
            messages = getAllMessageFromFirebase(conversationId).sortedBy {
                it.timeSent
            }
        )
    }

    private suspend fun getAllMessageFromFirebase(conversationId: String): List<MessageDto> {
        val querySnapshot = db
            .collection(Constant.CONVERSATION_COLLECTION)
            .document(conversationId)
            .collection(Constant.MESSAGE_COLLECTION)
            .get()
            .await()
        val messages = mutableListOf<MessageDto>()
        for(document in querySnapshot.documents) {
            val messageDto = document.toObject<MessageDto>()
            messageDto?.let {
                messages.add(messageDto)
            }
        }
        return messages
    }

    override fun getConversation(param: String): Flow<ResultModel<ConversationModel>> {
        return flow {
            val result = try {
                val querySnapshot = db
                    .collection(Constant.CONVERSATION_COLLECTION)
                    .whereEqualTo(Constant.ID_FIELD, param)
                    .get()
                    .await()
                val doc = querySnapshot.documents.first()
                val conversationDto = doc.toObject<FireStoreConversationDto>()
                val conversation =  conversationDto?.toModel(
                    messages = getAllMessageFromFirebase(param).sortedBy {
                        it.timeSent
                    }
                )
                ResultModel.Success(conversation!!)
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

    override fun getAllMessages(conversationId: String): Flow<ResultModel<List<MessageModel>>> {
       return flow {
           val result = try {
              val messages = getAllMessageFromFirebase(conversationId).map {
                  it.toModel()
              }
               ResultModel.Success(messages)
           } catch (t: Throwable) {
               t.printStackTrace()
               ResultModel.Error(t)
           }
           emit(result)
       }
    }

    override fun getMessage(messageParam: MessageParam): Flow<ResultModel<MessageModel>> {
        return flow {
            val result = try {
                val querySnapshot = db
                    .collection(Constant.CONVERSATION_COLLECTION)
                    .document(messageParam.conversationId!!)
                    .collection(Constant.MESSAGE_COLLECTION)
                    .whereEqualTo(Constant.ID_FIELD, messageParam.id!!)
                    .get()
                    .await()
                val doc = querySnapshot.documents.first()
                val messageDto = doc.toObject<MessageDto>()
                ResultModel.Success(messageDto!!.toModel())
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

    override fun checkTwoUserPair(
        userParam1: UserParam,
        userParam2: UserParam
    ): Flow<ResultModel<ConversationModel>> {
        return flow {
            val result = try {
                val conversations = getConversations(userId = userParam1.id)
                val isUsersAlreadyPair = conversations.firstOrNull {
                    it.users.size == 2
                            && it.users.contains(userParam1.id)
                            && it.users.contains(userParam2.id)
                }
                if(isUsersAlreadyPair != null) {
                    ResultModel.Success(isUsersAlreadyPair)
                } else {
                    ResultModel.Error(t = Throwable("Users not pair"))
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

    private suspend fun getConversations(userId: String): List<ConversationModel> {
        val querySnapshot = db.collection(Constant.CONVERSATION_COLLECTION)
            .whereArrayContains(Constant.USERS_FIELD, userId)
            .get()
            .await()
        val conversations = mutableListOf<ConversationModel>()
        for (document in querySnapshot.documents) {
            val conversation = document.toObject<FireStoreConversationDto>()
            conversation?.let {
                conversations.add(conversation.toModel(listOf()))  //todo: need to fix later
            }
        }
        return conversations
    }
}