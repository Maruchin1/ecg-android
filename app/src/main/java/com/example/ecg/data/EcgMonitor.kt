package com.example.ecg.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Tu jest główny obiekt który musisz zmodyfikować i dodać implementację komunikacji z urządzeniem.
 */
object EcgMonitor {

    /**
     * Tutaj mamy strumień z pomiarami które są potem wyświetlane na ekranie. Prywatny strumień
     * jest Mutable i private więc można go aktualizować tylko wewnatrz tego obiektu.
     * Drugi strumień jest przeszkształceniem tego pierwszego to formu publicznej i niemutowalnej.
     * Tego strumienia używa UI ale ni emoże go modygfiować.
     */
    private val _measurements = MutableStateFlow(EcgConfig.initialMeasurements)
    val measurements = _measurements.asStateFlow()

    /**
     * Zbieranie pomiarów dobrze żeby działo się w tle. Ten scope jest ustawiony na specialnym
     * wątku IO który jest dedykowany do komunikacji wejścia wyjścia.
     */
    private val monitorScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Tutaj trzymamy referencję to Joba w którym zbierane są pomiary. Ten job możemy potem anulować
     * gdy chcemy zatrzymać zbieranie pomiarów. Jeśli pomiary nie będą zbierane z urządzenia w
     * żadnej pętli tylko urządzenie będzie ci je wysyłać to Job będzie raczej zbędny.
     */
    private var monitoringJob: Job? = null

    /**
     * Ta kolejka docelowo do usunięcia. Trzymam tu tylko testowe dane.
     */
    private val measurementQueue = mutableListOf<Double>()

    /**
     * Tu wiadomo, będziesz się łączył z urządzeniem
     */
    fun connect() {
        measurementQueue.addAll(testEcgMeasurements)
    }

    /**
     * Tu analogicznie, tylko do rozłączenia
     */
    fun disconnect() {
        monitoringJob?.cancel()
        measurementQueue.clear()
    }

    /**
     * W tej funkcji odpalam asynchroniczną pętlę które mi symuluje zbieranie pomiarów z urządznia.
     * Jak komunikacja będzie odwrtona że to urządzenie wysyła pomiary to ta pętla będzie zbędna.
     */
    fun startMonitoring() {
        monitoringJob = monitorScope.launch {
            /**
             * Ten while(isActive) kręci się tak długo jak Job który odpaliliśmy jest aktywny.
             * Job przestaje być aktywny jak wywołamy na nim cancel().
             */
            while (isActive) {
                val nextMeasurement = measurementQueue.removeAt(0)
                /**
                 * Ta linijka jest najważniesza. Każdy nowy pomiar musisz dodać do listy już
                 * zebranych pomiarów. Lista jest w strumieniu a strumień aktualizujemy funkcją
                 * update. Samej listy nie modyfikujemy tylko tworzymy nową listę poprzez
                 * wzięcie aktualnej listy i dodanie do niej nowego pomiaru.
                 *
                 * Jest to bardzo ważne żeby robić ten update i tworzyć nową listę bo to zapwenia
                 * nam poprawne działanie tego procesu na wątku w tle.
                 */
                _measurements.update { it + nextMeasurement }
                if (measurementQueue.isEmpty()) {
                    measurementQueue.addAll(testEcgMeasurements)
                }
                delay(EcgConfig.deviceSampling)
            }
        }
    }

    /**
     * To zatrzymujemy zbieranie pomiarów. Jeśli będziesz je odczytywał w pętli to bedziesz musiał
     * anulować Joba żeby przerwać pętlę. Jeśłi urządzenie będzie ci wysyłać pomiary to Job będzie
     * zbędny
     */
    fun stopMonitoring() {
        monitoringJob?.cancel()
    }

    /**
     * Tutaj resetujemy listę pomiarów do stanu początkowego
     */
    fun resetMeasurements() {
        _measurements.update { EcgConfig.initialMeasurements }
    }
}