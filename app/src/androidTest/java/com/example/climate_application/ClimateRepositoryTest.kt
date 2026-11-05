package com.example.climate_application

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClimateRepositoryTest {

    private lateinit var repository: ClimateRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        repository = ClimateRepository(context)
        repository.bindService()
    }

    @After
    fun tearDown() {
        repository.unbindService()
    }

    @Test
    fun testRepositoryOperations() = runBlocking {
        // Wait briefly for service binding
        kotlinx.coroutines.delay(500)

        repository.setAcPower(false)
        var state = repository.uiState.first()
        assertFalse(state.acPower)

        repository.setDriverTemperature(26.0f)
        state = repository.uiState.first()
        assertEquals(26.0f, state.driverTemperature, 0.01f)

        repository.setFanSpeed(3)
        state = repository.uiState.first()
        assertEquals(3, state.fanSpeed)
    }
}
