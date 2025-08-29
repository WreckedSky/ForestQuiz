package com.example.forestquiz.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.forestquiz.ui.screens.ChoiceScreen
import com.example.forestquiz.ui.screens.FrontScreen
import com.example.forestquiz.ui.screens.LearnScreen
import com.example.forestquiz.ui.screens.QuizScreen
import com.example.forestquiz.ui.screens.RandomQuizScreen
import com.example.forestquiz.ui.screens.ResultsScreen
import com.example.forestquiz.viewmodel.QuizViewModel


sealed class Screen(val route: String) {
    object Front : Screen("front")
    object WeeklyQuizChoice : Screen("weekly_quiz_choice")
    object LearnChoice : Screen("learn_choice")
    object RandomQuizSetup : Screen("random_quiz_setup")
    object Quiz : Screen("quiz")
    object Learn : Screen("learn")
    object Results : Screen("results")
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val quizViewModel: QuizViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Front.route) {
        composable(Screen.Front.route) {
            FrontScreen(
                onNavigateToWeeklyQuiz = { navController.navigate(Screen.WeeklyQuizChoice.route) },
                onNavigateToLearn = { navController.navigate(Screen.LearnChoice.route) },
                onNavigateToRandomQuiz = { navController.navigate(Screen.RandomQuizSetup.route) }
            )
        }

        composable(Screen.WeeklyQuizChoice.route) {
            ChoiceScreen(
                viewModel = quizViewModel,
                onWeekSelected = {
                    navController.navigate(Screen.Quiz.route)
                },
                mode = "Quiz"
            )
        }

        composable(Screen.LearnChoice.route) {
            ChoiceScreen(
                viewModel = quizViewModel,
                onWeekSelected = {
                    navController.navigate(Screen.Learn.route)
                },
                mode = "Learn"
            )
        }

        composable(Screen.RandomQuizSetup.route) {
            RandomQuizScreen(
                viewModel = quizViewModel,
                onQuizFinished = {
                    navController.popBackStack()
                    navController.navigate(Screen.Results.route)
                }
            )
        }

        composable(Screen.Quiz.route) {
            QuizScreen(
                viewModel = quizViewModel,
                onQuizFinished = {
                    navController.popBackStack()
                    navController.navigate(Screen.Results.route)
                }
            )
        }

        composable(Screen.Learn.route) {
            LearnScreen(viewModel = quizViewModel, onNavigateHome = {
                navController.popBackStack(Screen.Front.route, inclusive = false)
            })
        }

        composable(Screen.Results.route) {
            ResultsScreen(viewModel = quizViewModel, onNavigateHome = {
                navController.popBackStack(Screen.Front.route, inclusive = false)
            })
        }
    }
}