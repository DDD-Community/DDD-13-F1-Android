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
import com.f1.quiket.feature.floating.presentation.screen.lectureselect.LectureSelectScreen

// ─── Floating Graph ────────────────────────────────────────────────────────────

fun NavGraphBuilder.floatingGraph(
    navController: NavController,
    onFinish: () -> Unit,
) {
    composable(ScheduleExamDestination.route) {
        ScheduleExamScreen()
    }
    composable(CreateQuizDestination.route) {
        CreateQuizScreen()
    }
    composable(UploadDestination.route) {
        UploadScreen(
            onBackClick = { navController.popBackStack() },
            onNextClick = onFinish,
        )
    }
    composable(AddSubjectDestination.route) {
        AddSubjectScreen(onFinish = onFinish)
    }
}

// ─── AddSubject Graph ──────────────────────────────────────────────────────────

fun NavGraphBuilder.addSubjectGraph(
    navController: NavController,
    onFinish: () -> Unit,
) {
    // 1depth
    composable(route = AddSubjectStep1Destination.route) {
        AddSubjectStep1Screen(
            onBackClick = { navController.popBackStack() },
            onSkipClick = { _ -> onFinish() },
            onNextClick = { subjectName, purpose ->
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
            onCreateClick = { _, _ -> onFinish() },
        )
    }
}

// ─── LectureUpload Graph ───────────────────────────────────────────────────────

/**
 * 과목 선택 화면.
 * 강의가 지정된 경우에만 진입합니다.
 */
fun NavGraphBuilder.lectureSelectGraph(
    navController: NavController,
    onFinish: () -> Unit,
) {
    composable(route = LectureSelectDestination.route) {
        LectureSelectScreen(
            onBackClick = { navController.popBackStack() },
            onLectureSelected = { lectureId, lectureTitle, chapterCount, category ->
                navController.navigate(
                    LectureUploadFileDestination.createRoute(lectureId, lectureTitle, chapterCount, category)
                )
            },
        )
    }
}

/**
 * 자료 추가 화면.
 * - 강의 지정 없이 바로 진입하는 경우
 * - 과목 선택 후 진입하는 경우 (lectureId, lectureTitle, chapterCount 포함)
 */
fun NavGraphBuilder.lectureUploadFileGraph(
    navController: NavController,
    onFinish: () -> Unit,
) {
    // 강의 지정 없이 바로 진입
    composable(route = LectureUploadFileDestination.route) {
        UploadScreen(
            onBackClick = { navController.popBackStack() },
            onNextClick = onFinish,
        )
    }

    // 과목 선택 후 진입
    composable(
        route = LectureUploadFileDestination.routeWithArgs,
        arguments = listOf(
            navArgument(LectureUploadFileDestination.ARG_LECTURE_ID) {
                type = NavType.StringType
            },
            navArgument(LectureUploadFileDestination.ARG_LECTURE_TITLE) {
                type = NavType.StringType
            },
            navArgument(LectureUploadFileDestination.ARG_CHAPTER_COUNT) {
                type = NavType.IntType
            },
            navArgument(LectureUploadFileDestination.ARG_CATEGORY) {
                type = NavType.StringType
            },
        ),
    ) { backStack ->
        val args = backStack.arguments
        val category = args?.getString(LectureUploadFileDestination.ARG_CATEGORY)
            ?.takeIf { it != "-" }
        UploadScreen(
            subjectId = args?.getString(LectureUploadFileDestination.ARG_LECTURE_ID),
            chapterTitle = args?.getString(LectureUploadFileDestination.ARG_LECTURE_TITLE),
            lecturePurpose = category,
            chapterCount = args?.getInt(LectureUploadFileDestination.ARG_CHAPTER_COUNT),
            onBackClick = { navController.popBackStack() },
            onNextClick = onFinish,
        )
    }
}