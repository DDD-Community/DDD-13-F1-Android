package com.f1.quiket.feature.home.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.Tutorial
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.home.model.TooltipAlignment
import com.f1.quiket.feature.home.model.TutorialPage
import com.f1.quiket.feature.home.model.TutorialStep

@Composable
fun HomeTutorialOverlay(
    currentPage: TutorialPage,
    firstPageSteps: List<TutorialStep>,
    secondPageSteps: List<TutorialStep>,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    statusBarHeight: Dp,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val currentSteps = when (currentPage) {
        TutorialPage.FIRST -> firstPageSteps
        TutorialPage.SECOND -> secondPageSteps
    }
    val isLastPage = currentPage == TutorialPage.SECOND

    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(100f)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onNext() }
        ) {
            drawRect(color = Tutorial)

            currentSteps.forEach { step ->
                step.anchorRect?.let { rect ->
                    val top = rect.top - with(density) { statusBarHeight.toPx() }
                    val bottom = rect.bottom - with(density) { statusBarHeight.toPx() }

                    drawRoundRect(
                        color = Color.Transparent,
                        topLeft = androidx.compose.ui.geometry.Offset(rect.left, top),
                        size = androidx.compose.ui.geometry.Size(
                            rect.right - rect.left,
                            bottom - top
                        ),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
                        blendMode = androidx.compose.ui.graphics.BlendMode.Clear
                    )
                }
            }
        }

        currentSteps.forEach { step ->
            step.anchorRect?.let { rect ->
                val top = with(density) { rect.top.toDp() } - statusBarHeight
                val bottom = with(density) { rect.bottom.toDp() } - statusBarHeight
                val left = with(density) { rect.left.toDp() }
                val right = with(density) { rect.right.toDp() }

                val (x, y) = when (step.tooltipAlignment) {
                    TooltipAlignment.Step1 -> Pair(left +40.dp, bottom + 20.dp)
                    TooltipAlignment.Step2 -> Pair(left, top - 60.dp)
                    TooltipAlignment.Step3 -> Pair(right - 170.dp, top - 100.dp)
                    TooltipAlignment.Step4 -> Pair(left, top - 80.dp)
                    TooltipAlignment.Step5 -> Pair(right - 80.dp, bottom + 12.dp)
                    TooltipAlignment.Step6 -> Pair(right - 200.dp, top - 50.dp)
                }

                TutorialTooltip(
                    step = step,
                    modifier = Modifier.offset(x = x, y = y)
                )
            } ?: run {
                android.util.Log.d("Tutorial", "step ${step.step} anchorRect is null")
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (!isLastPage) "탭하여 다음으로 넘어가기 >" else "Quiket 사용해보기",
                style = MaterialTheme.typography.labelSmall,
                color = White,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onNext() }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HomeTutorialOverlayFirstPagePreview() {
    val firstPageSteps = listOf(
        TutorialStep(
            1,
            "공부하고 싶은 과목을 추가해\n",
            "챕터, 파트",
            " 별로 분류해 보관할\n수 있어요",
            TooltipAlignment.Step1,
            anchorRect = Rect(24f, 400f, 400f, 500f)
        ),
        TutorialStep(
            2,
            "나의 강의 자료를 ",
            "pdf, 이미지,\n텍스트",
            "로 업로드할 수 있어요",
            TooltipAlignment.Step2,
            anchorRect = Rect(24f, 250f, 200f, 310f)
        ),
        TutorialStep(
            3,
            "업로드한 강의를 기반으로",
            "\nAI가 퀴즈를 만들어줘요",
            "",
            TooltipAlignment.Step3,
            anchorRect = Rect(210f, 250f, 400f, 310f)
        )
    )

    QuiketTheme {
        HomeTutorialOverlay(
            currentPage = TutorialPage.FIRST,
            firstPageSteps = firstPageSteps,
            secondPageSteps = emptyList(),
            onNext = {},
            onSkip = {},
            statusBarHeight = 0.dp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun HomeTutorialOverlaySecondPagePreview() {

    val secondPageSteps = listOf(
        TutorialStep(
            4,
            "누르면 마이페이지로 이동해요.\n퀴즈로 모은 ",
            "도토리",
            "를 쓸 수 있어요!",
            TooltipAlignment.Step4,
            anchorRect = Rect(16f, 100f, 420f, 160f)
        ),
        TutorialStep(
            5,
            "최근에 생성하고 풀어본 퀴즈",
            " 항목",
            "을 볼 수 있어요",
            TooltipAlignment.Step5,
            anchorRect = Rect(140f, 270f, 280f, 320f)
        ),
        TutorialStep(
            6,
            "플로팅 버튼으로도 ",
            "과목 추가,\n강의 업로드, 퀴즈 만들기",
            " 등을 \n모두 할 수 있어요!",
            TooltipAlignment.Step6,
            anchorRect = Rect(340f, 600f, 420f, 680f)
        )
    )
    QuiketTheme {
        HomeTutorialOverlay(
            currentPage = TutorialPage.SECOND,
            firstPageSteps = emptyList(),
            secondPageSteps = secondPageSteps,
            onNext = {},
            onSkip = {},
            statusBarHeight = 0.dp
        )
    }
}