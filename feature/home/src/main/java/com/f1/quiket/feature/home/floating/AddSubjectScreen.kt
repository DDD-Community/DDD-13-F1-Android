package com.f1.quiket.feature.home.floating

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.f1.quiket.feature.home.presentation.CreateQuizTopBar

@Composable
fun AddSubjectScreen(
    onBackClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        CreateQuizTopBar(
            title = "과목 추가",
            onBackClick = onBackClick,
        )
        Text(
            text = "과목 추가 화면입니다",
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
        )
    }
}
