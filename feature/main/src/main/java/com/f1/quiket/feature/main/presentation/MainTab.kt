package com.f1.quiket.feature.main.presentation

import com.f1.quiket.core.navigation.QuiketDestination
import com.f1.quiket.feature.history.navigation.HistoryDestination
import com.f1.quiket.feature.home.navigation.HomeDestination
import com.f1.quiket.feature.mypage.navigation.MyPageDestination
import com.f1.quiket.feature.review.navigation.ReviewDestination

enum class MainTab(
    val destination: QuiketDestination,
    val label: String,
    val iconText: String,
) {
    Home(
        destination = HomeDestination,
        label = "홈",
        iconText = "홈",
    ),
    History(
        destination = HistoryDestination,
        label = "기록",
        iconText = "기록",
    ),
    Review(
        destination = ReviewDestination,
        label = "오답노트",
        iconText = "오답",
    ),
    MyPage(
        destination = MyPageDestination,
        label = "마이",
        iconText = "마이",
    ),
}
