package com.example.turingmachineapplication.presentation

import androidx.lifecycle.ViewModel
import com.example.turingmachineapplication.TuringMachine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {
    private var _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private var tm = TuringMachine(_uiState.value.tape)
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

