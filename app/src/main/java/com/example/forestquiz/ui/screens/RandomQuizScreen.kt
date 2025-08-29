package com.example.forestquiz.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.forestquiz.viewmodel.QuizViewModel

@Composable
fun RandomQuizScreen(viewModel: QuizViewModel, onQuizFinished: () -> Unit) {
    val state by viewModel.quizState.collectAsState()

    // When the quiz is marked as finished in the ViewModel, trigger navigation
    LaunchedEffect(state.isQuizFinished) {
        if (state.isQuizFinished) {
            onQuizFinished()
        }
    }

    if (state.activeQuestions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentQuestion = state.activeQuestions[state.currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Question ${state.currentQuestionIndex + 1}/${state.activeQuestions.size}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(text = currentQuestion.q, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(24.dp))

            // Display options
            state.shuffledOptions.forEach { option ->
                val isSelected = state.selectedOption == option
                val buttonColor = when {
                    !state.isAnswerSubmitted -> ButtonDefaults.buttonColors()
                    option == currentQuestion.a -> ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)) // Green
                    isSelected && option != currentQuestion.a -> ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)) // Red
                    else -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                }

                Button(
                    onClick = { if (!state.isAnswerSubmitted) viewModel.onOptionSelected(option) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RectangleShape,
                    border = if (isSelected && !state.isAnswerSubmitted) BorderStroke(2.dp, MaterialTheme.colorScheme.secondary) else null,
                    colors = buttonColor
                ) {
                    Text(text = option)
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (state.isAnswerSubmitted) {
                val resultText = if (state.isCorrect) "Correct!" else "Wrong!"
                val color = if (state.isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)
                Text(text = resultText, color = color, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                if(!state.isCorrect) {
                    Text(text = "The correct answer is: ${currentQuestion.a}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (state.isAnswerSubmitted) {
                        viewModel.nextQuestion()
                    } else {
                        viewModel.submitAnswer()
                    }
                },
                enabled = state.selectedOption != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isAnswerSubmitted) "Next" else "Submit")
            }
        }
    }
}