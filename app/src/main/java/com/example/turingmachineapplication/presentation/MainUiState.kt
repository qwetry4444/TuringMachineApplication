package com.example.turingmachineapplication.presentation

import com.example.turingmachineapplication.TuringMachine

data class MainUiState (
    val tape: MutableList<Char> = mutableListOf('0', '0', '0', '1', '1', '1', '*', '1', '1', '0', '0', '0'),
    val headPosition: Int = 0,
    val isOver: Boolean = false
)