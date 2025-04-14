package com.example.turingmachineapplication.features.TmInput.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.turingmachineapplication.core.domain.TurginMachineLogic.Algorithm
import com.example.turingmachineapplication.core.presentation.navigation.Screen

@Composable
fun TmInputPage(navController: NavHostController, modifier: Modifier = Modifier) {
    val viewModel: TmInputViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 64.dp)
    ) {

        Text(
            text = "Введите ленту",
            style = MaterialTheme.typography.titleMedium
        )

        TextField(
            value = uiState.tape,
            onValueChange = { viewModel.processAction(TmInputAction.TapeInput(it)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Выберите алгоритм",
            style = MaterialTheme.typography.titleMedium
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .clickable { viewModel.processAction(TmInputAction.MenuDismiss) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Algorithm.toString(uiState.algorithm),
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Выбрать алгоритм"
                )
            }

            DropdownMenu(
                expanded = uiState.isMenuExpanded,
                onDismissRequest = { viewModel.processAction(TmInputAction.MenuDismiss) },
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            ) {
                Algorithm.entries.forEach { algorithm ->
                    DropdownMenuItem(
                        text = { Text(Algorithm.toString(algorithm)) },
                        onClick = {
                            viewModel.processAction(TmInputAction.AlgorithmInput(algorithm))
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                navController.navigate(Screen.TmProcess.withArgs(uiState.algorithm.name, uiState.tape))

//                navController.currentBackStackEntry?.savedStateHandle?.set("algorithm", uiState.algorithm.name)
//                navController.currentBackStackEntry?.savedStateHandle?.set("tape", uiState.tape)
//                navController.navigate(Screen.TmProcess.route)

//                val entry = navController.currentBackStackEntry
//                entry?.savedStateHandle?.set("algorithm", uiState.algorithm.name)
//                entry?.savedStateHandle?.set("tape", uiState.tape)
//                navController.navigate(Screen.TmProcess.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Запустить")
        }
    }
}

