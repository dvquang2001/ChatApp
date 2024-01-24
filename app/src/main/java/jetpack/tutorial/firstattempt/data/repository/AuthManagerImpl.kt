package jetpack.tutorial.firstattempt.data.repository

import com.google.firebase.auth.FirebaseAuth
import jetpack.tutorial.firstattempt.data.dto.auth.AuthState
import jetpack.tutorial.firstattempt.data.mapper.toModel
import jetpack.tutorial.firstattempt.domain.data_source.AuthManager
import jetpack.tutorial.firstattempt.domain.model.auth.AuthStateModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthManager {

    private val authStateSharedFlow: MutableSharedFlow<AuthState> = MutableSharedFlow(replay = 1)

    init {
        subscribeFirebaseAuthState()
    }

    private fun subscribeFirebaseAuthState() {
        auth.addAuthStateListener {
            if (it.currentUser != null) {
                authStateSharedFlow.tryEmit(AuthState.LOGGED_IN)
            } else {
                authStateSharedFlow.tryEmit(AuthState.LOGGED_OUT)
            }
        }
    }

    override fun getAuthState(): Flow<AuthStateModel> {
       return flow {
           emitAll(authStateSharedFlow.map { it.toModel() })
       }
    }

}