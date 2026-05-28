package com.f1.quiket.feature.history.domain.repository

import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.feature.history.domain.model.QuizPlaySession
import com.f1.quiket.feature.history.domain.model.QuizResult
import com.f1.quiket.feature.history.domain.model.QuizResultSubmit
import com.f1.quiket.feature.history.domain.model.QuizRetry
import com.f1.quiket.feature.history.domain.model.RecentActivityPage

interface HistoryRepository {
    suspend fun getRecentActivities(
        page: Int = 0,
        size: Int = 20,
    ): NetworkResult<RecentActivityPage>

    suspend fun submitQuizResult(request: QuizResultSubmit): NetworkResult<QuizResult>

    suspend fun getQuizResult(playSessionId: String): NetworkResult<QuizResult>

    suspend fun retryAllQuestions(
        playSessionId: String,
        request: QuizRetry,
    ): NetworkResult<QuizPlaySession>

    suspend fun retryWrongQuestions(
        playSessionId: String,
        request: QuizRetry,
    ): NetworkResult<QuizPlaySession>
}
