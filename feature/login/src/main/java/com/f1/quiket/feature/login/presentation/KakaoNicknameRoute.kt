package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun KakaoNicknameRoute(
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    viewModel: KakaoNicknameViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                KakaoNicknameEffect.NavigateToMain -> onComplete()
                is KakaoNicknameEffect.ShowMessage -> Unit
            }
        }
    }

    SignUpNicknameScreen(
        nickname = state.nickname,
        nicknameErrorMessage = state.nicknameErrorMessage,
        isNextEnabled = state.isNextEnabled,
        onNicknameChange = { viewModel.onIntent(KakaoNicknameIntent.NicknameChanged(it)) },
        onBackClick = onBackClick,
        onNextClick = { viewModel.onIntent(KakaoNicknameIntent.Submit) },
    )
}
