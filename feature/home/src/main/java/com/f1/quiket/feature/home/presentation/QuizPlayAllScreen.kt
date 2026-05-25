package com.f1.quiket.feature.home.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f1.quiket.core.designsystem.theme.Brown950
import com.f1.quiket.core.designsystem.theme.Gray700
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White

@Composable
fun QuizPlayAllRoute(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizPlayAllViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    QuizPlayAllScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onCloseClick = onCloseClick,
        modifier = modifier,
    )
}

@Composable
fun QuizPlayAllScreen(
    state: QuizPlayAllState,
    onIntent: (QuizPlayAllIntent) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentQuestion = state.currentQuestion

    BackHandler(
        enabled = state.showTutorial || state.isQuestionListVisible || state.isSubmitConfirmVisible,
    ) {
        when {
            state.showTutorial -> onIntent(QuizPlayAllIntent.DismissTutorial)
            state.isSubmitConfirmVisible -> onIntent(QuizPlayAllIntent.CloseSubmitConfirm)
            state.isQuestionListVisible -> onIntent(QuizPlayAllIntent.CloseQuestionList)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            QuizPlayAllTopBar(
                onListClick = { onIntent(QuizPlayAllIntent.OpenQuestionList) },
                onCloseClick = onCloseClick,
            )

            QuizPlayAllProgress(
                current = state.currentQuestionNumber.coerceAtMost(state.totalQuestionCount),
                total = state.totalQuestionCount,
                progress = state.progressFraction,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            if (currentQuestion == null) {
                QuizPlayAllEmptyContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            } else {
                QuizPlayAllContent(
                    question = currentQuestion,
                    selectedOptionId = state.selectedOptionId,
                    bookmarked = state.isCurrentQuestionBookmarked,
                    onOptionClick = { optionId -> onIntent(QuizPlayAllIntent.SelectOption(optionId)) },
                    onBookmarkClick = { onIntent(QuizPlayAllIntent.ToggleBookmark) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                )
            }
        }

        QuizPlayAllBottomBar(
            canMovePrevious = state.canMovePrevious,
            canMoveNext = state.canMoveNext,
            isLastQuestion = state.isLastQuestion,
            onPreviousClick = { onIntent(QuizPlayAllIntent.MovePrevious) },
            onNextClick = { onIntent(QuizPlayAllIntent.MoveNext) },
            onSubmitClick = { onIntent(QuizPlayAllIntent.OpenSubmitConfirm) },
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        if (state.isQuestionListVisible) {
            QuizPlayAllQuestionListSheet(
                state = state,
                onDismiss = { onIntent(QuizPlayAllIntent.CloseQuestionList) },
                onQuestionClick = { index -> onIntent(QuizPlayAllIntent.SelectQuestion(index)) },
            )
        }

        if (state.isSubmitConfirmVisible) {
            QuizPlayAllSubmitConfirmDialog(
                unsolvedQuestions = state.unsolvedQuestions,
                bookmarkedQuestions = state.bookmarkedQuestions,
                onContinueClick = { onIntent(QuizPlayAllIntent.CloseSubmitConfirm) },
                onSubmitClick = { onIntent(QuizPlayAllIntent.Submit) },
            )
        }

        if (state.showTutorial) {
            QuizPlayAllTutorialOverlay(
                onDismiss = { onIntent(QuizPlayAllIntent.DismissTutorial) },
            )
        }
    }
}

@Composable
private fun QuizPlayAllContent(
    question: QuizPlayAllQuestion,
    selectedOptionId: String?,
    bookmarked: Boolean,
    onOptionClick: (String) -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 28.dp, bottom = 116.dp),
    ) {
        QuizPlayAllQuestionHeader(
            questionNumber = question.number,
            timerText = question.timerText,
            bookmarked = bookmarked,
            onBookmarkClick = onBookmarkClick,
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = question.body,
            color = Gray950,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 22.sp,
                lineHeight = 31.sp,
                fontWeight = FontWeight.Bold,
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            question.options.forEach { option ->
                QuizPlayAllOptionButton(
                    option = option,
                    selected = selectedOptionId == option.id,
                    onClick = { onOptionClick(option.id) },
                )
            }
        }
    }
}

@Composable
private fun QuizPlayAllEmptyContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "문제를 불러올 수 없어요",
            color = Gray700,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Composable
private fun QuizPlayAllBottomBar(
    canMovePrevious: Boolean,
    canMoveNext: Boolean,
    isLastQuestion: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(92.dp)
            .background(White)
            .padding(start = 16.dp, end = 16.dp, bottom = 28.dp),
        horizontalArrangement = if (isLastQuestion) Arrangement.spacedBy(12.dp) else Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QuizPlayAllCircleButton(
            direction = QuizPlayAllMoveDirection.Previous,
            enabled = canMovePrevious,
            onClick = onPreviousClick,
        )

        if (isLastQuestion) {
            QuizPlayAllSubmitButton(
                onClick = onSubmitClick,
                modifier = Modifier.weight(1f),
            )
        }

        QuizPlayAllCircleButton(
            direction = QuizPlayAllMoveDirection.Next,
            enabled = canMoveNext,
            onClick = onNextClick,
        )
    }
}

@Composable
private fun QuizPlayAllSubmitButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Brown950)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "제출하기",
            color = White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
            ),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, widthDp = 360, heightDp = 800)
@Composable
private fun QuizPlayAllScreenPreview() {
    QuiketTheme {
        QuizPlayAllScreen(
            state = previewQuizPlayAllState,
            onIntent = {},
            onCloseClick = {},
        )
    }
}

private val previewQuizPlayAllState = QuizPlayAllState(
    questions = listOf(
        QuizPlayAllQuestion(
            id = "preview-1",
            number = 1,
            body = "SQL에서 두 테이블의 공통 컬럼을 기준으로 행을 결합하는 연산은 무엇인가요?",
            timerText = "00:30",
            options = listOf(
                QuizPlayAllOption("preview-1-1", 1, "SELECT"),
                QuizPlayAllOption("preview-1-2", 2, "JOIN"),
                QuizPlayAllOption("preview-1-3", 3, "WHERE"),
                QuizPlayAllOption("preview-1-4", 4, "GROUP BY"),
            ),
        ),
    ),
    selectedOptionIds = mapOf("preview-1" to "preview-1-2"),
    bookmarkedQuestionIds = setOf("preview-1"),
    showTutorial = false,
)
