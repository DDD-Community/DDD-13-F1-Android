package com.f1.quiket.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.f1.quiket.feature.home.floating.AddSubjectScreen
import com.f1.quiket.feature.home.floating.ScheduleExamScreen
import com.f1.quiket.feature.home.floating.UploadScreen
import com.f1.quiket.feature.home.presentation.CreateQuizRoute
import com.f1.quiket.feature.home.presentation.HomeRoute

fun NavGraphBuilder.homeGraph(
    navigateToScheduleExam: () -> Unit,
    navigateToCreateQuiz: () -> Unit,
    navigateToUpload: () -> Unit,
    navigateToAddSubject: () -> Unit,
    onBackClick: () -> Unit,
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
        CreateQuizRoute(
            onBackClick = onBackClick,
            onAddSubjectClick = navigateToAddSubject,
        )
    }
    composable(UploadDestination.route) {
        UploadScreen()
    }
    composable(AddSubjectDestination.route) {
        AddSubjectScreen(onBackClick = onBackClick)
    }

}
