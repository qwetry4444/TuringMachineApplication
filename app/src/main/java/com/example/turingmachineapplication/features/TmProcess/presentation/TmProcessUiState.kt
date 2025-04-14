package com.example.turingmachineapplication.features.TmProcess.presentation

import com.example.turingmachineapplication.Algorithm


data class TmProcessUiState (
    val tape: MutableList<Char> = mutableListOf('0', '1', '1', '*', '1', '1', '=', '0', '0'),
    val algorithm: Algorithm,
    val headPosition: Int = 0,
    val isOver: Boolean = false
)