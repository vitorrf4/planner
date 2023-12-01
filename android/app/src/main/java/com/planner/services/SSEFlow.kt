package com.planner.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planner.models.SSEEvent
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SSEFlow : ViewModel() {
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
}