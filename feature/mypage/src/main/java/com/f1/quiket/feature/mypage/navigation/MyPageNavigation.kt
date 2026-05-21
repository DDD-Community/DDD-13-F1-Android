package com.f1.quiket.feature.mypage.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.f1.quiket.feature.mypage.presentation.MyPageRoute
import com.f1.quiket.feature.mypage.presentation.MyPageSettingRoute

fun NavGraphBuilder.myPageGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
) {
    composable(route = MyPageDestination.route) {
        MyPageRoute(
            onNavigateToSettings = {
                navController.navigate(MyPageSettingDestination.route)
            },
        )
    }
    composable(route = MyPageSettingDestination.route) {
        MyPageSettingRoute(
            onNavigateBack = { navController.popBackStack() },
            onLogout = onLogout,
        )
    }
}