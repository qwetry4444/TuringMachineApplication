package com.example.turingmachineapplication.features.TmProcess.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.turingmachineapplication.core.domain.TurginMachineLogic.Algorithm
import com.example.turingmachineapplication.core.domain.TurginMachineLogic.TuringMachine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TmProcessViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val algorithm = savedStateHandle.get<String>("algorithm")?.let { Algorithm.valueOf(it) }
        ?: Algorithm.AdditionUnary
    private val tape = savedStateHandle.get<String>("tape") ?: error("No tape")

    private val _uiState = MutableStateFlow(
        TmProcessUiState(algorithm = algorithm, tape = tape.toMutableList())
    )
    val uiState = _uiState.asStateFlow()

    private var tm = TuringMachine(_uiState.value.tape, _uiState.value.algorithm)

    private var _isOver: Boolean = false

    fun processAction(action: Action) {
        when (action) {
            Action.NextStep -> {
                _isOver = tm.step()
                _uiState.update {
                    it.copy(
                        headPosition = tm.getHeadPosition(),
                        tape = tm.getTape(),
                        isOver = _isOver
                    )
                }
            }

            else -> {}
        }
    }

    sealed class Action {
        data object NextStep: Action()
    }
}