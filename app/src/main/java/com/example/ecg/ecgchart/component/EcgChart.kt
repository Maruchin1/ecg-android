package com.example.ecg.ecgchart.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberEnd
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberTop
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.Scroll
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer

/**
 * Tu mas zgłówny komponent UI z wykresem. Do wyświetlanai wykresu używam biblioteki Vico
 * https://www.patrykandpatrick.com/vico/guide/latest która ma bardzo dużo opcji konfiguracji.
 */
@Composable
fun EcgChart(measurements: List<Double>, modifier: Modifier = Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val scrollState = rememberVicoScrollState()

    LaunchedEffect(measurements) {
        modelProducer.runTransaction { lineSeries { series(measurements) } }
        scrollState.scroll(Scroll.Absolute.End)
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(fill(Color.Red)),
                        areaFill = null
                    )
                ),
                rangeProvider = remember(measurements) {
                    CartesianLayerRangeProvider.fixed(
                        /**
                         * Wykres zakłada żę dane są wyskalowane w zakresie od -1 do 1.
                         * Jak chcesz inny zakres to zmień minY i maxY
                         */
                        minY = -1.0,
                        maxY = 1.0,
                        minX = 0.0,
                        /**
                         * Tu jest taki myk żeby wykres się automatycznie przesuwał.
                         * 40 to jest liczba pomiarów które zmieszczą się początkowo na wykresie.
                         * Wynika to z faktu że szerokość wykresu to 600dp a odstęp między punktami
                         * to 15dp. Jak pomiarów będzie więcej niż 40 to wykres będzie się przesuwał.
                         */
                        maxX = maxOf(40.0, measurements.size.toDouble())
                    )
                },
                pointSpacing = 15.dp
            ),
            /**
             * Wszystkie osie domyśłnie wyczysciłem z dodatkowych podziałek, etykiet itp. Jak
             * chcesz tutaj coś bardziej przybajerzyć to da się to konfigurować.
             */
            startAxis = VerticalAxis.rememberStart(
                line = rememberAxisLineComponent(
                    fill = fill(MaterialTheme.colorScheme.primary)
                ),
                guideline = null,
                tick = null,
                label = null
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                line = rememberAxisLineComponent(
                    fill = fill(MaterialTheme.colorScheme.primary)
                ),
                guideline = null,
                tick = null,
                label = null,
            ),
            topAxis = HorizontalAxis.rememberTop(
                line = rememberAxisLineComponent(
                    fill = fill(MaterialTheme.colorScheme.primary)
                ),
                guideline = null,
                tick = null,
                label = null,
            ),
            endAxis = VerticalAxis.rememberEnd(
                line = rememberAxisLineComponent(
                    fill = fill(MaterialTheme.colorScheme.primary)
                ),
                guideline = null,
                tick = null,
                label = null
            )
        ),
        modelProducer = modelProducer,
        scrollState = scrollState,
        animationSpec = null,
        runInitialAnimation = false,
        modifier = Modifier
            .size(600.dp, 300.dp)
            .then(modifier)
    )
}