package com.example.climate_application

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClimateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ClimateRepository(application)

    val uiState: StateFlow<ClimateUiState> = repository.uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ClimateUiState()
        )

    init {
        repository.bindService()
    }

    fun setAcPower(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAcPower(enabled)
        }
    }

    fun setDriverTemperature(temp: Float) {
        viewModelScope.launch {
            repository.setDriverTemperature(temp)
        }
    }

    fun setPassengerTemperature(temp: Float) {
        viewModelScope.launch {
            repository.setPassengerTemperature(temp)
        }
    }

    fun setFanSpeed(speed: Int) {
        viewModelScope.launch {
            repository.setFanSpeed(speed)
        }
    }

    fun setFanDirection(direction: Int) {
        viewModelScope.launch {
            repository.setFanDirection(direction)
        }
    }

    fun setRecirculation(enabled: Boolean) {
        viewModelScope.launch {
            repository.setRecirculation(enabled)
        }
    }

    fun setFrontDefrost(enabled: Boolean) {
        viewModelScope.launch {
            repository.setFrontDefrost(enabled)
        }
    }

    fun setRearDefrost(enabled: Boolean) {
        viewModelScope.launch {
            repository.setRearDefrost(enabled)
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.unbindService()
    }
}
