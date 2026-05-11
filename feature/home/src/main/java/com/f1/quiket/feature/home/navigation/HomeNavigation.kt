package com.f1.quiket.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.f1.quiket.feature.floating.presentation.navigation.AddSubjectDestination
import com.f1.quiket.feature.floating.presentation.navigation.CreateQuizDestination
import com.f1.quiket.feature.floating.presentation.navigation.ScheduleExamDestination
import com.f1.quiket.feature.floating.presentation.navigation.UploadDestination
import com.f1.quiket.feature.floating.presentation.screen.CreateQuizScreen
import com.f1.quiket.feature.floating.presentation.screen.ScheduleExamScreen
import com.f1.quiket.feature.floating.presentation.screen.UploadScreen
import com.f1.quiket.feature.floating.presentation.screen.addsubject.AddSubjectScreen
import com.f1.quiket.feature.home.presentation.HomeRoute

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    navigateToScheduleExam: () -> Unit,
    navigateToCreateQuiz: () -> Unit,
    navigateToUpload: () -> Unit,
    navigateToAddSubject: () -> Unit
) {
    composable(route = HomeDestination.route) {
        HomeRoute(
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
        CreateQuizScreen()
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