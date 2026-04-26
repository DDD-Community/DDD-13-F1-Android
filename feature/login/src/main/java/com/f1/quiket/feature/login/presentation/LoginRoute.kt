package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onBackClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {
    LoginScreen(
        onBackClick = onBackClick,
        onQuiketLoginClick = onLoginSuccess,
        onKakaoLoginClick = onLoginSuccess,
        onSignUpClick = onSignUpClick,
    )
}
