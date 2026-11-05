package com.example.climate_application

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import kotlinx.coroutines.*

class CarClimateService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val callbacks = RemoteCallbackList<ICarClimateCallback>()

    private var currentState = ClimateStateParcel(
        acPower = true,
        driverTemperature = 22.0f,
        passengerTemperature = 22.0f,
        fanSpeed = 2,
        fanDirection = 0,
        recirculation = false,
        frontDefrost = false,
        rearDefrost = false,
        connected = true
    )

    private val binder = object : ICarClimateService.Stub() {
        override fun getClimateState(): ClimateStateParcel {
            return currentState
        }

        override fun setAcPower(enabled: Boolean) {
            updateState { it.copy(acPower = enabled) }
        }

        override fun setDriverTemperature(temperature: Float) {
            updateState { it.copy(driverTemperature = temperature) }
        }

        override fun setPassengerTemperature(temperature: Float) {
            updateState { it.copy(passengerTemperature = temperature) }
        }

        override fun setFanSpeed(speed: Int) {
            updateState { it.copy(fanSpeed = speed) }
        }

        override fun setFanDirection(direction: Int) {
            updateState { it.copy(fanDirection = direction) }
        }

        override fun setRecirculation(enabled: Boolean) {
            updateState { it.copy(recirculation = enabled) }
        }

        override fun setFrontDefrost(enabled: Boolean) {
            updateState { it.copy(frontDefrost = enabled) }
        }

        override fun setRearDefrost(enabled: Boolean) {
            updateState { it.copy(rearDefrost = enabled) }
        }

        override fun registerCallback(callback: ICarClimateCallback?) {
            if (callback != null) {
                callbacks.register(callback)
                try {
                    callback.onClimateStateChanged(currentState)
                } catch (e: Exception) {
                    Log.e(TAG, "Error sending initial state to callback", e)
                }
            }
        }

        override fun unregisterCallback(callback: ICarClimateCallback?) {
            if (callback != null) {
                callbacks.unregister(callback)
            }
        }
    }

    private fun updateState(updater: (ClimateStateParcel) -> ClimateStateParcel) {
        currentState = updater(currentState)
        broadcastStateChange()
    }

    private fun broadcastStateChange() {
        val n = callbacks.beginBroadcast()
        for (i in 0 until n) {
            try {
                callbacks.getBroadcastItem(i)?.onClimateStateChanged(currentState)
            } catch (e: Exception) {
                Log.e(TAG, "Error broadcasting state change", e)
            }
        }
        callbacks.finishBroadcast()
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.i(TAG, "CarClimateService bound")
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        callbacks.kill()
        Log.i(TAG, "CarClimateService destroyed")
    }

    companion object {
        private const val TAG = "CarClimateService"
    }
}
