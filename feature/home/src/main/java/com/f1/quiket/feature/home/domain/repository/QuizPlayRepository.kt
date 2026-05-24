package com.f1.quiket.feature.home.domain.repository

import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.feature.home.domain.model.QuizPlaySession
import com.f1.quiket.feature.home.domain.model.QuizPlayStart
import com.f1.quiket.feature.home.domain.model.QuizSession

interface QuizPlayRepository {
    suspend fun getQuizSession(quizSessionId: String): NetworkResult<QuizSession>

    suspend fun startQuizPlaySession(
        quizSessionId: String,
        request: QuizPlayStart,
    ): NetworkResult<QuizPlaySession>
}
