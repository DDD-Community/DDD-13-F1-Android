package com.f1.quiket.feature.home.presentation

import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.core.testing.MainDispatcherRule
import com.f1.quiket.feature.home.domain.model.QuizPlayMode
import com.f1.quiket.feature.home.domain.model.QuizPlaySession
import com.f1.quiket.feature.home.domain.model.QuizPlaySessionStatus
import com.f1.quiket.feature.home.domain.model.QuizPlayStart
import com.f1.quiket.feature.home.domain.model.QuizPlayType
import com.f1.quiket.feature.home.domain.model.QuizRetry
import com.f1.quiket.feature.home.domain.model.QuizResult
import com.f1.quiket.feature.home.domain.model.QuizResultSubmit
import com.f1.quiket.feature.home.domain.model.QuizSession
import com.f1.quiket.feature.home.domain.model.RetryAvailable
import com.f1.quiket.feature.home.domain.model.RewardSummary
import com.f1.quiket.feature.home.domain.repository.QuizPlayRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizResultViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun retryAll_success_emitsNavigateToRetry() = runTest {
        val repository = FakeQuizPlayRepository()
        repository.quizResult = NetworkResult.Success(result())
        repository.retryAllResult = NetworkResult.Success(retryPlaySession())
        val viewModel = QuizResultViewModel(repository)

        viewModel.onIntent(QuizResultIntent.Load("result-1"))
        advanceUntilIdle()
        val effect = async { viewModel.effect.first() }

        viewModel.onIntent(QuizResultIntent.RetryAll)
        advanceUntilIdle()

        assertThat(repository.retryAllPlaySessionId).isEqualTo("play-1")
        assertThat(repository.retryAllRequest?.clientSessionId).isNotEmpty()
        assertThat(viewModel.state.value.isRetrying).isFalse()
        assertThat(effect.await()).isEqualTo(
            QuizResultEffect.NavigateToRetry(
                QuizRetryPlayConfig(
                    quizSessionId = "session-retry",
                    clientSessionId = "client-retry",
                    playSessionId = "play-retry",
                    playType = QuizPlayType.RetryAll,
                    playMode = QuizPlayMode.AllAtOnce,
                    timerEnabled = false,
                    timerScope = null,
                    timerSeconds = null,
                ),
            ),
        )
    }

    @Test
    fun retryWrong_withoutWrongQuestions_emitsMessage() = runTest {
        val repository = FakeQuizPlayRepository()
        repository.quizResult = NetworkResult.Success(
            result(
                wrongCount = 0,
                retryAvailable = RetryAvailable(
                    retryAll = true,
                    retryWrong = true,
                    wrongCount = 0,
                ),
            ),
        )
        val viewModel = QuizResultViewModel(repository)

        viewModel.onIntent(QuizResultIntent.Load("result-1"))
        advanceUntilIdle()
        val effect = async { viewModel.effect.first() }

        viewModel.onIntent(QuizResultIntent.RetryWrong)
        advanceUntilIdle()

        assertThat(repository.retryWrongPlaySessionId).isNull()
        assertThat(effect.await()).isEqualTo(QuizResultEffect.ShowMessage("다시 풀 오답이 없어요."))
    }

    private class FakeQuizPlayRepository : QuizPlayRepository {
        var quizResult: NetworkResult<QuizResult> =
            NetworkResult.Failure(code = "TEST", message = "not configured")
        var retryAllResult: NetworkResult<QuizPlaySession> =
            NetworkResult.Failure(code = "TEST", message = "not configured")
        var retryWrongResult: NetworkResult<QuizPlaySession> =
            NetworkResult.Failure(code = "TEST", message = "not configured")
        var retryAllPlaySessionId: String? = null
        var retryWrongPlaySessionId: String? = null
        var retryAllRequest: QuizRetry? = null
        var retryWrongRequest: QuizRetry? = null

        override suspend fun getQuizSession(quizSessionId: String): NetworkResult<QuizSession> =
            NetworkResult.Failure(code = "TEST", message = "not configured")

        override suspend fun startQuizPlaySession(
            quizSessionId: String,
            request: QuizPlayStart,
        ): NetworkResult<QuizPlaySession> =
            NetworkResult.Failure(code = "TEST", message = "not configured")

        override suspend fun submitQuizResult(request: QuizResultSubmit): NetworkResult<QuizResult> =
            NetworkResult.Failure(code = "TEST", message = "not configured")

        override suspend fun getQuizResult(resultId: String): NetworkResult<QuizResult> = quizResult

        override suspend fun retryAllQuestions(
            playSessionId: String,
            request: QuizRetry,
        ): NetworkResult<QuizPlaySession> {
            retryAllPlaySessionId = playSessionId
            retryAllRequest = request
            return retryAllResult
        }

        override suspend fun retryWrongQuestions(
            playSessionId: String,
            request: QuizRetry,
        ): NetworkResult<QuizPlaySession> {
            retryWrongPlaySessionId = playSessionId
            retryWrongRequest = request
            return retryWrongResult
        }
    }
}

private fun result(
    wrongCount: Int = 1,
    retryAvailable: RetryAvailable? = RetryAvailable(
        retryAll = true,
        retryWrong = true,
        wrongCount = 1,
    ),
): QuizResult = QuizResult(
    playSessionId = "play-1",
    resultId = "result-1",
    quizSessionId = "session-1",
    subjectId = "subject-1",
    subjectName = "SQLD",
    totalCount = 2,
    correctCount = 1,
    wrongCount = wrongCount,
    skipCount = 0,
    accuracyPct = 50,
    elapsedMs = 0,
    scoreMatched = true,
    abuseFlagged = false,
    rewards = RewardSummary(
        dotoriEarned = 1,
        xpEarned = 1,
        leveledUp = false,
        newLevel = null,
        currentDotoriBalance = null,
        currentXpTotal = null,
    ),
    reviewItems = emptyList(),
    retryAvailable = retryAvailable,
    createdAt = null,
)

private fun retryPlaySession(): QuizPlaySession = QuizPlaySession(
    playSessionId = "play-retry",
    clientSessionId = "client-retry",
    quizSessionId = "session-retry",
    playType = QuizPlayType.RetryAll,
    status = QuizPlaySessionStatus.InProgress,
    quizSession = QuizSession(
        id = "session-retry",
        subjectId = "subject-1",
        subjectName = "SQLD",
        quizType = com.f1.quiket.feature.home.domain.model.ServerQuizType.MultipleChoice,
        choiceCount = 4,
        questionCount = 1,
        playMode = QuizPlayMode.AllAtOnce,
        timerEnabled = false,
        timerScope = null,
        timerSeconds = null,
        difficulty = com.f1.quiket.feature.home.domain.model.QuizDifficulty.Medium,
        status = com.f1.quiket.feature.home.domain.model.QuizGenerationStatus.Completed,
        questions = emptyList(),
    ),
)
