package com.f1.quiket.feature.home.presentation

import com.f1.quiket.core.common.mvi.UiEffect
import com.f1.quiket.core.common.mvi.UiIntent
import com.f1.quiket.core.common.mvi.UiState
import com.f1.quiket.feature.home.domain.model.QuizResult

data class QuizResultState(
    val isLoading: Boolean = false,
    val result: QuizResult? = null,
    val errorMessage: String? = null,
) : UiState

sealed interface QuizResultIntent : UiIntent {
    data class Load(val playSessionId: String) : QuizResultIntent
}

sealed interface QuizResultEffect : UiEffect
