package com.example.forestquiz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.forestquiz.viewmodel.QuizViewModel

@Composable
fun ChoiceScreen(
    viewModel: QuizViewModel,
    onWeekSelected: () -> Unit,
    mode: String
) {
    Scaffold(
        topBar = {
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(12) { weekIndex ->
                val weekNumber = weekIndex + 1
                Button(
                    onClick = {
                        viewModel.prepareWeeklySession(weekNumber)
                        onWeekSelected()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Week $weekNumber")
                }
            }
        }
    }
}