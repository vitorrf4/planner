package com.example.aula20

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teste_api.models.EVENT_STATUS
import com.example.teste_api.models.SSEEventData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SSEViewModel : ViewModel() {
    private val repository = SSERepository()
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
}