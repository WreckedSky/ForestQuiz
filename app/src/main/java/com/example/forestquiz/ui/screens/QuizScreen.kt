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
import androidx.compose.ui.unit.dp
import com.example.forestquiz.viewmodel.QuizViewModel

@Composable
fun QuizScreen(viewModel: QuizViewModel, onQuizFinished: () -> Unit) {
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
                    !state.isAnswerSubmitted -> MaterialTheme.colorScheme.primary
                    option != currentQuestion.a && isSelected -> Color.Red // Wrong selection
                    option == currentQuestion.a -> Color.Green // Correct answer
                    else -> MaterialTheme.colorScheme.primary // Other options
                }

                Button(
                    onClick = { if (!state.isAnswerSubmitted) viewModel.onOptionSelected(option) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    shape = RectangleShape,
                    border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.secondary) else null,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text(text = option)
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (state.isAnswerSubmitted) {
                val resultText = if (state.isCorrect) "Correct!" else "Wrong! The answer is: ${currentQuestion.a}"
                Text(text = resultText, color = if (state.isCorrect) Color.Green else Color.Red)
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