package com.example.climate_application

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClimateRepository(private val context: Context) {

    private val _uiState = MutableStateFlow(ClimateUiState())
    val uiState: StateFlow<ClimateUiState> = _uiState.asStateFlow()

    private var carClimateService: ICarClimateService? = null
    private var isBound = false

    private val callback = object : ICarClimateCallback.Stub() {
        override fun onClimateStateChanged(state: ClimateStateParcel?) {
            state?.let {
                _uiState.value = it.toUiState()
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            carClimateService = ICarClimateService.Stub.asInterface(service)
            isBound = true
            try {
                carClimateService?.registerCallback(callback)
                val initialState = carClimateService?.climateState
                if (initialState != null) {
                    _uiState.value = initialState.toUiState().copy(connected = true)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error initializing from service", e)
                _uiState.value = _uiState.value.copy(connected = false, error = e.localizedMessage)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            try {
                carClimateService?.unregisterCallback(callback)
            } catch (_: Exception) {}
            carClimateService = null
            isBound = false
            _uiState.value = _uiState.value.copy(connected = false)
        }
    }

    fun bindService() {
        if (!isBound) {
            val intent = Intent(context, CarClimateService::class.java)
            try {
                context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to bind CarClimateService", e)
                _uiState.value = _uiState.value.copy(connected = true, error = "Running in simulated mode")
            }
        }
    }

    fun unbindService() {
        if (isBound) {
            try {
                carClimateService?.unregisterCallback(callback)
                context.unbindService(serviceConnection)
            } catch (e: Exception) {
                Log.e(TAG, "Error unbinding service", e)
            } finally {
                isBound = false
                carClimateService = null
            }
        }
    }

    fun setAcPower(enabled: Boolean) {
        try {
            if (carClimateService != null) {
                carClimateService?.setAcPower(enabled)
            } else {
                simulateLocal { copy(acPower = enabled) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting AC power", e)
        }
    }

    fun setDriverTemperature(temp: Float) {
        try {
            if (carClimateService != null) {
                carClimateService?.setDriverTemperature(temp)
            } else {
                simulateLocal { copy(driverTemperature = temp) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting driver temp", e)
        }
    }

    fun setPassengerTemperature(temp: Float) {
        try {
            if (carClimateService != null) {
                carClimateService?.setPassengerTemperature(temp)
            } else {
                simulateLocal { copy(passengerTemperature = temp) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting passenger temp", e)
        }
    }

    fun setFanSpeed(speed: Int) {
        try {
            if (carClimateService != null) {
                carClimateService?.setFanSpeed(speed)
            } else {
                simulateLocal { copy(fanSpeed = speed) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting fan speed", e)
        }
    }

    fun setFanDirection(direction: Int) {
        try {
            if (carClimateService != null) {
                carClimateService?.setFanDirection(direction)
            } else {
                simulateLocal { copy(fanDirection = direction) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting fan direction", e)
        }
    }

    fun setRecirculation(enabled: Boolean) {
        try {
            if (carClimateService != null) {
                carClimateService?.setRecirculation(enabled)
            } else {
                simulateLocal { copy(recirculation = enabled) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting recirculation", e)
        }
    }

    fun setFrontDefrost(enabled: Boolean) {
        try {
            if (carClimateService != null) {
                carClimateService?.setFrontDefrost(enabled)
            } else {
                simulateLocal { copy(frontDefrost = enabled) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting front defrost", e)
        }
    }

    fun setRearDefrost(enabled: Boolean) {
        try {
            if (carClimateService != null) {
                carClimateService?.setRearDefrost(enabled)
            } else {
                simulateLocal { copy(rearDefrost = enabled) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting rear defrost", e)
        }
    }

    private inline fun simulateLocal(block: ClimateUiState.() -> ClimateUiState) {
        _uiState.value = _uiState.value.block()
    }

    companion object {
        private const val TAG = "ClimateRepository"
    }
}
