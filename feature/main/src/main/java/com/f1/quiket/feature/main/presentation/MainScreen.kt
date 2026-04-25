package com.f1.quiket.feature.main.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.f1.quiket.core.designsystem.component.QuiketTopBar
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray400
import com.f1.quiket.feature.history.navigation.historyGraph
import com.f1.quiket.feature.home.navigation.HomeDestination
import com.f1.quiket.feature.home.navigation.homeGraph
import com.f1.quiket.feature.mypage.navigation.myPageGraph
import com.f1.quiket.feature.review.navigation.reviewGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: MainTab.Home.destination.route
    val currentTab =
        MainTab.entries.firstOrNull { tab -> tab.destination.route == currentRoute } ?: MainTab.Home

    Scaffold(
        //topBar = { QuiketTopBar(title = currentTab.label) },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                MainTab.entries.forEach { tab ->

                    val selected = currentRoute == tab.destination.route

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.destination.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(
                                    id = if (selected) tab.selectedIconRes else tab.unselectedIconRes
                                ),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) Brown950 else Gray400
                            )
                        },
                        // 선택한 Bottom바 색상 제거
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
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
