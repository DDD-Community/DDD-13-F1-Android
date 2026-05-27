package com.f1.quiket.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f1.quiket.core.designsystem.component.QuiketPrimaryButton
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray700
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.core.designsystem.theme.Yellow100
import com.f1.quiket.core.designsystem.theme.Yellow500
import com.f1.quiket.feature.home.domain.model.QuizResult
import com.f1.quiket.feature.home.domain.model.RewardSummary

@Composable
fun QuizResultRoute(
    playSessionId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizResultViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(playSessionId) {
        viewModel.onIntent(QuizResultIntent.Load(playSessionId))
    }

    QuizResultScreen(
        state = state,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
fun QuizResultScreen(
    state: QuizResultState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CreateQuizTopBar(
                title = "퀴즈 결과",
                onBackClick = onBackClick,
            )

            when {
                state.isLoading -> QuizResultMessage(
                    text = "결과를 불러오는 중이에요",
                    modifier = Modifier.weight(1f),
                )

                !state.errorMessage.isNullOrBlank() -> QuizResultMessage(
                    text = state.errorMessage,
                    modifier = Modifier.weight(1f),
                )

                state.result != null -> QuizResultContent(
                    result = state.result,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        QuiketPrimaryButton(
            text = "홈으로 가기",
            enabled = true,
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 28.dp),
        )
    }
}

@Composable
private fun QuizResultContent(
    result: QuizResult,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 116.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = result.subjectName ?: "퀴즈",
            color = Gray950,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 24.sp,
                lineHeight = 34.sp,
                fontWeight = FontWeight.Bold,
            ),
        )

        QuizResultScoreCard(result = result)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            QuizResultChip(text = "정답 ${result.correctCount}")
            QuizResultChip(text = "오답 ${result.wrongCount}")
            QuizResultChip(text = "건너뜀 ${result.skipCount}")
        }

        QuizRewardCard(rewards = result.rewards)

        if (result.reviewItems.isNotEmpty()) {
            Text(
                text = "문제별 해설",
                color = Gray950,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
            result.reviewItems
                .sortedBy { item -> item.displayOrder }
                .take(5)
                .forEach { item ->
                    QuizReviewPreviewCard(
                        number = item.displayOrder,
                        body = item.summary ?: item.body,
                        correct = item.correctServer,
                    )
                }
        }
    }
}

@Composable
private fun QuizResultScoreCard(
    result: QuizResult,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Brown50)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "${result.correctCount}/${result.totalCount}",
            color = Brown950,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 36.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
        Text(
            text = "정답률 ${result.accuracyPct}%",
            color = Gray700,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Composable
private fun QuizRewardCard(
    rewards: RewardSummary,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "획득 보상",
            color = Gray950,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "도토리 ${rewards.dotoriEarned} · XP ${rewards.xpEarned}",
            color = Brown950,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Composable
private fun QuizReviewPreviewCard(
    number: Int,
    body: String,
    correct: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Q$number",
            color = Brown950,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
            ),
        )
        Text(
            text = body,
            color = Gray950,
            maxLines = 2,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.weight(1f),
        )
        Text(
            text = if (correct) "정답" else "오답",
            color = if (correct) Yellow500 else Gray700,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Composable
private fun QuizResultChip(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(30.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Yellow100)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Yellow500,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Composable
private fun QuizResultMessage(
    text: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Gray700,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, widthDp = 360, heightDp = 800)
@Composable
private fun QuizResultScreenPreview() {
    QuiketTheme {
        QuizResultScreen(
            state = QuizResultState(),
            onBackClick = {},
        )
    }
}
