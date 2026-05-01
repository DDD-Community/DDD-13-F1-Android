package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onEmailLoginClick: () -> Unit,
    onBackClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
) {
    LoginScreen(
        onBackClick = onBackClick,
        onQuiketLoginClick = onEmailLoginClick,
        onKakaoLoginClick = onLoginSuccess,
        onSignUpClick = onSignUpClick,
    )
}
