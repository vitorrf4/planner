package com.example.teste_api.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teste_api.models.SSEEventData
import com.example.teste_api.models.STATUS
import com.example.teste_api.repositories.SSERepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SSEViewModel : ViewModel(){
    val repository = SSERepository()

    var sseEvents = MutableLiveData<SSEEventData>()

    fun getSSEEvents() = viewModelScope.launch {
        repository.sseEventsFlow
            .onEach { sseEventData ->
                sseEvents.postValue(sseEventData)
            }
            .catch {
                sseEvents.postValue(SSEEventData(status = STATUS.ERROR))
            }
            .launchIn(viewModelScope)
    }
}