package com.planner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planner.models.EVENT_STATUS
import com.planner.models.SSEEventData
import com.planner.services.SSEConnection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SSEViewModel : ViewModel() {
    private var repository = SSEConnection()
    var sseEvents = MutableLiveData<SSEEventData>()

    fun getSSEEvents() = viewModelScope.launch {
        repository.sseEventsFlow
            .onEach { sseEventData ->
                sseEvents.postValue(sseEventData)
            }
            .catch {
                sseEvents.postValue(SSEEventData(eventStatus = EVENT_STATUS.ERROR))
            }
            .launchIn(viewModelScope)
    }

    fun retryConnection() {
        this.repository = SSEConnection()
    }
}