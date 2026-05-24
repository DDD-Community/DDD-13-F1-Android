package com.f1.quiket.feature.home.domain.model

data class QuizSession(
    val id: String,
    val subjectId: String,
    val subjectName: String?,
    val quizType: ServerQuizType,
    val choiceCount: Int?,
    val questionCount: Int,
    val playMode: QuizPlayMode,
    val timerEnabled: Boolean,
    val timerScope: String?,
    val timerSeconds: Int?,
    val difficulty: QuizDifficulty,
    val status: QuizGenerationStatus,
    val questions: List<Question>,
)

data class Question(
    val id: String,
    val subjectId: String?,
    val chapterId: String?,
    val partId: String?,
    val partName: String?,
    val questionType: ServerQuizType,
    val difficulty: QuizDifficulty,
    val summary: String?,
    val body: String,
    val correctExplanation: String?,
    val incorrectExplanation: String?,
    val displayOrder: Int,
    val options: List<QuestionOption>,
    val answer: QuestionAnswer,
)

data class QuestionOption(
    val id: String,
    val optionNumber: Int,
    val content: String,
)

data class QuestionAnswer(
    val answerValue: String,
)

data class QuizPlayStart(
    val clientSessionId: String,
    val playType: QuizPlayType,
    val parentPlaySessionId: String? = null,
    val questionShuffled: Boolean = false,
    val optionShuffled: Boolean = true,
    val shuffleSeed: String? = null,
)

data class QuizPlaySession(
    val playSessionId: String,
    val clientSessionId: String,
    val quizSessionId: String,
    val playType: QuizPlayType,
    val status: QuizPlaySessionStatus,
    val quizSession: QuizSession?,
)

enum class QuizPlayType(
    val wireValue: String,
) {
    First("first"),
    RetryAll("retry_all"),
    RetryWrong("retry_wrong"),
    Unknown("unknown"),
}

enum class QuizPlaySessionStatus(
    val wireValue: String,
) {
    InProgress("in_progress"),
    Submitted("submitted"),
    Abandoned("abandoned"),
    Unknown("unknown"),
}
