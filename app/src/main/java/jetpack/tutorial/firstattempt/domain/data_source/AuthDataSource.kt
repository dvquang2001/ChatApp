package jetpack.tutorial.firstattempt.domain.data_source

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.model.auth.LoginModel
import jetpack.tutorial.firstattempt.domain.model.auth.RegisterModel
import jetpack.tutorial.firstattempt.domain.usecase.auth.login.LoginParam
import jetpack.tutorial.firstattempt.domain.usecase.auth.regsiter.RegisterParam
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {

    fun loginWithEmailPassword(param: LoginParam): Flow<ResultModel<LoginModel>>

    fun registerWithEmailPassword(param: RegisterParam): Flow<ResultModel<RegisterModel>>

    fun logout(): Flow<ResultModel<Unit>>
}