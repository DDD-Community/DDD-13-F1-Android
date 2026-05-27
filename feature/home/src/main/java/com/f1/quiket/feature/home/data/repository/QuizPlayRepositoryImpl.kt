package com.f1.quiket.feature.home.data.repository

import com.f1.quiket.core.common.coroutines.AppDispatchers
import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.core.network.retrofit.ApiResponseHandler
import com.f1.quiket.feature.home.data.mapper.toDomain
import com.f1.quiket.feature.home.data.mapper.toRequest
import com.f1.quiket.feature.home.data.remote.QuizPlayApi
import com.f1.quiket.feature.home.domain.model.QuizPlaySession
import com.f1.quiket.feature.home.domain.model.QuizPlayStart
import com.f1.quiket.feature.home.domain.model.QuizResult
import com.f1.quiket.feature.home.domain.model.QuizResultSubmit
import com.f1.quiket.feature.home.domain.model.QuizSession
import com.f1.quiket.feature.home.domain.repository.QuizPlayRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.withContext

@Singleton
class QuizPlayRepositoryImpl @Inject constructor(
    private val api: QuizPlayApi,
    private val responseHandler: ApiResponseHandler,
    private val dispatchers: AppDispatchers,
) : QuizPlayRepository {
    override suspend fun getQuizSession(
        quizSessionId: String,
    ): NetworkResult<QuizSession> = withContext(dispatchers.io) {
        responseHandler.execute(
            call = { api.getQuizSession(quizSessionId = quizSessionId) },
            mapper = { response -> response.toDomain() },
        )
    }

    override suspend fun startQuizPlaySession(
        quizSessionId: String,
        request: QuizPlayStart,
    ): NetworkResult<QuizPlaySession> = withContext(dispatchers.io) {
        responseHandler.execute(
            call = { api.startQuizPlaySession(quizSessionId, request.toRequest()) },
            mapper = { response -> response.toDomain() },
        )
    }

    override suspend fun submitQuizResult(
        request: QuizResultSubmit,
    ): NetworkResult<QuizResult> = withContext(dispatchers.io) {
        responseHandler.execute(
            call = { api.submitQuizResult(request.toRequest()) },
            mapper = { response -> response.toDomain() },
        )
    }

    override suspend fun getQuizResult(
        playSessionId: String,
    ): NetworkResult<QuizResult> = withContext(dispatchers.io) {
        responseHandler.execute(
            call = { api.getQuizResult(playSessionId = playSessionId) },
            mapper = { response -> response.toDomain() },
        )
    }
}
