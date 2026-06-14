package com.f1.quiket.feature.mypage.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyPageSettingRoute(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToAccountSetting: () -> Unit,
    onNavigateToAlarmSetting: () -> Unit,
    onNavigateToInquiry: () -> Unit,
    viewModel: MyPageSettingViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                MyPageSettingEffect.GoBack -> onNavigateBack()
                MyPageSettingEffect.GoToLogin -> onLogout()
                MyPageSettingEffect.GoToAccountSetting -> onNavigateToAccountSetting()
                MyPageSettingEffect.GoToAlarmSetting -> onNavigateToAlarmSetting()
                MyPageSettingEffect.GoToInquiry -> onNavigateToInquiry()
                MyPageSettingEffect.GoToTerms -> { /* TODO: 웹뷰 또는 외부 링크 */ }
                MyPageSettingEffect.GoToPrivacyPolicy -> { /* TODO: 웹뷰 또는 외부 링크 */ }
                MyPageSettingEffect.GoToAppInfo -> { /* TODO: 앱 정보 화면 */ }
            }
        }
    }

    MyPageSettingScreen(
        onIntent = viewModel::onIntent,
    )
}