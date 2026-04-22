package com.f1.quiket.feature.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.f1.quiket.core.designsystem.component.QuiketPrimaryButton
import com.f1.quiket.core.designsystem.component.QuiketTopBar
import com.f1.quiket.core.designsystem.theme.quiketSpacing

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
) {
    val spacing = quiketSpacing

    Scaffold(
        topBar = { QuiketTopBar(title = "로그인") },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = spacing.large, vertical = spacing.xLarge),
            verticalArrangement = Arrangement.spacedBy(spacing.large),
        ) {
            Text(
                text = "학습을 이어가려면 로그인이 필요해요.",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "현재는 실제 인증 연동 전 단계라서, 버튼만 누르면 메인 화면으로 이동하는 뼈대입니다.",
                style = MaterialTheme.typography.bodyLarge,
            )
            QuiketPrimaryButton(
                label = "로그인하고 시작하기",
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
