package jetpack.tutorial.firstattempt.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages.GetAllMessagesParam
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
) : ConversationDataSource {

    override fun getAllUsers(param: GetUsersParam): Flow<ResultModel<List<UserModel>>> {
        return callbackFlow {
            if (param.userIds.isEmpty()) {
                db.collection(Constant.USER_COLLECTION)
                    .addSnapshotListener { querySnapshot, error ->
                        if (querySnapshot != null) {
                            val users = mutableListOf<UserModel>()
                            for (document in querySnapshot.documents) {
                                val user = document.toObject<FireStoreUserDto>()
                                user?.let {
                                    users.add(user.toModel())
                                }
                            }
                            trySend(ResultModel.Success(users))
                        }
                        if (error != null) {
                            error.printStackTrace()
                            trySend(ResultModel.Error(error))
                            close(error)
                        }
                    }
            } else {
                db.collection(Constant.USER_COLLECTION)
                    .whereArrayContains(Constant.USERS_FIELD, param.userIds)
                    .addSnapshotListener { querySnapshot, error ->
                        if (querySnapshot != null) {
                            val users = mutableListOf<UserModel>()
                            for (document in querySnapshot.documents) {
                                val user = document.toObject<FireStoreUserDto>()
                                user?.let {
                                    users.add(user.toModel())
                                }
                            }
                            trySend(ResultModel.Success(users))
                        }
                        if (error != null) {
                            error.printStackTrace()
                            trySend(ResultModel.Error(error))
                            close(error)
                        }
                    }
            }
            awaitClose()
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

        val addDocumentFlow: Flow<String>
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
                        close(e)
                    }
            }
        }
    }


    override fun updateMessage(messageParam: MessageParam): Flow<ResultModel<MessageModel>> {
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
                        close(e)
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

    override fun getConversation(param: String): Flow<ResultModel<ConversationModel>> {
        val getMessageFlow = callbackFlow {
            val messages = mutableListOf<MessageDto>()
            val querySnapshot = db
                .collection(Constant.CONVERSATION_COLLECTION)
                .document(param)
                .collection(Constant.MESSAGE_COLLECTION)
                .get()
                .addOnSuccessListener {
                    for (document in it.documents) {
                        val messageDto = document.toObject<MessageDto>()
                        messageDto?.let {
                            messages.add(messageDto)
                        }
                        trySend(messages)
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    close(e)
                }
            awaitClose()
        }
        return callbackFlow {
            getMessageFlow.collect { messages ->
                db.collection(Constant.CONVERSATION_COLLECTION)
                    .whereEqualTo(Constant.ID_FIELD, param)
                    .get()
                    .addOnSuccessListener {
                        val doc = it.documents.first()
                        val conversationDto = doc.toObject<FireStoreConversationDto>()
                        val conversation = conversationDto?.toModel(
                            messages = messages.sortedBy { dto ->
                                dto.timeSent
                            }
                        )
                        val result = ResultModel.Success(conversation!!)
                        trySend(result)
                    }
                    .addOnFailureListener { t ->
                        t.printStackTrace()
                        val result = ResultModel.Error(t)
                        trySend(result)
                    }
            }
            awaitClose()
        }
    }

    override fun getAllMessages(param: GetAllMessagesParam): Flow<ResultModel<List<MessageModel>>> {
        return callbackFlow {
            val needToLoad = if(param.loadCount == 0) 1 else param.loadCount * 20 - 1
            val pagingMessages = db.collection(Constant.CONVERSATION_COLLECTION)
                .document(param.conversationId)
                .collection(Constant.MESSAGE_COLLECTION)
                .orderBy(Constant.TIME_SENT, Query.Direction.DESCENDING)
                .limit(needToLoad.toLong())

            pagingMessages
                .get()
                .addOnSuccessListener { snapshot ->
                    val lastVisible = snapshot.documents[snapshot.size() - 1]

                    val next = db.collection(Constant.CONVERSATION_COLLECTION)
                        .document(param.conversationId)
                        .collection(Constant.MESSAGE_COLLECTION)
                        .orderBy(Constant.TIME_SENT, Query.Direction.DESCENDING)
                        .startAfter(lastVisible)
                        .limit(20)

                    next.get().addOnSuccessListener { it1 ->
                        Log.d("TAG", "getAllMessages: ${it1.documents.size}")
                        val messages = mutableListOf<MessageDto>()
                        for (document in it1.documents) {
                            val messageDto = document.toObject<MessageDto>()
                            messageDto?.let {
                                messages.add(messageDto)
                            }
                        }
                        Log.d("TAG-123", "getAllMessages: ${messages.size}")
                        trySend(ResultModel.Success(messages.map { dto ->
                            dto.toModel()
                        }))
                    }
                }
//            val querySnapshot = db
//                .collection(Constant.CONVERSATION_COLLECTION)
//                .document(conversationId)
//                .collection(Constant.MESSAGE_COLLECTION)
//                .addSnapshotListener { snapshot, e ->
//                    if(snapshot != null) {
//                        Log.d("TAG-123", "getAllMessages: notify")
//                        val messages = mutableListOf<MessageDto>()
//                        Log.d("TAG-123", "getAllMessages: ${snapshot.documentChanges.size}")
//                        for (document in snapshot.documents) {
//                            val messageDto = document.toObject<MessageDto>()
//                            messageDto?.let {
//                                messages.add(messageDto)
//                            }
//                        }
//                        trySend(ResultModel.Success(messages.map { dto ->
//                            dto.toModel()
//                        }))
//                    }
//                    if(e != null) {
//                        e.printStackTrace()
//                        trySend(ResultModel.Error(e))
//                        close(e)
//                    }
//                }
            awaitClose()
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
                if (isUsersAlreadyPair != null) {
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