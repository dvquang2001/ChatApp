package jetpack.tutorial.firstattempt.base

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

abstract class BaseViewModel<ViewState: BaseViewState, ViewEvent: BaseViewEvent, ViewEffect: BaseViewEffect>(
    initialViewState: ViewState
) : ViewModel(){

    private val _viewModelJob = SupervisorJob()
    private val _coroutineScope = CoroutineScope(Dispatchers.IO)

    protected open val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
        Log.e("BaseViewModel", "$context at $this get error: $throwable")
    }

    protected val coroutineScope: CoroutineScope by lazy {
        _coroutineScope + exceptionHandler + _viewModelJob
    }

    /**
     * Used for storing and sending state of views. It should be repopulate right after view is recreated.
     * Example: Storing data on screen like text, checkbox..
     */
    protected open var currentState: ViewState = initialViewState
    private val _state = MutableStateFlow(initialViewState)
    val state: Flow<ViewState> = _state

    /**
     * Update state and notify to consumer.
     */
    protected fun setState(state: ViewState) {
        this.currentState = state
        this._state.value = state
    }

    /**
     * Used for sending an event from ViewModel to View layer (Fragment, Activity) once.
     * If the effect is sent when view is not ready to handle, it will be ignored.
     */
    private val _effect = Channel<ViewEffect>()
    val effect: Flow<ViewEffect> = _effect.receiveAsFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: Flow<Boolean> = _loadingState

    /**
     * Send effect to view.
     */
    protected fun setEffect(effect: ViewEffect) {
        coroutineScope.ensureActive()
        coroutineScope.launch { _effect.send(effect) }
    }

    /**
     * Handle event from view.
     */
    abstract fun onEvent(event: ViewEvent)

    protected open fun onLoading(loading: Boolean = true) {
        _loadingState.update { loading }
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }
}