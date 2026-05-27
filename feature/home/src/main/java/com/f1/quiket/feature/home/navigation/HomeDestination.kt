package com.f1.quiket.feature.home.navigation

import com.f1.quiket.core.navigation.QuiketDestination
import com.f1.quiket.feature.home.domain.model.QuizPlayMode
import com.f1.quiket.feature.home.domain.model.QuizTimerScope

data object HomeDestination : QuiketDestination {
    override val route: String = "main/home"
}

data object QuizStartDestination : QuiketDestination {
    const val ARG_QUIZ_SESSION_ID = "quizSessionId"
    const val BASE_ROUTE = "home/quiz-start"
    override val route: String = "$BASE_ROUTE?$ARG_QUIZ_SESSION_ID={$ARG_QUIZ_SESSION_ID}"

    fun createRoute(quizSessionId: String?): String =
        if (quizSessionId.isNullOrBlank()) {
            BASE_ROUTE
        } else {
            "$BASE_ROUTE?$ARG_QUIZ_SESSION_ID=$quizSessionId"
        }
}

data object QuizPlayAllDestination : QuiketDestination {
    const val ARG_QUIZ_SESSION_ID = "quizSessionId"
    const val ARG_PLAY_MODE = "playMode"
    const val ARG_TIMER_ENABLED = "timerEnabled"
    const val ARG_TIMER_SCOPE = "timerScope"
    const val ARG_TIMER_SECONDS = "timerSeconds"
    const val BASE_ROUTE = "home/quiz-play/all"
    override val route: String = "$BASE_ROUTE?" +
        "$ARG_QUIZ_SESSION_ID={$ARG_QUIZ_SESSION_ID}&" +
        "$ARG_PLAY_MODE={$ARG_PLAY_MODE}&" +
        "$ARG_TIMER_ENABLED={$ARG_TIMER_ENABLED}&" +
        "$ARG_TIMER_SCOPE={$ARG_TIMER_SCOPE}&" +
        "$ARG_TIMER_SECONDS={$ARG_TIMER_SECONDS}"

    fun createRoute(
        quizSessionId: String?,
        playMode: QuizPlayMode = QuizPlayMode.AllAtOnce,
        timerEnabled: Boolean = false,
        timerScope: QuizTimerScope? = null,
        timerSeconds: Int? = null,
    ): String = BASE_ROUTE +
        "?$ARG_QUIZ_SESSION_ID=${quizSessionId.orEmpty()}" +
        "&$ARG_PLAY_MODE=${playMode.wireValue}" +
        "&$ARG_TIMER_ENABLED=$timerEnabled" +
        "&$ARG_TIMER_SCOPE=${timerScope?.wireValue.orEmpty()}" +
        "&$ARG_TIMER_SECONDS=${timerSeconds ?: -1}"
}

data object QuizResultDestination : QuiketDestination {
    const val ARG_PLAY_SESSION_ID = "playSessionId"
    const val BASE_ROUTE = "home/quiz-result"
    override val route: String = "$BASE_ROUTE/{$ARG_PLAY_SESSION_ID}"

    fun createRoute(playSessionId: String): String = "$BASE_ROUTE/$playSessionId"
}
