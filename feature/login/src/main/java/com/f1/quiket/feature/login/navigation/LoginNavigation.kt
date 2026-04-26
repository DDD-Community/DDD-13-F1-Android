package com.f1.quiket.feature.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.f1.quiket.feature.login.presentation.LoginRoute

fun NavGraphBuilder.loginGraph(
    onBackClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit = {},
) {
    composable(route = LoginDestination.route) {
        LoginRoute(
            onLoginSuccess = onLoginSuccess,
            onBackClick = onBackClick,
            onSignUpClick = onSignUpClick,
        )
    }
}
