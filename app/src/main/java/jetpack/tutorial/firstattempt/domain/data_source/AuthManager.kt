package jetpack.tutorial.firstattempt.domain.data_source

import jetpack.tutorial.firstattempt.domain.model.auth.AuthStateModel
import kotlinx.coroutines.flow.Flow

interface AuthManager {

    fun getAuthState(): Flow<AuthStateModel>
}