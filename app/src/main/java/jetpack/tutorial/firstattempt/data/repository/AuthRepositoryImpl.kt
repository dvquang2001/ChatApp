package jetpack.tutorial.firstattempt.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.data.dto.main.FireStoreUserDto
import jetpack.tutorial.firstattempt.data.dto.auth.LoginDto
import jetpack.tutorial.firstattempt.data.dto.auth.RegisterDto
import jetpack.tutorial.firstattempt.data.mapper.toModel
import jetpack.tutorial.firstattempt.data.util.Constant
import jetpack.tutorial.firstattempt.domain.data_source.AuthDataSource
import jetpack.tutorial.firstattempt.domain.model.auth.LoginModel
import jetpack.tutorial.firstattempt.domain.model.auth.RegisterModel
import jetpack.tutorial.firstattempt.domain.usecase.auth.login.LoginParam
import jetpack.tutorial.firstattempt.domain.usecase.auth.regsiter.RegisterParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
): AuthDataSource{

    override fun loginWithEmailPassword(param: LoginParam): Flow<ResultModel<LoginModel>> {
        return flow {
           val result = try {
               val signInResult = auth.signInWithEmailAndPassword(
                   param.email,
                   param.password
               ).await()
               val userId = signInResult.user!!.uid
               val loginDto = LoginDto(userId = userId, email = param.email)
               val loginModel = loginDto.toModel()
               ResultModel.Success(loginModel)
           } catch (t: Throwable) {
               t.printStackTrace()
               ResultModel.Error(t)
           }
            emit(result)
        }
    }

    override fun registerWithEmailPassword(param: RegisterParam): Flow<ResultModel<RegisterModel>> {
        return flow {
            val result = try {
                val createAccountResult = auth.createUserWithEmailAndPassword(
                    param.email,
                    param.password
                ).await()
                val userId = createAccountResult.user!!.uid
                val firebaseUser = FireStoreUserDto(
                    id = userId,
                    email = param.email,
                    name = param.fullName,
                    createdAt = Timestamp.now()
                )
                db.runBatch {
                    it.set(db.collection(Constant.USER_COLLECTION).document(userId), firebaseUser)
                }
                val registerDto = RegisterDto(
                    userId = userId,
                    email = param.email,
                    fullName = param.fullName
                )
                ResultModel.Success(
                    registerDto.toModel()
                )
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

    override fun logout(): Flow<ResultModel<Unit>> {
        return flow {
            val result = try {
                val logout = auth.signOut()
                ResultModel.Success(logout)
            } catch (t: Throwable) {
                t.printStackTrace()
                ResultModel.Error(t)
            }
            emit(result)
        }
    }

}