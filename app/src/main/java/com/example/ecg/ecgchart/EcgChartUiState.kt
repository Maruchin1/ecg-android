package com.example.ecg.ecgchart

import com.example.ecg.data.EcgConfig

data class EcgChartUiState(
    val measurements: List<Double> = EcgConfig.initialMeasurements,
    val isMeasuring: Boolean = false
)
