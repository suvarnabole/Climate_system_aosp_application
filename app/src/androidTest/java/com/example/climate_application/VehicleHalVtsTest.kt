package com.example.climate_application

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class VehicleHalVtsTest {

    @Test
    fun testParcelableSerialization() {
        val original = ClimateStateParcel(
            acPower = true,
            driverTemperature = 21.5f,
            passengerTemperature = 23.0f,
            fanSpeed = 3,
            fanDirection = 1,
            recirculation = true,
            frontDefrost = false,
            rearDefrost = true,
            connected = true
        )

        val parcel = android.os.Parcel.obtain()
        original.writeToParcel(parcel, 0)
        parcel.setDataPosition(0)

        val createdFromParcel = ClimateStateParcel.CREATOR.createFromParcel(parcel)
        parcel.recycle()

        assertEquals(original.acPower, createdFromParcel.acPower)
        assertEquals(original.driverTemperature, createdFromParcel.driverTemperature, 0.01f)
        assertEquals(original.passengerTemperature, createdFromParcel.passengerTemperature, 0.01f)
        assertEquals(original.fanSpeed, createdFromParcel.fanSpeed)
        assertEquals(original.fanDirection, createdFromParcel.fanDirection)
        assertEquals(original.recirculation, createdFromParcel.recirculation)
        assertEquals(original.frontDefrost, createdFromParcel.frontDefrost)
        assertEquals(original.rearDefrost, createdFromParcel.rearDefrost)
        assertEquals(original.connected, createdFromParcel.connected)
    }

    @Test
    fun testUiStateMapping() {
        val parcel = ClimateStateParcel(
            acPower = false,
            driverTemperature = 19.0f,
            passengerTemperature = 21.0f,
            fanSpeed = 1,
            fanDirection = 2,
            recirculation = false,
            frontDefrost = true,
            rearDefrost = false,
            connected = true
        )

        val uiState = parcel.toUiState()
        assertEquals(parcel.acPower, uiState.acPower)
        assertEquals(parcel.driverTemperature, uiState.driverTemperature, 0.01f)
        assertEquals(parcel.passengerTemperature, uiState.passengerTemperature, 0.01f)
        assertEquals(parcel.fanSpeed, uiState.fanSpeed)
        assertEquals(parcel.fanDirection, uiState.fanDirection)
        assertEquals(parcel.recirculation, uiState.recirculation)
        assertEquals(parcel.frontDefrost, uiState.frontDefrost)
        assertEquals(parcel.rearDefrost, uiState.rearDefrost)
        assertEquals(parcel.connected, uiState.connected)
    }
}
