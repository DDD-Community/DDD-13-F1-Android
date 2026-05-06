package com.f1.quiket.feature.home.presentation

import androidx.compose.runtime.Composable
import com.f1.quiket.feature.home.model.FabAction

@Composable
fun HomeRoute(
    navigateToScheduleExam: () -> Unit,
    navigateToCreateQuiz: () -> Unit,
    navigateToUpload: () -> Unit,
    navigateToAddSubject: () -> Unit
) {
    HomeScreen(
        onFabItemClick = { action ->
            when (action) {
                FabAction.ScheduleExam -> navigateToScheduleExam()
                FabAction.CreateQuiz -> navigateToCreateQuiz()
                FabAction.Upload -> navigateToUpload()
                FabAction.AddSubject -> navigateToAddSubject()
            }
        }
    )
}