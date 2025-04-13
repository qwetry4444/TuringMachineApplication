package com.example.turingmachineapplication.features.TmProcess.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.turingmachineapplication.features.TmInput.presentation.TmInputViewModel


@Composable
fun TmProcessPage(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val viewModel: TmProcessViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(24.dp)
    ) {
        Tape(uiState.value.tape, uiState.value.headPosition)
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.processAction(TmProcessViewModel.Action.NextStep)},
                modifier = Modifier
                    .background(Color.Green)
                    .size(48.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
            }
            Spacer(modifier = Modifier.width(64.dp))
            if (uiState.value.isOver){
                Text("Конец")
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Tape(tape: MutableList<Char>, currentPosition: Int, modifier: Modifier = Modifier){
    FlowRow(horizontalArrangement = Arrangement.Center) {
        tape.forEachIndexed { index, char ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(48.dp)
                    .border(2.dp, Color.Black)
                    .background(if (index == currentPosition) Color.Green else Color.Transparent),
            ) {
                Text(text = char.toString())
            }
        }
    }
}