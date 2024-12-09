package com.example.ecg.data

import kotlin.time.Duration.Companion.milliseconds

object EcgConfig {

    val deviceSampling = 40.milliseconds

    val chartSampling = 40.milliseconds

    val initialMeasurements = listOf(0.0)
}