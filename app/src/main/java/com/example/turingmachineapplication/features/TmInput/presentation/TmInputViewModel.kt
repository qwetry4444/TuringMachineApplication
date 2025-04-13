package com.example.turingmachineapplication.features.TmInput.presentation

import androidx.lifecycle.ViewModel
import com.example.turingmachineapplication.Algorithm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TmInputViewModel: ViewModel() {
    private var _uiState = MutableStateFlow(TmInputUiState())
    val uiState = _uiState.asStateFlow()

    fun processAction(action: TmInputAction) {
        when (action) {
            is TmInputAction.TapeInput -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        tape = action.newValue
                    )
                }
            }
            is TmInputAction.AlgorithmInput -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        algorithm = action.newAlgorithm,
                        isMenuExpanded = false
                    )
                }
            }
            is TmInputAction.MenuDismiss -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        isMenuExpanded = !uiState.value.isMenuExpanded
                    )
                }
            }
        }
    }
}

sealed class TmInputAction {
    data class TapeInput(val newValue: String) : TmInputAction()
    data class AlgorithmInput(val newAlgorithm: Algorithm) : TmInputAction()
    data object MenuDismiss : TmInputAction()
}