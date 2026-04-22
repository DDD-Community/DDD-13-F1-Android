package com.f1.quiket.feature.main.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.f1.quiket.core.designsystem.component.QuiketTopBar
import com.f1.quiket.feature.history.navigation.HistoryDestination
import com.f1.quiket.feature.history.navigation.historyGraph
import com.f1.quiket.feature.home.navigation.HomeDestination
import com.f1.quiket.feature.home.navigation.homeGraph
import com.f1.quiket.feature.mypage.navigation.MyPageDestination
import com.f1.quiket.feature.mypage.navigation.myPageGraph
import com.f1.quiket.feature.review.navigation.ReviewDestination
import com.f1.quiket.feature.review.navigation.reviewGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: MainTab.Home.destination.route
    val currentTab = MainTab.entries.firstOrNull { tab -> tab.destination.route == currentRoute } ?: MainTab.Home

    Scaffold(
        topBar = { QuiketTopBar(title = currentTab.label) },
        bottomBar = {
            NavigationBar {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.destination.route,
                        onClick = {
                            navController.navigate(tab.destination.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
                        icon = { Text(text = tab.iconText) },
                        label = { Text(text = tab.label) },
                    )
                }
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = HomeDestination.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            homeGraph()
            historyGraph()
            reviewGraph()
            myPageGraph()
        }
    }
}
