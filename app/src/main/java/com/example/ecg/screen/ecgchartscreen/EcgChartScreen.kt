package com.example.ecg.screen.ecgchartscreen

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ecg.data.EcgMeasurement
import com.example.ecg.ui.theme.EcgandroidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcgChartScreen(
    measurements: List<EcgMeasurement>,
    onStartMeasuring: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "ECG Chart")
                }
            )
        }
    ) { innerPadding ->

    }
}

@Preview
@Composable
private fun EcgChartScreenPreview() {
    EcgandroidTheme {
        EcgChartScreen(
            measurements = emptyList(),
            onStartMeasuring = {}
        )
    }
}