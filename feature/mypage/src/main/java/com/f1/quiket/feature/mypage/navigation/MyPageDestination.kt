package com.f1.quiket.feature.mypage.navigation

import com.f1.quiket.core.navigation.QuiketDestination

data object MyPageDestination : QuiketDestination {
    override val route: String = "main/mypage"
}

data object MyPageSettingDestination : QuiketDestination {
    override val route: String = "main/mypage/setting"
}