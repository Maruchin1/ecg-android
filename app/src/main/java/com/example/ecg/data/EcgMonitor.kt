package com.example.ecg.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class EcgMonitor {

    private val _measurements = MutableStateFlow<List<EcgMeasurement>>(emptyList())
    val measurements = _measurements.asStateFlow()

    suspend fun startMeasuring() {
        delay(5.milliseconds)
        val nextMeasurement = getNextMeasurement()
        _measurements.update { it + nextMeasurement }
    }

    private fun getNextMeasurement() = EcgMeasurement(
        value = Random.nextDouble(-1.0, 1.0),
        timestamp = LocalDateTime.now()
    )
}