package com.example.turingmachineapplication.features.TmInput.presentation

import com.example.turingmachineapplication.core.domain.TurginMachineLogic.Algorithm

data class TmInputUiState (
    val algorithm: Algorithm = Algorithm.AdditionUnary,
    val tape: String = "",
    val isMenuExpanded: Boolean = false
)