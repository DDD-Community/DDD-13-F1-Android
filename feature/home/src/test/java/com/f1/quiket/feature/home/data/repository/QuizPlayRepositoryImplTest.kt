package com.f1.quiket.feature.home.data.repository

import com.f1.quiket.core.common.coroutines.AppDispatchers
import com.f1.quiket.core.network.model.ApiResponse
import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.core.network.retrofit.ApiResponseHandler
import com.f1.quiket.core.network.retrofit.NetworkErrorMapper
import com.f1.quiket.feature.home.data.remote.QuestionAnswerResponse
import com.f1.quiket.feature.home.data.remote.QuestionOptionResponse
import com.f1.quiket.feature.home.data.remote.QuestionResponse
import com.f1.quiket.feature.home.data.remote.QuizPlayApi
import com.f1.quiket.feature.home.data.remote.QuizPlaySessionDataResponse
import com.f1.quiket.feature.home.data.remote.QuizPlayStartRequest
import com.f1.quiket.feature.home.data.remote.QuizSessionDataResponse
import com.f1.quiket.feature.home.domain.model.QuizPlayMode
import com.f1.quiket.feature.home.domain.model.QuizPlaySessionStatus
import com.f1.quiket.feature.home.domain.model.QuizPlayStart
import com.f1.quiket.feature.home.domain.model.QuizPlayType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class QuizPlayRepositoryImplTest {
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Test
    fun getQuizSession_success_mapsQuestionsAndPlayMode() = runTest {
        val api = FakeQuizPlayApi()
        val repository = repository(api)

        api.getQuizSessionHandler = { quizSessionId ->
            assertThat(quizSessionId).isEqualTo("session-1")
            successResponse(
                code = "QUIZ_SESSION_SUCCESS",
                data = quizSessionResponse(playMode = "one_by_one"),
            )
        }

        val result = repository.getQuizSession("session-1")

        val quizSession = (result as NetworkResult.Success).data
        assertThat(quizSession.playMode).isEqualTo(QuizPlayMode.OneByOne)
        assertThat(quizSession.questions.single().options.single().content).isEqualTo("정답")
    }

    @Test
    fun startQuizPlaySession_success_mapsRequestBody() = runTest {
        val api = FakeQuizPlayApi()
        val repository = repository(api)

        api.startQuizPlaySessionHandler = { quizSessionId, request ->
            assertThat(quizSessionId).isEqualTo("session-1")
            assertThat(request).isEqualTo(
                QuizPlayStartRequest(
                    clientSessionId = "client-1",
                    playType = "first",
                    questionShuffled = true,
                    optionShuffled = false,
                    shuffleSeed = "seed-1",
                ),
            )
            successResponse(
                code = "QUIZ_PLAY_SESSION_CREATED",
                data = QuizPlaySessionDataResponse(
                    playSessionId = "play-1",
                    clientSessionId = "client-1",
                    quizSessionId = "session-1",
                    playType = "first",
                    status = "in_progress",
                ),
            )
        }

        val result = repository.startQuizPlaySession(
            quizSessionId = "session-1",
            request = QuizPlayStart(
                clientSessionId = "client-1",
                playType = QuizPlayType.First,
                questionShuffled = true,
                optionShuffled = false,
                shuffleSeed = "seed-1",
            ),
        )

        val playSession = (result as NetworkResult.Success).data
        assertThat(playSession.playSessionId).isEqualTo("play-1")
        assertThat(playSession.status).isEqualTo(QuizPlaySessionStatus.InProgress)
    }

    private fun repository(api: QuizPlayApi): QuizPlayRepositoryImpl =
        QuizPlayRepositoryImpl(
            api = api,
            responseHandler = ApiResponseHandler(NetworkErrorMapper(json)),
            dispatchers = AppDispatchers(
                io = dispatcher,
                main = dispatcher,
                default = dispatcher,
            ),
        )

    private class FakeQuizPlayApi : QuizPlayApi {
        var getQuizSessionHandler:
            suspend (String) -> Response<ApiResponse<QuizSessionDataResponse>> =
            { unhandled("getQuizSession") }
        var startQuizPlaySessionHandler:
            suspend (String, QuizPlayStartRequest) -> Response<ApiResponse<QuizPlaySessionDataResponse>> =
            { _, _ -> unhandled("startQuizPlaySession") }

        override suspend fun getQuizSession(
            quizSessionId: String,
        ): Response<ApiResponse<QuizSessionDataResponse>> = getQuizSessionHandler(quizSessionId)

        override suspend fun startQuizPlaySession(
            quizSessionId: String,
            request: QuizPlayStartRequest,
        ): Response<ApiResponse<QuizPlaySessionDataResponse>> =
            startQuizPlaySessionHandler(quizSessionId, request)

        private fun <T> unhandled(method: String): T {
            error("Unhandled QuizPlayApi call: $method")
        }
    }

    private companion object {
        val json = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }

        fun quizSessionResponse(playMode: String): QuizSessionDataResponse = QuizSessionDataResponse(
            id = "session-1",
            subjectId = "subject-1",
            subjectName = "SQLD",
            quizType = "multiple_choice",
            choiceCount = 4,
            questionCount = 1,
            playMode = playMode,
            difficulty = "easy",
            status = "completed",
            questions = listOf(
                QuestionResponse(
                    id = "question-1",
                    subjectId = "subject-1",
                    partId = "part-1",
                    partName = "SQLD 개요",
                    questionType = "multiple_choice",
                    difficulty = "easy",
                    body = "정답은?",
                    displayOrder = 1,
                    options = listOf(
                        QuestionOptionResponse(
                            id = "option-1",
                            optionNumber = 1,
                            content = "정답",
                        ),
                    ),
                    answer = QuestionAnswerResponse(answerValue = "option-1"),
                ),
            ),
        )

        fun <T : Any> successResponse(
            code: String,
            data: T,
        ): Response<ApiResponse<T>> = Response.success(
            ApiResponse(
                success = true,
                code = code,
                message = "success",
                data = data,
            ),
        )
    }
}
