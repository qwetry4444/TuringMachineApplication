@file:Suppress("UNREACHABLE_CODE")

package com.example.turingmachineapplication.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.turingmachineapplication.core.presentation.navigation.AppNavGraph
import com.example.turingmachineapplication.core.presentation.navigation.Screen
import com.example.turingmachineapplication.ui.theme.TuringMachineApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TuringMachineApplicationTheme {
                AppNavGraph(
                    rememberNavController(),
                    Screen.TmInput
                )
            }
        }
    }
}


