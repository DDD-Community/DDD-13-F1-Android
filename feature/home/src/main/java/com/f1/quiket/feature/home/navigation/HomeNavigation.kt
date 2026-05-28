package com.f1.quiket.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.f1.quiket.feature.floating.presentation.navigation.AddSubjectDestination
import com.f1.quiket.feature.floating.presentation.navigation.CreateQuizDestination
import com.f1.quiket.feature.floating.presentation.navigation.ScheduleExamDestination
import com.f1.quiket.feature.floating.presentation.navigation.UploadDestination
import com.f1.quiket.feature.floating.presentation.screen.ScheduleExamScreen
import com.f1.quiket.feature.floating.presentation.screen.UploadScreen
import com.f1.quiket.feature.floating.presentation.screen.addsubject.AddSubjectScreen
import com.f1.quiket.feature.home.domain.model.QuizPlayMode
import com.f1.quiket.feature.home.domain.model.QuizTimerScope
import com.f1.quiket.feature.home.presentation.CreateQuizRoute
import com.f1.quiket.feature.home.presentation.HomeRoute
import com.f1.quiket.feature.home.presentation.QuizPlayAllRoute
import com.f1.quiket.feature.home.presentation.QuizResultRoute
import com.f1.quiket.feature.home.presentation.QuizStartRoute

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    isQuizGenerating: Boolean,
    activeQuizSessionId: String?,
    onQuizGenerationStarted: () -> Unit,
    onQuizGenerationFinished: () -> Unit,
    onQuizCreated: (String) -> Unit,
    onQuizPlayCompleted: () -> Unit,
    navigateToQuizStart: (String?) -> Unit,
    navigateToScheduleExam: () -> Unit,
    navigateToCreateQuiz: () -> Unit,
    navigateToUpload: () -> Unit,
    navigateToAddSubject: () -> Unit,
) {
    composable(route = HomeDestination.route) { backStackEntry ->
        HomeRoute(
            isQuizGenerating = isQuizGenerating,
            activeQuizSessionId = activeQuizSessionId,
            navigateToQuizStart = navigateToQuizStart,
            navigateToQuizResult = { playSessionId ->
                navController.navigate(QuizResultDestination.createRoute(playSessionId))
            },
            navigateToScheduleExam = navigateToScheduleExam,
            navigateToCreateQuiz = navigateToCreateQuiz,
            navigateToUpload = navigateToUpload,
            navigateToAddSubject = navigateToAddSubject,
            homeBackStackEntry = backStackEntry,
        )
    }

    composable(ScheduleExamDestination.route) {
        ScheduleExamScreen()
    }
    composable(CreateQuizDestination.route) {
        CreateQuizRoute(
            onBackClick = { navController.popBackStack() },
            onAddSubjectClick = { navController.navigate(AddSubjectDestination.route) },
            onQuizGenerationStarted = onQuizGenerationStarted,
            onQuizGenerationFinished = onQuizGenerationFinished,
            onQuizCreated = onQuizCreated,
        )
    }
    composable(
        route = QuizStartDestination.route,
        arguments = listOf(
            navArgument(QuizStartDestination.ARG_QUIZ_SESSION_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
        ),
    ) { backStackEntry ->
        val quizSessionId = backStackEntry.arguments
            ?.getString(QuizStartDestination.ARG_QUIZ_SESSION_ID)
        QuizStartRoute(
            onBackClick = { navController.popBackStack() },
            onStartClick = { config ->
                navController.navigate(
                    QuizPlayAllDestination.createRoute(
                        quizSessionId = quizSessionId,
                        playMode = config.playMode,
                        timerEnabled = config.timerEnabled,
                        timerScope = config.timerScope,
                        timerSeconds = config.timerSeconds,
                    ),
                )
            },
        )
    }
    composable(
        route = QuizPlayAllDestination.route,
        arguments = listOf(
            navArgument(QuizPlayAllDestination.ARG_QUIZ_SESSION_ID) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(QuizPlayAllDestination.ARG_PLAY_MODE) {
                type = NavType.StringType
                defaultValue = QuizPlayMode.AllAtOnce.wireValue
            },
            navArgument(QuizPlayAllDestination.ARG_TIMER_ENABLED) {
                type = NavType.BoolType
                defaultValue = false
            },
            navArgument(QuizPlayAllDestination.ARG_TIMER_SCOPE) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(QuizPlayAllDestination.ARG_TIMER_SECONDS) {
                type = NavType.IntType
                defaultValue = -1
            },
        ),
    ) { backStackEntry ->
        val quizSessionId = backStackEntry.arguments
            ?.getString(QuizPlayAllDestination.ARG_QUIZ_SESSION_ID)
            ?.takeIf { id -> id.isNotBlank() }
        val playMode = backStackEntry.arguments
            ?.getString(QuizPlayAllDestination.ARG_PLAY_MODE)
            .toQuizPlayMode()
        val timerEnabled = backStackEntry.arguments
            ?.getBoolean(QuizPlayAllDestination.ARG_TIMER_ENABLED)
            ?: false
        val timerScope = backStackEntry.arguments
            ?.getString(QuizPlayAllDestination.ARG_TIMER_SCOPE)
            .toQuizTimerScope()
        val timerSeconds = backStackEntry.arguments
            ?.getInt(QuizPlayAllDestination.ARG_TIMER_SECONDS)
            ?.takeIf { seconds -> seconds > 0 }
        QuizPlayAllRoute(
            quizSessionId = quizSessionId,
            playMode = playMode,
            timerEnabled = timerEnabled,
            timerScope = timerScope,
            timerSeconds = timerSeconds,
            onCloseClick = { navController.popBackStack() },
            onResultReady = { playSessionId ->
                onQuizPlayCompleted()
                navController.navigate(QuizResultDestination.createRoute(playSessionId)) {
                    popUpTo(QuizPlayAllDestination.route) {
                        inclusive = true
                    }
                }
            },
        )
    }
    composable(
        route = QuizResultDestination.route,
        arguments = listOf(
            navArgument(QuizResultDestination.ARG_PLAY_SESSION_ID) {
                type = NavType.StringType
            },
        ),
    ) { backStackEntry ->
        val playSessionId = backStackEntry.arguments
            ?.getString(QuizResultDestination.ARG_PLAY_SESSION_ID)
            .orEmpty()
        QuizResultRoute(
            playSessionId = playSessionId,
            onBackClick = {
                navController.popBackStack(HomeDestination.route, inclusive = false)
            },
        )
    }
    composable(UploadDestination.route) {
        UploadScreen(
            onBackClick = { navController.popBackStack() },
            onNextClick = { navController.popBackStack() },
        )
    }
    composable(AddSubjectDestination.route) {
        AddSubjectScreen(
            onDismiss = { navController.popBackStack() },
            onFinish = {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("subject_created", true)
                navController.popBackStack()
            },
        )
    }
}

private fun String?.toQuizPlayMode(): QuizPlayMode =
    QuizPlayMode.entries.firstOrNull { mode -> mode.wireValue == this }
        ?: QuizPlayMode.AllAtOnce

private fun String?.toQuizTimerScope(): QuizTimerScope? =
    QuizTimerScope.entries.firstOrNull { scope -> scope.wireValue == this }
