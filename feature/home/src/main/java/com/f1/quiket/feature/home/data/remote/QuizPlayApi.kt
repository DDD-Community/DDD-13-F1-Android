package com.f1.quiket.feature.home.data.remote

import com.f1.quiket.core.network.auth.AuthenticatedRequest
import com.f1.quiket.core.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface QuizPlayApi {
    @Headers(AuthenticatedRequest.HEADER)
    @GET("quiz-sessions/{quizSessionId}")
    suspend fun getQuizSession(
        @Path("quizSessionId") quizSessionId: String,
    ): Response<ApiResponse<QuizSessionDataResponse>>

    @Headers(AuthenticatedRequest.HEADER)
    @POST("quiz-sessions/{quizSessionId}/play-sessions")
    suspend fun startQuizPlaySession(
        @Path("quizSessionId") quizSessionId: String,
        @Body request: QuizPlayStartRequest,
    ): Response<ApiResponse<QuizPlaySessionDataResponse>>
}
