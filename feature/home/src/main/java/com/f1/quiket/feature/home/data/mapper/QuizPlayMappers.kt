package com.f1.quiket.feature.home.data.mapper

import com.f1.quiket.feature.home.data.remote.QuestionAnswerResponse
import com.f1.quiket.feature.home.data.remote.QuestionOptionResponse
import com.f1.quiket.feature.home.data.remote.QuestionResponse
import com.f1.quiket.feature.home.data.remote.QuizPlaySessionDataResponse
import com.f1.quiket.feature.home.data.remote.QuizPlayStartRequest
import com.f1.quiket.feature.home.data.remote.QuizSessionDataResponse
import com.f1.quiket.feature.home.domain.model.Question
import com.f1.quiket.feature.home.domain.model.QuestionAnswer
import com.f1.quiket.feature.home.domain.model.QuestionOption
import com.f1.quiket.feature.home.domain.model.QuizDifficulty
import com.f1.quiket.feature.home.domain.model.QuizGenerationStatus
import com.f1.quiket.feature.home.domain.model.QuizPlayMode
import com.f1.quiket.feature.home.domain.model.QuizPlaySession
import com.f1.quiket.feature.home.domain.model.QuizPlaySessionStatus
import com.f1.quiket.feature.home.domain.model.QuizPlayStart
import com.f1.quiket.feature.home.domain.model.QuizPlayType
import com.f1.quiket.feature.home.domain.model.QuizSession
import com.f1.quiket.feature.home.domain.model.ServerQuizType

fun QuizSessionDataResponse.toDomain(): QuizSession = QuizSession(
    id = id,
    subjectId = subjectId,
    subjectName = subjectName,
    quizType = quizType.toServerQuizType(),
    choiceCount = choiceCount,
    questionCount = questionCount,
    playMode = playMode.toQuizPlayMode(),
    timerEnabled = timerEnabled,
    timerScope = timerScope,
    timerSeconds = timerSeconds,
    difficulty = difficulty.toQuizDifficulty(),
    status = status.toQuizGenerationStatus(),
    questions = questions.map { question -> question.toDomain() },
)

fun QuestionResponse.toDomain(): Question = Question(
    id = id,
    subjectId = subjectId,
    chapterId = chapterId,
    partId = partId,
    partName = partName,
    questionType = questionType.toServerQuizType(),
    difficulty = difficulty.toQuizDifficulty(),
    summary = summary,
    body = body,
    correctExplanation = correctExplanation,
    incorrectExplanation = incorrectExplanation,
    displayOrder = displayOrder,
    options = options.map { option -> option.toDomain() },
    answer = answer.toDomain(),
)

fun QuestionOptionResponse.toDomain(): QuestionOption = QuestionOption(
    id = id,
    optionNumber = optionNumber,
    content = content,
)

fun QuestionAnswerResponse.toDomain(): QuestionAnswer = QuestionAnswer(
    answerValue = answerValue,
)

fun QuizPlayStart.toRequest(): QuizPlayStartRequest = QuizPlayStartRequest(
    clientSessionId = clientSessionId,
    playType = playType.wireValue,
    parentPlaySessionId = parentPlaySessionId,
    questionShuffled = questionShuffled,
    optionShuffled = optionShuffled,
    shuffleSeed = shuffleSeed,
)

fun QuizPlaySessionDataResponse.toDomain(): QuizPlaySession = QuizPlaySession(
    playSessionId = playSessionId,
    clientSessionId = clientSessionId,
    quizSessionId = quizSessionId,
    playType = playType.toQuizPlayType(),
    status = status.toQuizPlaySessionStatus(),
    quizSession = quizSession?.toDomain(),
)

internal fun String.toServerQuizType(): ServerQuizType = when (this) {
    ServerQuizType.MultipleChoice.wireValue -> ServerQuizType.MultipleChoice
    ServerQuizType.Ox.wireValue -> ServerQuizType.Ox
    else -> ServerQuizType.MultipleChoice
}

internal fun String.toQuizPlayMode(): QuizPlayMode = when (this) {
    QuizPlayMode.AllAtOnce.wireValue -> QuizPlayMode.AllAtOnce
    QuizPlayMode.OneByOne.wireValue -> QuizPlayMode.OneByOne
    else -> QuizPlayMode.AllAtOnce
}

internal fun String.toQuizDifficulty(): QuizDifficulty = when (this) {
    QuizDifficulty.Easy.wireValue -> QuizDifficulty.Easy
    QuizDifficulty.Medium.wireValue -> QuizDifficulty.Medium
    QuizDifficulty.Hard.wireValue -> QuizDifficulty.Hard
    else -> QuizDifficulty.Medium
}

internal fun String.toQuizGenerationStatus(): QuizGenerationStatus =
    QuizGenerationStatus.entries.firstOrNull { status -> status.wireValue == this }
        ?: QuizGenerationStatus.Unknown

internal fun String.toQuizPlayType(): QuizPlayType =
    QuizPlayType.entries.firstOrNull { type -> type.wireValue == this }
        ?: QuizPlayType.Unknown

private fun String.toQuizPlaySessionStatus(): QuizPlaySessionStatus =
    QuizPlaySessionStatus.entries.firstOrNull { status -> status.wireValue == this }
        ?: QuizPlaySessionStatus.Unknown
