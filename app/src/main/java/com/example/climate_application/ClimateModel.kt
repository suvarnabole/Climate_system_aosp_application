package com.example.climate_application


//package com.example.climate

data class ClimateUiState(
    val acPower: Boolean = false,
    val driverTemperature: Float = 22f,
    val passengerTemperature: Float = 22f,
    val fanSpeed: Int = 2,
    val fanDirection: Int = 0,
    val recirculation: Boolean = false,
    val frontDefrost: Boolean = false,
    val rearDefrost: Boolean = false,
    val connected: Boolean = false,
    val error: String? = null
)
