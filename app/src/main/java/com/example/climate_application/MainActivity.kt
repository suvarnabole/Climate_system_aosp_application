package com.example.climate_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.climate_application.ui.theme.Climate_ApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ClimateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Climate_ApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ClimateScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 720)
@Composable
fun MainActivityPreview() {
    Climate_ApplicationTheme {
        ClimateContent(
            uiState = ClimateUiState(
                acPower = true,
                driverTemperature = 22.0f,
                passengerTemperature = 22.0f,
                fanSpeed = 2,
                connected = true
            ),
            onAcPowerChanged = {},
            onDriverTempChanged = {},
            onPassengerTempChanged = {},
            onFanSpeedChanged = {},
            onFanDirectionChanged = {},
            onRecirculationChanged = {},
            onFrontDefrostChanged = {},
            onRearDefrostChanged = {}
        )
    }
}
