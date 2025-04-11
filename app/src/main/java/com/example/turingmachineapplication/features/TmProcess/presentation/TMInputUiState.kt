package com.example.turingmachineapplication.features.TmProcess.presentation

data class TMInputUiState (
    val tape: MutableList<Char> = mutableListOf('0', '0', '0', '1', '1', '1', '*', '1', '1', '0', '0', '0'),
    val headPosition: Int = 0,
    val isOver: Boolean = false
)