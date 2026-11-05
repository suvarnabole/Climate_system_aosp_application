package com.example.climate_application

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.climate_application.ui.theme.Climate_ApplicationTheme

@Composable
fun ClimateScreen(
    viewModel: ClimateViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ClimateContent(
        uiState = uiState,
        onAcPowerChanged = { viewModel.setAcPower(it) },
        onDriverTempChanged = { viewModel.setDriverTemperature(it) },
        onPassengerTempChanged = { viewModel.setPassengerTemperature(it) },
        onFanSpeedChanged = { viewModel.setFanSpeed(it) },
        onFanDirectionChanged = { viewModel.setFanDirection(it) },
        onRecirculationChanged = { viewModel.setRecirculation(it) },
        onFrontDefrostChanged = { viewModel.setFrontDefrost(it) },
        onRearDefrostChanged = { viewModel.setRearDefrost(it) },
        modifier = modifier
    )
}

@Composable
fun ClimateContent(
    uiState: ClimateUiState,
    onAcPowerChanged: (Boolean) -> Unit,
    onDriverTempChanged: (Float) -> Unit,
    onPassengerTempChanged: (Float) -> Unit,
    onFanSpeedChanged: (Int) -> Unit,
    onFanDirectionChanged: (Int) -> Unit,
    onRecirculationChanged: (Boolean) -> Unit,
    onFrontDefrostChanged: (Boolean) -> Unit,
    onRearDefrostChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header / Status Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AOSP Vehicle Climate Control",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(
                                color = if (uiState.connected) Color.Green else Color.Red,
                                shape = RoundedCornerShape(6.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (uiState.connected) "Connected" else "Disconnected",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (!uiState.error.isNullOrEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Status: ${uiState.error}",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            // Master AC Power Toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.acPower) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "AC Power", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(text = if (uiState.acPower) "Climate System ON" else "Climate System OFF", style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(
                        checked = uiState.acPower,
                        onCheckedChange = onAcPowerChanged
                    )
                }
            }

            // Dual Zone Temperature Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Driver Temperature
                TemperatureControlCard(
                    title = "Driver",
                    temperature = uiState.driverTemperature,
                    onIncrease = { onDriverTempChanged((uiState.driverTemperature + 0.5f).coerceIn(16f, 32f)) },
                    onDecrease = { onDriverTempChanged((uiState.driverTemperature - 0.5f).coerceIn(16f, 32f)) },
                    modifier = Modifier.weight(1f),
                    enabled = uiState.acPower
                )

                // Passenger Temperature
                TemperatureControlCard(
                    title = "Passenger",
                    temperature = uiState.passengerTemperature,
                    onIncrease = { onPassengerTempChanged((uiState.passengerTemperature + 0.5f).coerceIn(16f, 32f)) },
                    onDecrease = { onPassengerTempChanged((uiState.passengerTemperature - 0.5f).coerceIn(16f, 32f)) },
                    modifier = Modifier.weight(1f),
                    enabled = uiState.acPower
                )
            }

            // Fan Speed & Direction Controls
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "Fan Speed: ${uiState.fanSpeed}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        for (speed in 0..5) {
                            Button(
                                onClick = { onFanSpeedChanged(speed) },
                                enabled = uiState.acPower,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.fanSpeed == speed) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.secondaryContainer
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = "$speed")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "Air Distribution", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val modes = listOf("Face", "Floor", "Mixed", "Defrost")
                        modes.forEachIndexed { index, mode ->
                            Button(
                                onClick = { onFanDirectionChanged(index) },
                                enabled = uiState.acPower,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (uiState.fanDirection == index) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.secondaryContainer
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = mode, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Auxiliary HVAC Controls (Recirc, Front/Rear Defrost)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onRecirculationChanged(!uiState.recirculation) },
                    enabled = uiState.acPower,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.recirculation) MaterialTheme.colorScheme.tertiaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Recirc: ${if (uiState.recirculation) "ON" else "OFF"}")
                }

                Button(
                    onClick = { onFrontDefrostChanged(!uiState.frontDefrost) },
                    enabled = uiState.acPower,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.frontDefrost) MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Front Defrost")
                }

                Button(
                    onClick = { onRearDefrostChanged(!uiState.rearDefrost) },
                    enabled = uiState.acPower,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.rearDefrost) MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Rear Defrost")
                }
            }
        }
    }
}

@Composable
fun TemperatureControlCard(
    title: String,
    temperature: Float,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "%.1f °C".format(temperature), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onDecrease,
                    enabled = enabled,
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onIncrease,
                    enabled = enabled,
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                ) {
                    Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 720)
@Composable
fun ClimateScreenPreview() {
    Climate_ApplicationTheme {
        ClimateContent(
            uiState = ClimateUiState(
                acPower = true,
                driverTemperature = 21.5f,
                passengerTemperature = 22.0f,
                fanSpeed = 3,
                fanDirection = 0,
                recirculation = false,
                frontDefrost = false,
                rearDefrost = false,
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
