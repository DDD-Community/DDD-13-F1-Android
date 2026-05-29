package com.f1.quiket.feature.home.presentation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.f1.quiket.feature.home.model.FabAction

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    isQuizGenerating: Boolean,
    activeQuizSessionId: String?,
    navigateToQuizStart: (String?) -> Unit,
    navigateToQuizResult: (String) -> Unit,
    navigateToScheduleExam: () -> Unit,
    navigateToCreateQuiz: () -> Unit,
    navigateToUpload: () -> Unit,
    navigateToAddSubject: () -> Unit,
    navigateToMyPage: () -> Unit,
    homeBackStackEntry: NavBackStackEntry? = null,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val subjectCreatedFlow = remember(homeBackStackEntry) {
        homeBackStackEntry?.savedStateHandle?.getStateFlow("subject_created", false)
            ?: MutableStateFlow(false)
    }
    val subjectCreated by subjectCreatedFlow.collectAsStateWithLifecycle()

    LaunchedEffect(subjectCreated) {
        if (subjectCreated) {
            homeBackStackEntry?.savedStateHandle?.set("subject_created", false)
            viewModel.onIntent(HomeIntent.LoadHomeData)
        }
    }
    val serverActiveQuizSessionId = uiState.homeData
        ?.hero
        ?.takeIf { hero -> hero.hasActiveQuiz }
        ?.activeQuiz
        ?.quizSessionId
    val effectiveActiveQuizSessionId = serverActiveQuizSessionId ?: activeQuizSessionId
    val isWaitingQuizGeneration = isQuizGenerating && effectiveActiveQuizSessionId == null

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        isQuizGenerating = isWaitingQuizGeneration,
        hasActiveQuizSession = effectiveActiveQuizSessionId != null,
        onBoardingDone = {
            viewModel.onIntent(
                HomeIntent.OnboardingDoneClick
            )
        },
        onQuizCardClick = {
            effectiveActiveQuizSessionId?.let(navigateToQuizStart)
        },
        onQuizActionClick = {
            when {
                effectiveActiveQuizSessionId != null -> navigateToQuizStart(effectiveActiveQuizSessionId)
                isWaitingQuizGeneration -> Unit
                else -> navigateToCreateQuiz()
            }
        },
        onQuizResultClick = navigateToQuizResult,
        onHomeRefresh = { viewModel.onIntent(HomeIntent.LoadHomeData) },
        onFabItemClick = { action ->
            when (action) {
                FabAction.ScheduleExam -> navigateToScheduleExam()
                FabAction.CreateQuiz -> navigateToCreateQuiz()
                FabAction.Upload -> navigateToUpload()
                FabAction.AddSubject -> navigateToAddSubject()
            }
        },
        onProfileClick = navigateToMyPage,
    )
}
