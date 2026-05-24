package com.f1.quiket.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f1.quiket.feature.home.model.FabAction

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToScheduleExam: () -> Unit,
    navigateToCreateQuiz: () -> Unit,
    navigateToUpload: () -> Unit,
    navigateToAddSubject: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onBoardingDone = {
            viewModel.dispatch(
                HomeIntent.OnboardingDoneClick
            )
        },
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