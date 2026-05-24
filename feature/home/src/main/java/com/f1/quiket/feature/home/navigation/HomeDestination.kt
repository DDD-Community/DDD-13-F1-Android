package com.f1.quiket.feature.home.navigation

import com.f1.quiket.core.navigation.QuiketDestination

data object HomeDestination : QuiketDestination {
    override val route: String = "main/home"
}

data object QuizStartDestination : QuiketDestination {
    override val route: String = "home/quiz-start"
}
