package com.f1.quiket.feature.home.presentation

import com.f1.quiket.core.common.mvi.UiEffect
import com.f1.quiket.core.common.mvi.UiIntent
import com.f1.quiket.core.common.mvi.UiState

data class CreateQuizState(
    val subjects: List<QuizSubjectUiModel> = emptyList(),
    val isLoadingSubjects: Boolean = false,
    val loadingScopeSubjectId: String? = null,
    val isCreatingQuiz: Boolean = false,
    val generationProgress: Float = 0f,
    val rewardCount: Int = 10,
) : UiState

sealed interface CreateQuizIntent : UiIntent {
    data object LoadSubjects : CreateQuizIntent
    data class LoadQuizScope(val subjectId: String) : CreateQuizIntent
}

sealed interface CreateQuizEffect : UiEffect {
    data class ShowMessage(val message: String) : CreateQuizEffect
    data object QuizGenerationFinished : CreateQuizEffect
    data class QuizCreated(val quizSessionId: String) : CreateQuizEffect
}
