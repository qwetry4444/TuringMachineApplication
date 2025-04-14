package com.example.turingmachineapplication.features.TmProcess.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController


@Composable
fun TmProcessPage(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val viewModel = hiltViewModel<TmProcessViewModel>()
    val uiState = viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Text(
            text = "Текущая лента",
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Tape(
            tape = uiState.value.tape,
            currentPosition = uiState.value.headPosition
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            IconButton(
                onClick = { viewModel.processAction(TmProcessViewModel.Action.NextStep) },
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF4CAF50), shape = androidx.compose.foundation.shape.CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Следующий шаг",
                    tint = Color.White
                )
            }

            if (uiState.value.isOver) {
                Text(
                    text = "Конец",
                    color = Color.Red,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Tape(
    tape: List<Char>,
    currentPosition: Int,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        tape.forEachIndexed { index, char ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .border(
                        width = 2.dp,
                        color = if (index == currentPosition) Color(0xFF4CAF50) else Color.Gray,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = if (index == currentPosition) Color(0xFFB2FF59) else Color.White,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    )
            ) {
                Text(text = char.toString())
            }
        }
    }
}