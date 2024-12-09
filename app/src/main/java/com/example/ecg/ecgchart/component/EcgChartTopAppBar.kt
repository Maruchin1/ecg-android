package com.example.ecg.ecgchart.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcgChartTopAppBar(
    isMeasuring: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = "ECG Chart")
        },
        actions = {
            if (isMeasuring) {
                TextButton(onClick = onStop) {
                    Text(text = "Stop")
                }
            } else {
                TextButton(onClick = onStart) {
                    Text(text = "Start")
                }
            }
            TextButton(onClick = onReset) {
                Text(text = "Reset")
            }
        },
        modifier = modifier
    )
}