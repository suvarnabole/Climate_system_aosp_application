package com.example.climate_application

import android.content.Intent
import android.os.IBinder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ServiceTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class CarClimateServiceTest {

    @get:Rule
    val serviceRule = ServiceTestRule()

    private lateinit var binder: IBinder
    private lateinit var climateService: ICarClimateService

    @Before
    fun setUp() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), CarClimateService::class.java)
        binder = serviceRule.bindService(intent)
        climateService = ICarClimateService.Stub.asInterface(binder)
    }

    @After
    fun tearDown() {
        // ServiceTestRule automatically unbinds
    }

    @Test
    fun testInitialState() {
        val state = climateService.climateState
        assertNotNull(state)
        assertTrue(state.connected)
        assertEquals(22.0f, state.driverTemperature, 0.01f)
        assertEquals(2, state.fanSpeed)
    }

    @Test
    fun testSetAcPower() {
        climateService.setAcPower(false)
        var state = climateService.climateState
        assertFalse(state.acPower)

        climateService.setAcPower(true)
        state = climateService.climateState
        assertTrue(state.acPower)
    }

    @Test
    fun testSetDriverTemperature() {
        climateService.setDriverTemperature(24.5f)
        val state = climateService.climateState
        assertEquals(24.5f, state.driverTemperature, 0.01f)
    }

    @Test
    fun testSetPassengerTemperature() {
        climateService.setPassengerTemperature(20.0f)
        val state = climateService.climateState
        assertEquals(20.0f, state.passengerTemperature, 0.01f)
    }

    @Test
    fun testSetFanSpeed() {
        climateService.setFanSpeed(4)
        val state = climateService.climateState
        assertEquals(4, state.fanSpeed)
    }

    @Test
    fun testSetFanDirection() {
        climateService.setFanDirection(2)
        val state = climateService.climateState
        assertEquals(2, state.fanDirection)
    }

    @Test
    fun testSetRecirculation() {
        climateService.setRecirculation(true)
        val state = climateService.climateState
        assertTrue(state.recirculation)
    }

    @Test
    fun testSetFrontDefrost() {
        climateService.setFrontDefrost(true)
        val state = climateService.climateState
        assertTrue(state.frontDefrost)
    }

    @Test
    fun testSetRearDefrost() {
        climateService.setRearDefrost(true)
        val state = climateService.climateState
        assertTrue(state.rearDefrost)
    }

    @Test
    fun testCallbackNotification() {
        val latch = CountDownLatch(1)
        var updatedState: ClimateStateParcel? = null

        val callback = object : ICarClimateCallback.Stub() {
            override fun onClimateStateChanged(state: ClimateStateParcel?) {
                updatedState = state
                latch.countDown()
            }
        }

        climateService.registerCallback(callback)
        climateService.setFanSpeed(5)

        val received = latch.await(2, TimeUnit.SECONDS)
        assertTrue("Callback should be triggered", received)
        assertEquals(5, updatedState?.fanSpeed)

        climateService.unregisterCallback(callback)
    }
}
