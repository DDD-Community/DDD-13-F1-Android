package com.f1.quiket.feature.splash.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    onDecideNext: (Boolean) -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(1200L)
        onDecideNext(false)
    }

    SplashScreen()
}
