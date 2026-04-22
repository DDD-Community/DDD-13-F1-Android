package com.f1.quiket

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.feature.login.navigation.LoginDestination
import com.f1.quiket.feature.login.presentation.LoginRoute
import com.f1.quiket.feature.main.navigation.MainDestination
import com.f1.quiket.feature.main.presentation.MainRoute
import com.f1.quiket.feature.splash.navigation.SplashDestination
import com.f1.quiket.feature.splash.presentation.SplashRoute

@Composable
fun QuiketApp() {
    QuiketTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SplashDestination.route,
        ) {
            composable(route = SplashDestination.route) {
                SplashRoute(
                    onDecideNext = { isLoggedIn ->
                        if (isLoggedIn) {
                            navigateToRoot(navController, MainDestination.route, SplashDestination.route)
                        } else {
                            navigateToRoot(navController, LoginDestination.route, SplashDestination.route)
                        }
                    },
                )
            }

            composable(route = LoginDestination.route) {
                LoginRoute(
                    onLoginSuccess = {
                        navigateToRoot(navController, MainDestination.route, LoginDestination.route)
                    },
                )
            }

            composable(route = MainDestination.route) {
                MainRoute()
            }
        }
    }
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
