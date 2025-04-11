package com.example.turingmachineapplication.features.TMInput.presentation

import com.example.turingmachineapplication.Algorithm

data class TmProcessUiState(
    val algorithm: Algorithm = Algorithm.AdditionUnary,
    val tape: String = ""
)