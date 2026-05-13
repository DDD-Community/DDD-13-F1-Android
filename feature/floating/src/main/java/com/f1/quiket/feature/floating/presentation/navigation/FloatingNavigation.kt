package com.f1.quiket.feature.floating.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.f1.quiket.feature.floating.domain.model.ExamType
import com.f1.quiket.feature.floating.domain.model.StudyField
import com.f1.quiket.feature.floating.domain.model.StudyPurpose
import com.f1.quiket.feature.floating.domain.model.UsagePurpose
import com.f1.quiket.feature.floating.presentation.screen.CreateQuizScreen
import com.f1.quiket.feature.floating.presentation.screen.ScheduleExamScreen
import com.f1.quiket.feature.floating.presentation.screen.UploadScreen
import com.f1.quiket.feature.floating.presentation.screen.addsubject.AddSubjectScreen
import com.f1.quiket.feature.floating.presentation.screen.addsubject.AddSubjectStep1Screen
import com.f1.quiket.feature.floating.presentation.screen.addsubject.AddSubjectStep2Screen
import com.f1.quiket.feature.floating.presentation.screen.addsubject.AddSubjectStep3Screen

fun NavGraphBuilder.floatingGraph(
    onFinish: () -> Unit,
) {
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
        AddSubjectScreen(onFinish = onFinish)
    }
}

fun NavGraphBuilder.addSubjectGraph(
    navController: NavController,
    onFinish: () -> Unit,
) {
    // 1depth
    composable(route = AddSubjectStep1Destination.route) {
        AddSubjectStep1Screen(
            onBackClick = { navController.popBackStack() },
            onSkipClick = onFinish,
            onNextClick = { subjectName, purpose ->
                // subjectName은 ViewModel로 전달하거나 SavedStateHandle 활용 권장
                navController.navigate(AddSubjectStep2Destination.createRoute(purpose))
            },
        )
    }

    // 2depth
    composable(
        route = AddSubjectStep2Destination.route,
        arguments = listOf(
            navArgument(AddSubjectStep2Destination.ARG_PURPOSE) { type = NavType.StringType },
        ),
    ) { backStack ->
        val purposeName = backStack.arguments?.getString(AddSubjectStep2Destination.ARG_PURPOSE)
        val purpose = purposeName?.let { runCatching { StudyPurpose.valueOf(it) }.getOrNull() }
            ?: StudyPurpose.EXAM

        AddSubjectStep2Screen(
            studyPurpose = purpose,
            onBackClick = { navController.popBackStack() },
            onSkipClick = onFinish,
            onNextClick = { selection ->
                val route = when (selection) {
                    is ExamType -> AddSubjectStep3Destination.createRoute(purpose, examType = selection)
                    is StudyField -> AddSubjectStep3Destination.createRoute(purpose, studyField = selection)
                    is UsagePurpose -> AddSubjectStep3Destination.createRoute(purpose, usagePurpose = selection)
                    else -> AddSubjectStep3Destination.createRoute(purpose)
                }
                navController.navigate(route)
            },
        )
    }

    // 3depth
    composable(
        route = AddSubjectStep3Destination.route,
        arguments = listOf(
            navArgument(AddSubjectStep3Destination.ARG_PURPOSE) { type = NavType.StringType },
            navArgument(AddSubjectStep3Destination.ARG_EXAM_TYPE) {
                type = NavType.StringType; defaultValue = ""
            },
            navArgument(AddSubjectStep3Destination.ARG_FIELD) {
                type = NavType.StringType; defaultValue = ""
            },
            navArgument(AddSubjectStep3Destination.ARG_USAGE) {
                type = NavType.StringType; defaultValue = ""
            },
        ),
    ) { backStack ->
        val args = backStack.arguments

        val purpose = args?.getString(AddSubjectStep3Destination.ARG_PURPOSE)
            ?.let { runCatching { StudyPurpose.valueOf(it) }.getOrNull() }
            ?: StudyPurpose.EXAM

        val examType = args?.getString(AddSubjectStep3Destination.ARG_EXAM_TYPE)
            ?.takeIf { it.isNotEmpty() }
            ?.let { runCatching { ExamType.valueOf(it) }.getOrNull() }

        val studyField = args?.getString(AddSubjectStep3Destination.ARG_FIELD)
            ?.takeIf { it.isNotEmpty() }
            ?.let { runCatching { StudyField.valueOf(it) }.getOrNull() }

        val usagePurpose = args?.getString(AddSubjectStep3Destination.ARG_USAGE)
            ?.takeIf { it.isNotEmpty() }
            ?.let { runCatching { UsagePurpose.valueOf(it) }.getOrNull() }

        AddSubjectStep3Screen(
            studyPurpose = purpose,
            examType = examType,
            studyField = studyField,
            usagePurpose = usagePurpose,
            onBackClick = { navController.popBackStack() },
            onSkipClick = onFinish,
            onCreateClick = onFinish,
        )
    }
}