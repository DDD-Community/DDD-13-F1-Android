package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
) {
    LoginScreen(
        onLoginClick = onLoginSuccess,
    )
}
