package com.example.turingmachineapplication.core.presentation.navigation

import com.example.turingmachineapplication.Algorithm

sealed class Screen(val route: String) {
    data object TmInput: Screen("tm_input")
    object TmProcess: Screen("tm_process?algorithm={algorithm}?tape={tape}") {
        fun createRoute(algorithm: Algorithm, tape: String) = "tm_process?algorithm=${Algorithm.toString(algorithm)}?tape=$tape"
    }
}