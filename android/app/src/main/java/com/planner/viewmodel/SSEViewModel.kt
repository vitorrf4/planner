package com.planner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planner.models.SSEEvent
import com.planner.services.SSEConnection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SSEViewModel : ViewModel() {
    private var sseConnection = SSEConnection()
    var sseEvents = MutableLiveData<SSEEvent>()

    fun getSSEEvents() = viewModelScope.launch {
        sseConnection.sseEventsFlow
            .onEach { sseEvent ->
                sseEvents.postValue(sseEvent)
            }
            .catch {
                sseEvents.postValue(SSEEvent("error"))
            }
            .launchIn(viewModelScope)
    }

    fun retryConnection() {
        this.sseConnection = SSEConnection()
    }
}