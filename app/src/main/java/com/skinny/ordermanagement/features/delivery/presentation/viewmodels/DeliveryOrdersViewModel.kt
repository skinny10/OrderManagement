package com.skinny.ordermanagement.features.delivery.presentation.viewmodels

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skinny.ordermanagement.features.delivery.domain.usecases.GetDeliveryOrdersUseCase
import com.skinny.ordermanagement.features.delivery.domain.usecases.UpdateDeliveryOrderStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeliveryOrderUi(
    val id: String,
    val clientName: String,
    val address: String,
    val total: Double,
    val status: String,
    val date: String
)

data class DeliveryOrdersUiState(
    val orders: List<DeliveryOrderUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isListening: Boolean = false,          // ← nuevo: micrófono activo
    val voiceResult: String? = null,           // ← nuevo: resultado del comando
    val voiceError: String? = null             // ← nuevo: error de voz
)

@HiltViewModel
class DeliveryOrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetDeliveryOrdersUseCase,
    private val updateStatusUseCase: UpdateDeliveryOrderStatusUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeliveryOrdersUiState())
    val uiState: StateFlow<DeliveryOrdersUiState> = _uiState.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null

    init { loadOrders() }

    fun loadOrders() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getOrdersUseCase().onSuccess { orders ->
                val orderUis = orders.map { order ->
                    DeliveryOrderUi(
                        id         = order.id,
                        clientName = order.clientName,
                        address    = order.address,
                        total      = order.total,
                        status     = order.status,
                        date       = order.date
                    )
                }
                _uiState.value = DeliveryOrdersUiState(orders = orderUis, isLoading = false)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(isLoading = false, error = error.message)
            }
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            updateStatusUseCase(orderId, newStatus).onSuccess {
                loadOrders()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message)
            }
        }
    }

    // ← NUEVO: iniciar escucha de voz
    fun startListening(orderId: String, onStatusDetected: (String) -> Unit) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _uiState.value = _uiState.value.copy(
                voiceError = "Reconocimiento de voz no disponible"
            )
            return
        }

        speechRecognizer?.destroy()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-MX")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _uiState.value = _uiState.value.copy(isListening = true, voiceError = null)
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val spoken = matches?.firstOrNull()?.lowercase() ?: ""

                val status = when {
                    "entregado" in spoken  -> "Entregado"
                    "en camino" in spoken  -> "En camino"
                    "preparando" in spoken -> "Preparando"
                    "pendiente" in spoken  -> "Pendiente"
                    else -> null
                }

                if (status != null) {
                    _uiState.value = _uiState.value.copy(
                        isListening = false,
                        voiceResult = "Comando detectado: $status"
                    )
                    onStatusDetected(status)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isListening = false,
                        voiceError = "No se reconoció el comando: \"$spoken\""
                    )
                }
            }
            override fun onError(error: Int) {
                _uiState.value = _uiState.value.copy(
                    isListening = false,
                    voiceError = "Error al escuchar, intenta de nuevo"
                )
            }
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer?.startListening(intent)
    }

    fun clearVoiceResult() {
        _uiState.value = _uiState.value.copy(voiceResult = null, voiceError = null)
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
    }
}