package com.f1.quiket.feature.mypage.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyPageSettingRoute(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: MyPageSettingViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                MyPageSettingEffect.GoBack -> onNavigateBack()
                MyPageSettingEffect.GoToLogin -> onLogout()
                MyPageSettingEffect.GoToAccountSetting -> { /* TODO */ }
                MyPageSettingEffect.GoToAlarmSetting -> { /* TODO */ }
                MyPageSettingEffect.GoToInquiry -> { /* TODO */ }
                MyPageSettingEffect.GoToTerms -> { /* TODO */ }
                MyPageSettingEffect.GoToPrivacyPolicy -> { /* TODO */ }
                MyPageSettingEffect.GoToAppInfo -> { /* TODO */ }
            }
        }
    }

    MyPageSettingScreen(
        onIntent = viewModel::onIntent,
    )
}
