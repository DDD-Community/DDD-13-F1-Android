package com.f1.quiket.feature.home.presentation

import com.f1.quiket.core.common.mvi.UiEffect
import com.f1.quiket.core.common.mvi.UiIntent
import com.f1.quiket.core.common.mvi.UiState

data class QuizPlayAllState(
    val questions: List<QuizPlayAllQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionIds: Map<String, String> = emptyMap(),
    val bookmarkedQuestionIds: Set<String> = emptySet(),
    val isQuestionListVisible: Boolean = false,
    val isSubmitConfirmVisible: Boolean = false,
    val showTutorial: Boolean = true,
) : UiState {
    val currentQuestion: QuizPlayAllQuestion?
        get() = questions.getOrNull(currentQuestionIndex)

    val totalQuestionCount: Int
        get() = questions.size

    val currentQuestionNumber: Int
        get() = currentQuestionIndex + 1

    val solvedQuestionCount: Int
        get() = questions.count { selectedOptionIds.containsKey(it.id) }

    val unsolvedQuestionCount: Int
        get() = totalQuestionCount - solvedQuestionCount

    val bookmarkedQuestionCount: Int
        get() = questions.count { bookmarkedQuestionIds.contains(it.id) }

    val progressFraction: Float
        get() = if (totalQuestionCount == 0) {
            0f
        } else {
            currentQuestionNumber.toFloat() / totalQuestionCount.toFloat()
        }

    val canMovePrevious: Boolean
        get() = currentQuestionIndex > 0

    val canMoveNext: Boolean
        get() = currentQuestionIndex < questions.lastIndex

    val isLastQuestion: Boolean
        get() = questions.isNotEmpty() && currentQuestionIndex == questions.lastIndex

    val selectedOptionId: String?
        get() = currentQuestion?.let { selectedOptionIds[it.id] }

    val isCurrentQuestionBookmarked: Boolean
        get() = currentQuestion?.id?.let(bookmarkedQuestionIds::contains) == true

    val unsolvedQuestions: List<QuizPlayAllQuestion>
        get() = questions.filterNot { selectedOptionIds.containsKey(it.id) }

    val bookmarkedQuestions: List<QuizPlayAllQuestion>
        get() = questions.filter { bookmarkedQuestionIds.contains(it.id) }
}

data class QuizPlayAllQuestion(
    val id: String,
    val number: Int,
    val body: String,
    val timerText: String,
    val options: List<QuizPlayAllOption>,
)

data class QuizPlayAllOption(
    val id: String,
    val number: Int,
    val text: String,
)

sealed interface QuizPlayAllIntent : UiIntent {
    data class SelectOption(val optionId: String) : QuizPlayAllIntent
    data object MovePrevious : QuizPlayAllIntent
    data object MoveNext : QuizPlayAllIntent
    data object ToggleBookmark : QuizPlayAllIntent
    data object OpenQuestionList : QuizPlayAllIntent
    data object CloseQuestionList : QuizPlayAllIntent
    data class SelectQuestion(val questionIndex: Int) : QuizPlayAllIntent
    data object OpenSubmitConfirm : QuizPlayAllIntent
    data object CloseSubmitConfirm : QuizPlayAllIntent
    data object Submit : QuizPlayAllIntent
    data object DismissTutorial : QuizPlayAllIntent
}

sealed interface QuizPlayAllEffect : UiEffect
