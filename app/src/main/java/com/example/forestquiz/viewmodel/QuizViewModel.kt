package com.example.forestquiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forestquiz.data.Qna
import com.example.forestquiz.data.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random


data class AppUiState(
    val questions: List<Qna> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)


data class QuizState(
    val activeQuestions: List<Qna> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val shuffledOptions: List<String> = emptyList(),
    val selectedOption: String? = null,
    val isAnswerSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val score: Int = 0,
    val wrongAnswers: List<Qna> = emptyList(),
    val isQuizFinished: Boolean = false
)

class QuizViewModel : ViewModel() {

    private val repository = QuizRepository()

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState: StateFlow<AppUiState> = _appUiState.asStateFlow()

    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    init {
        loadAllQuestions()
    }

    private fun loadAllQuestions() {
        viewModelScope.launch {
            _appUiState.update { it.copy(isLoading = true) }
            val questions = repository.getAllQuestions()
            _appUiState.update {
                it.copy(questions = questions, isLoading = false)
            }
        }
    }

    fun prepareWeeklySession(week: Int) {
        val allQuestions = _appUiState.value.questions
        val startIndex = (week - 1) * 10
        if (allQuestions.isNotEmpty() && startIndex < allQuestions.size) {
            val questionsForWeek = allQuestions.subList(startIndex, startIndex + 10)
            startSession(questionsForWeek)
        }
    }

    fun prepareRandomQuiz(count: Int) {
        val allQuestions = _appUiState.value.questions
        if (allQuestions.isNotEmpty()) {
            val randomQuestions = allQuestions.shuffled().take(count)
            startSession(randomQuestions)
        }
    }

    private fun startSession(questions: List<Qna>) {
        _quizState.value = QuizState(activeQuestions = questions)
        loadQuestion(0)
    }

    private fun loadQuestion(index: Int) {
        val question = _quizState.value.activeQuestions[index]
        val options = listOf(question.a, question.o1, question.o2, question.o3)

        val shuffled = options.shuffled(Random(System.currentTimeMillis()))
        _quizState.update {
            it.copy(
                currentQuestionIndex = index,
                shuffledOptions = shuffled,
                selectedOption = null,
                isAnswerSubmitted = false
            )
        }
    }

    fun onOptionSelected(option: String) {
        _quizState.update { it.copy(selectedOption = option) }
    }

    fun submitAnswer() {
        val state = _quizState.value
        val currentQuestion = state.activeQuestions[state.currentQuestionIndex]
        val isCorrect = state.selectedOption == currentQuestion.a

        _quizState.update {
            it.copy(
                isAnswerSubmitted = true,
                isCorrect = isCorrect,
                score = if (isCorrect) it.score + 1 else it.score,
                wrongAnswers = if (!isCorrect) it.wrongAnswers + currentQuestion else it.wrongAnswers
            )
        }
    }

    fun nextQuestion() {
        val nextIndex = _quizState.value.currentQuestionIndex + 1
        if (nextIndex < _quizState.value.activeQuestions.size) {
            loadQuestion(nextIndex)
        } else {

            _quizState.update { it.copy(isQuizFinished = true) }
        }
    }

    fun resetQuiz() {
        _quizState.value = QuizState()
    }
}