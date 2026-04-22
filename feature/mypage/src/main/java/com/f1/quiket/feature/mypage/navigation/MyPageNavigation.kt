package com.f1.quiket.feature.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.f1.quiket.feature.mypage.presentation.MyPageRoute

fun NavGraphBuilder.myPageGraph() {
    composable(route = MyPageDestination.route) {
        MyPageRoute()
    }
}
