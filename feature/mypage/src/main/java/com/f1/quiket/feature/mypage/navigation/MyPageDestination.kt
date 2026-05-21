package com.f1.quiket.feature.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.f1.quiket.core.navigation.QuiketDestination
import com.f1.quiket.feature.mypage.presentation.MyPageRoute
import com.f1.quiket.feature.mypage.presentation.MyPageSettingRoute

data object MyPageDestination : QuiketDestination {
    override val route: String = "main/mypage"
}

data object MyPageSettingDestination : QuiketDestination {
    override val route: String = "main/mypage/setting"
}