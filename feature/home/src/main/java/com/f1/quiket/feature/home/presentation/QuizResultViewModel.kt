package com.f1.quiket.feature.home.presentation

import com.f1.quiket.core.common.mvi.MviViewModel
import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.feature.home.domain.repository.QuizPlayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val quizPlayRepository: QuizPlayRepository,
) : MviViewModel<QuizResultState, QuizResultIntent, QuizResultEffect>(
    initialState = QuizResultState(),
) {
    override fun handleIntent(intent: QuizResultIntent) {
        when (intent) {
            is QuizResultIntent.Load -> load(intent.playSessionId)
        }
    }

    private fun load(playSessionId: String) {
        if (playSessionId.isBlank()) {
            updateState { copy(errorMessage = "결과를 불러올 수 없어요.") }
            return
        }

        launch {
            updateState {
                copy(
                    isLoading = true,
                    errorMessage = null,
                )
            }
            when (val result = quizPlayRepository.getQuizResult(playSessionId)) {
                is NetworkResult.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            result = result.data,
                        )
                    }
                }



                is NetworkResult.Failure -> {
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = result.message,
                        )
                    }
                }
            }
        }
    }
}
