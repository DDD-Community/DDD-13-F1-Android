package com.f1.quiket.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.f1.quiket.feature.floating.presentation.navigation.AddSubjectDestination
import com.f1.quiket.feature.floating.presentation.navigation.CreateQuizDestination
import com.f1.quiket.feature.floating.presentation.navigation.ScheduleExamDestination
import com.f1.quiket.feature.floating.presentation.navigation.UploadDestination
import com.f1.quiket.feature.floating.presentation.screen.ScheduleExamScreen
import com.f1.quiket.feature.floating.presentation.screen.UploadScreen
import com.f1.quiket.feature.floating.presentation.screen.addsubject.AddSubjectScreen
import com.f1.quiket.feature.home.presentation.CreateQuizRoute
import com.f1.quiket.feature.home.presentation.HomeRoute
import com.f1.quiket.feature.home.presentation.QuizPlayAllRoute
import com.f1.quiket.feature.home.presentation.QuizStartRoute

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    isQuizGenerating: Boolean,
    onQuizGenerationStarted: () -> Unit,
    navigateToQuizStart: () -> Unit,
    navigateToScheduleExam: () -> Unit,
    navigateToCreateQuiz: () -> Unit,
    navigateToUpload: () -> Unit,
    navigateToAddSubject: () -> Unit
) {
    composable(route = HomeDestination.route) {
        HomeRoute(
            isQuizGenerating = isQuizGenerating,
            navigateToQuizStart = navigateToQuizStart,
            navigateToScheduleExam = navigateToScheduleExam,
            navigateToCreateQuiz = navigateToCreateQuiz,
            navigateToUpload = navigateToUpload,
            navigateToAddSubject = navigateToAddSubject
        )
    }

    composable(ScheduleExamDestination.route) {
        ScheduleExamScreen()
    }
    composable(CreateQuizDestination.route) {
        CreateQuizRoute(
            onBackClick = { navController.popBackStack() },
            onAddSubjectClick = { navController.navigate(AddSubjectDestination.route) },
            onCreateQuizClick = onQuizGenerationStarted,
        )
    }
    composable(QuizStartDestination.route) {
        QuizStartRoute(
            onBackClick = { navController.popBackStack() },
            onStartClick = { navController.navigate(QuizPlayAllDestination.route) },
        )
    }
    composable(QuizPlayAllDestination.route) {
        QuizPlayAllRoute(
            onCloseClick = { navController.popBackStack() },
        )
    }
    composable(UploadDestination.route) {
        UploadScreen()
    }
    composable(AddSubjectDestination.route) {
        AddSubjectScreen(
            onDismiss = { navController.popBackStack() },
            onFinish = { navController.popBackStack() },
        )
    }
}
