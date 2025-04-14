package com.example.turingmachineapplication.core.presentation.navigation

import com.example.turingmachineapplication.Algorithm
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    data object TmInput: Screen("tm_input")
    data object TmProcess : Screen("tm_process?algorithm={algorithm}&tape={tape}") {
        fun withArgs(algorithm: String, tape: String) = "tm_process?algorithm=$algorithm&tape=$tape"
    }
}