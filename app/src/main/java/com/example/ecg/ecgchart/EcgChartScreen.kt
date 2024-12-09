package com.example.ecg.ecgchart

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecg.ecgchart.component.ResetButton
import com.example.ecg.ecgchart.component.EcgChart
import com.example.ecg.ecgchart.component.StartButton
import com.example.ecg.ecgchart.component.StopButton

@Composable
fun EcgChartScreen(viewModel: EcgChartViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    EcgChartScreen(
        state = state,
        onReset = viewModel::resetMeasurements,
        onStart = viewModel::startMeasuring,
        onStop = viewModel::stopMeasuring
    )
}

@Composable
private fun EcgChartScreen(
    state: EcgChartUiState,
    onReset: () -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedContent(state.isMeasuring) { isMeasuring ->
                if (isMeasuring) {
                    StopButton(onClick = onStop)
                } else {
                    StartButton(onClick = onStart)
                }
            }
            ResetButton(onClick = onReset)
        }
        EcgChart(measurements = state.measurements)
    }
}
