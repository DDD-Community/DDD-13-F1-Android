package com.f1.quiket.feature.home.presentation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class QuizPlayAllViewModelTest {
    @Test
    fun selectOption_updatesCurrentQuestionAnswer() {
        val viewModel = QuizPlayAllViewModel()
        val question = viewModel.state.value.currentQuestion!!
        val option = question.options[1]

        viewModel.onIntent(QuizPlayAllIntent.SelectOption(option.id))

        assertThat(viewModel.state.value.selectedOptionIds[question.id]).isEqualTo(option.id)
    }

    @Test
    fun movePreviousAndNext_clampsAtBounds() {
        val viewModel = QuizPlayAllViewModel()

        viewModel.onIntent(QuizPlayAllIntent.MovePrevious)

        assertThat(viewModel.state.value.currentQuestionIndex).isEqualTo(0)

        repeat(20) {
            viewModel.onIntent(QuizPlayAllIntent.MoveNext)
        }

        assertThat(viewModel.state.value.currentQuestionIndex)
            .isEqualTo(viewModel.state.value.questions.lastIndex)

        viewModel.onIntent(QuizPlayAllIntent.MoveNext)

        assertThat(viewModel.state.value.currentQuestionIndex)
            .isEqualTo(viewModel.state.value.questions.lastIndex)
    }

    @Test
    fun toggleBookmark_addsAndRemovesCurrentQuestion() {
        val viewModel = QuizPlayAllViewModel()
        val questionId = viewModel.state.value.currentQuestion!!.id

        viewModel.onIntent(QuizPlayAllIntent.ToggleBookmark)

        assertThat(viewModel.state.value.bookmarkedQuestionIds).contains(questionId)

        viewModel.onIntent(QuizPlayAllIntent.ToggleBookmark)

        assertThat(viewModel.state.value.bookmarkedQuestionIds).doesNotContain(questionId)
    }

    @Test
    fun selectQuestion_movesCurrentIndexAndClosesQuestionList() {
        val viewModel = QuizPlayAllViewModel()

        viewModel.onIntent(QuizPlayAllIntent.OpenQuestionList)
        viewModel.onIntent(QuizPlayAllIntent.SelectQuestion(4))

        assertThat(viewModel.state.value.currentQuestionIndex).isEqualTo(4)
        assertThat(viewModel.state.value.isQuestionListVisible).isFalse()
    }

    @Test
    fun submitConfirmState_calculatesUnsolvedAndBookmarkedQuestions() {
        val viewModel = QuizPlayAllViewModel()
        val firstQuestion = viewModel.state.value.currentQuestion!!
        val firstOption = firstQuestion.options.first()

        viewModel.onIntent(QuizPlayAllIntent.SelectOption(firstOption.id))
        viewModel.onIntent(QuizPlayAllIntent.SelectQuestion(1))
        viewModel.onIntent(QuizPlayAllIntent.ToggleBookmark)
        viewModel.onIntent(QuizPlayAllIntent.SelectQuestion(3))
        viewModel.onIntent(QuizPlayAllIntent.ToggleBookmark)
        viewModel.onIntent(QuizPlayAllIntent.OpenSubmitConfirm)

        val state = viewModel.state.value

        assertThat(state.isSubmitConfirmVisible).isTrue()
        assertThat(state.unsolvedQuestions.map { it.number }).doesNotContain(1)
        assertThat(state.unsolvedQuestions).hasSize(9)
        assertThat(state.bookmarkedQuestions.map { it.number }).containsExactly(2, 4).inOrder()
    }
}
