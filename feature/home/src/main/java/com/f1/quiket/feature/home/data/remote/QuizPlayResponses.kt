package com.f1.quiket.feature.home.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class QuizSessionDataResponse(
    val id: String,
    val subjectId: String,
    val subjectName: String? = null,
    val quizType: String,
    val choiceCount: Int? = null,
    val questionCount: Int,
    val playMode: String,
    val timerEnabled: Boolean = false,
    val timerScope: String? = null,
    val timerSeconds: Int? = null,
    val difficulty: String,
    val status: String,
    val questions: List<QuestionResponse> = emptyList(),
)

@Serializable
data class QuestionResponse(
    val id: String,
    val subjectId: String? = null,
    val chapterId: String? = null,
    val partId: String? = null,
    val partName: String? = null,
    val questionType: String,
    val difficulty: String,
    val summary: String? = null,
    val body: String,
    val correctExplanation: String? = null,
    val incorrectExplanation: String? = null,
    val displayOrder: Int,
    val options: List<QuestionOptionResponse> = emptyList(),
    val answer: QuestionAnswerResponse,
)

@Serializable
data class QuestionOptionResponse(
    val id: String,
    val optionNumber: Int,
    val content: String,
)

@Serializable
data class QuestionAnswerResponse(
    val answerValue: String,
)

@Serializable
data class QuizPlayStartRequest(
    val clientSessionId: String,
    val playType: String,
    val parentPlaySessionId: String? = null,
    val questionShuffled: Boolean = false,
    val optionShuffled: Boolean = true,
    val shuffleSeed: String? = null,
)

@Serializable
data class QuizPlaySessionDataResponse(
    val playSessionId: String,
    val clientSessionId: String,
    val quizSessionId: String,
    val playType: String,
    val status: String,
    val quizSession: QuizSessionDataResponse? = null,
)
