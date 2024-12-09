package com.example.ecg.ecgchart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecg.data.EcgConfig
import com.example.ecg.data.EcgMonitor
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn

/**
 * Ta klasa obsługuje stan ekranu z wykresem EKG. Z jednej strony wystawia ona dane
 * w formie modelu UiState. Z drugiej strony obsługuje akcje z ekranu poprzez publiczne funkcje
 * takie jak startMeasuring i stopMeasuring które są podłączone pod przyciski na ekranie.
 */
@OptIn(FlowPreview::class)
class EcgChartViewModel : ViewModel() {

    /**
     * Tutaj mamy strumień (Flow) danych z monitora EKG. W tym miejscu aplikujemy dodatkowe
     * próbkowanie dostosowane do potrzeb wyświetlania danych na wykresie. Jeśli dane z monitora
     * będą emitowane zbyt często to wykres nie wyrobi i się zatnie. Na razie w konfiguracji
     * ustawiłem próbkowanie na 40 milisekund ale możesz poeksperymentować z innymi watościami.
     */
    private val sampledMeasurements = EcgMonitor.measurements.sample(EcgConfig.chartSampling)

    /**
     * Tu mamy taki dodatkowy stan przy pomocy którego kontrolujemy które przycisku się wyświetlają
     * na ekranie czy "Rozpocznij" czy "Zatrzymaj".
     */
    private val isMeasuring = MutableStateFlow(false)

    /**
     * To jest takie nasze podejscie żeby dane do ekranu wystawiać w formie jednego strumienia z
     * jednym obiektem, który jest połączeniem danych z innych strumieni.
     */
    val uiState: StateFlow<EcgChartUiState> = combine(
        sampledMeasurements,
        isMeasuring,
        ::EcgChartUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EcgChartUiState()
    )

    /**
     * Ten blok jest wywoływany zaraz po utworzeniu instancji tej klasy. Czyli w praktyce przy
     * otwarciu ekranu. W tym miejscu najlpiej będzie nawiązać połączenie z urządzeniem
     */
    init {
        EcgMonitor.connect()
    }

    /**
     * Ta funkcja jest wywoływana przez przycisk na ekranie. Tutaj rozpoczynamy zbieranie pomiarów
     */
    fun startMeasuring() {
        EcgMonitor.startMonitoring()
        isMeasuring.value = true
    }

    /**
     * Tutaj analogicznie jak wyżej, funkcja podpięta pod przycisk, pozwala zatrzymać
     * zbieranie pomiarów
     */
    fun stopMeasuring() {
        EcgMonitor.stopMonitoring()
        isMeasuring.value = false
    }

    /**
     * Tu dałem dodatkową funkcję która pozwala wyczyścić pomiary, czyli wrócić do pustego wykresu
     */
    fun resetMeasurements() {
        EcgMonitor.resetMeasurements()
    }

    /**
     * Ta funkcja się wywoła w momencie wjścia z ekranu. W tej apce mamy jeden ekran czyli
     * w momencie wyjścia z aplikacji. W tym miejscu powinniśmy zakończyć połączenie z urządzeniem.
     */
    override fun onCleared() {
        super.onCleared()
        EcgMonitor.disconnect()
    }
}