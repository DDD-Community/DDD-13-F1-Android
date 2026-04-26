package com.f1.quiket

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.feature.login.navigation.LoginDestination
import com.f1.quiket.feature.login.navigation.loginGraph
import com.f1.quiket.feature.main.navigation.MainDestination
import com.f1.quiket.feature.main.presentation.MainRoute
import com.f1.quiket.feature.onboarding.navigation.OnboardingDestination
import com.f1.quiket.feature.onboarding.navigation.onboardingGraph
import com.f1.quiket.feature.splash.navigation.SplashDestination
import com.f1.quiket.feature.splash.navigation.splashGraph

@Composable
fun QuiketApp() {
    QuiketTheme {
        val context = LocalContext.current.applicationContext
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SplashDestination.route,
        ) {
            splashGraph(
                onDecideNext = {
                    // TODO: Restore onboarding completion check after onboarding QA.
//                        val nextRoute = if (context.isOnboardingCompleted()) {
//                            LoginDestination.route
//                        } else {
//                            OnboardingDestination.route
//                        }
                    val nextRoute = OnboardingDestination.route
                    navigateToRoot(navController, nextRoute, SplashDestination.route)
                },
            )

            onboardingGraph(
                onComplete = {
                    context.setOnboardingCompleted()
                    navigateToRoot(navController, LoginDestination.route, OnboardingDestination.route)
                },
            )

            loginGraph(
                onBackClick = {
                    navController.navigateUp()
                },
                onLoginSuccess = {
                    navigateToRoot(navController, MainDestination.route, LoginDestination.route)
                },
            )

            composable(route = MainDestination.route) {
                MainRoute()
            }
        }
    }
}

private const val QUIKET_PREFERENCES_NAME = "quiket_preferences"
private const val ONBOARDING_COMPLETED_KEY = "onboarding_completed"

private fun Context.isOnboardingCompleted(): Boolean =
    getSharedPreferences(QUIKET_PREFERENCES_NAME, Context.MODE_PRIVATE)
        .getBoolean(ONBOARDING_COMPLETED_KEY, false)

private fun Context.setOnboardingCompleted() {
    getSharedPreferences(QUIKET_PREFERENCES_NAME, Context.MODE_PRIVATE)
        .edit()
        .putBoolean(ONBOARDING_COMPLETED_KEY, true)
        .apply()
}

private fun navigateToRoot(
    navController: NavHostController,
    route: String,
    popUpToRoute: String,
) {
    navController.navigate(route) {
        popUpTo(popUpToRoute) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
