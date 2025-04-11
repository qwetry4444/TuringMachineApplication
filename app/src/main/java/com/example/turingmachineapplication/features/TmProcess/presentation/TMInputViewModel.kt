package com.example.turingmachineapplication.features.TmProcess.presentation

import androidx.lifecycle.ViewModel
import com.example.turingmachineapplication.Algorithm
import com.example.turingmachineapplication.TuringMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TMInputViewModel: ViewModel() {
    private var _uiState = MutableStateFlow(TMInputUiState())
    val uiState = _uiState.asStateFlow()

    private var tm = TuringMachine(_uiState.value.tape, Algorithm.MultiplicationUnary)

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
        }
    }

    sealed class Action {
        data object NextStep: Action()
    }
}

