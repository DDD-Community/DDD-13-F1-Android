package com.f1.quiket.feature.home.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.f1.quiket.core.designsystem.component.HomeActionButton
import com.f1.quiket.core.designsystem.component.HomeExamCard
import com.f1.quiket.core.designsystem.component.HomeProfileCard
import com.f1.quiket.core.designsystem.component.QuiketTopBar
import com.f1.quiket.core.designsystem.theme.*
import com.f1.quiket.feature.home.component.*
import com.f1.quiket.feature.home.model.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    uiState: HomeState,
    isQuizGenerating: Boolean = false,
    onBoardingDone: () -> Unit,
    onQuizCardClick: () -> Unit,
    onFabItemClick: (FabAction) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    val quizActionText = rememberQuizGeneratingText(isQuizGenerating)

    // 온보딩 툴팁
    var noteIconOffset by remember { mutableStateOf(Offset.Zero) }
    var noteIconSize by remember { mutableStateOf(IntSize.Zero) }

    // 튜토리얼 위치
    var uploadButtonRect by remember { mutableStateOf<Rect?>(null) }
    var quizButtonRect by remember { mutableStateOf<Rect?>(null) }
    var profileCardRect by remember { mutableStateOf<Rect?>(null) }
    var activityTabRect by remember { mutableStateOf<Rect?>(null) }
    var subjectAreaRect by remember { mutableStateOf<Rect?>(null) }
    var fabRect by remember { mutableStateOf<Rect?>(null) }
    var subjectTabRect by remember { mutableStateOf<Rect?>(null) }

    // 튜토리얼 상태
    var showTutorial by remember { mutableStateOf(false) }
    var tutorialPage by remember { mutableStateOf(TutorialPage.FIRST) }

    val exams = listOf(
        Exam("정보처리기사", "2026.06.28", "D-60"),
        Exam("SQLD", "2026.07.10", "D-82"),
        Exam("오픽", "2026.08.01", "D-104")
    )
    val pagerState = rememberPagerState(pageCount = { exams.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown50)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
                ) {
                    QuiketTopBar(
                        onNoteIconClick = {
                            tutorialPage = TutorialPage.FIRST
                            showTutorial = true
                        },
                        onNoteIconPositioned = { offset, size ->
                            noteIconOffset = offset
                            noteIconSize = size
                        },
                    )

                    Text(
                        "오늘의 공부, 시작해 볼까요?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Black,
                        modifier = Modifier.padding(top = 36.dp, bottom = 12.dp)
                    )
                    Text(
                        "내 강의 노트를 업로드 하거나 퀴즈를 만들어 보세요 !",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = Gray600
                    )

                    // 자료업로드
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .onGloballyPositioned { coords ->
                                    val pos = coords.positionInRoot()
                                    uploadButtonRect = Rect(
                                        pos.x, pos.y,
                                        pos.x + coords.size.width,
                                        pos.y + coords.size.height
                                    )
                                }
                        ) {
                            HomeActionButton(
                                text = "자료 업로드",
                                iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_upload,
                                backgroundColor = Gray100,
                                onClick = {},
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .onGloballyPositioned { coords ->
                                    val pos = coords.positionInRoot()
                                    quizButtonRect = Rect(
                                        pos.x, pos.y,
                                        pos.x + coords.size.width,
                                        pos.y + coords.size.height
                                    )
                                }
                        ) {
                            HomeActionButton(
                                text = quizActionText,
                                iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_make,
                                backgroundColor = Orange500,
                                onClick = onQuizCardClick,
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (isQuizGenerating) {
                                QuizGeneratingSparkleOverlay(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(12.dp))
                                )
                            }
                        }
                    }
                }
            }

            HomeProfileCard(
                "송미짱짱짱",
                1200,
                com.f1.quiket.core.designsystem.R.drawable.ic_qring_profile,
                {},
                modifier = Modifier
                    .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                    .onGloballyPositioned { coords ->
                        val pos = coords.positionInRoot()
                        profileCardRect = Rect(
                            pos.x,
                            pos.y,
                            pos.x + coords.size.width,
                            pos.y + coords.size.height
                        )
                    }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) { page ->
                HomeExamCard(
                    examName = exams[page].name,
                    date = exams[page].date,
                    dDay = exams[page].dDay,
                    onClick = {}
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                TabItem(
                    "내 과목",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(0.7f)
                        .onGloballyPositioned { coords ->
                            val pos = coords.positionInRoot()
                            subjectTabRect = Rect(
                                pos.x, pos.y,
                                pos.x + coords.size.width,
                                pos.y + coords.size.height
                            )
                        }

                )
                TabItem(
                    "최근 활동",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier
                        .weight(0.7f)
                        .onGloballyPositioned { coords ->
                            val pos = coords.positionInRoot()
                            activityTabRect = Rect(
                                pos.x,
                                pos.y,
                                pos.x + coords.size.width,
                                pos.y + coords.size.height
                            )
                        }
                )
                Spacer(modifier = Modifier.weight(1.6f))
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = White,
                shape = RoundedCornerShape(topEnd = 24.dp)
            ) {
                Box {
                    if (selectedTab == 0) {
                        ActiveSubjectContent(
                            onSubjectAreaPositioned = { subjectAreaRect = it }
                        )
                    } else {
                        ActiveActivityContent()
                    }
                }
            }
        }

        // 온보딩 툴팁
        if (uiState.showOnboarding && noteIconOffset != Offset.Zero) {
            val (yDp, endPadding) = rememberTooltipOffset(noteIconOffset, noteIconSize)
            HomeGuideTooltip(
                onClose = onBoardingDone,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = yDp)
                    .padding(end = endPadding)
                    .zIndex(10f)
            )
        }

        // 튜토리얼 오버레이
        if (showTutorial) {
            val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

            val firstPageSteps = listOf(
                TutorialStep(
                    1,
                    "공부하고 싶은 과목을 추가해\n",
                    "챕터, 파트",
                    " 별로 분류해 보관할\n수 있어요",
                    TooltipAlignment.Step1,
                    subjectTabRect,
                    arrowStartOffset = Offset(-30f, -1f),
                    arrowEndOffset = Offset(0f, -10f),
                    arrowCurvature = 0.3f
                ),
                TutorialStep(
                    2,
                    "나의 강의 자료를 ",
                    "pdf, 이미지,\n텍스트",
                    "로 업로드할 수 있어요",
                    TooltipAlignment.Step2,
                    uploadButtonRect,
                    arrowStartOffset = Offset(-150f, -60f),
                    arrowEndOffset = Offset(40f, -30f),
                    arrowCurvature = -0.3f
                ),
                TutorialStep(
                    3,
                    "업로드한 강의를 기반으로",
                    "\nAI가 퀴즈를 만들어줘요",
                    "",
                    TooltipAlignment.Step3,
                    quizButtonRect,
                    arrowStartOffset = Offset(30f, 0f),
                    arrowEndOffset = Offset(40f, -50f),
                    arrowCurvature = 0.3f
                )
            )
            val secondPageSteps = listOf(
                TutorialStep(
                    4,
                    "누르면 마이페이지로 이동해요.퀴즈로\n모은 ",
                    "도토리",
                    "를 쓸 수 있어요!",
                    TooltipAlignment.Step4,
                    profileCardRect,
                    arrowStartOffset = Offset(-160f, 5f),
                    arrowEndOffset = Offset(-30f, -55f),
                    arrowCurvature = -0.3f
                ),
                TutorialStep(
                    5,
                    "최근에 생성하고 풀어본 퀴즈",
                    " 항목",
                    "을 볼 수 있어요",
                    TooltipAlignment.Step5,
                    activityTabRect,
                    arrowStartOffset = Offset(-30f, -1f),
                    arrowEndOffset = Offset(0f, -15f),
                    arrowCurvature = 0.3f
                ),
                TutorialStep(
                    6,
                    "플로팅 버튼으로도 ",
                    "과목 추가,\n강의 업로드, 퀴즈 만들기",
                    " 등을 \n모두 할 수 있어요!",
                    TooltipAlignment.Step6,
                    fabRect,
                    arrowStartOffset = Offset(0f, 0f),
                    arrowEndOffset = Offset(-40f, -30f),
                    arrowCurvature = 0.3f
                )
            )
            val thirdPageSteps = listOf(
                TutorialStep(
                    7,
                    "퀴켓의 마스코트 다람쥐,\n",
                    "",
                    "'큐링이'에요.",
                    TooltipAlignment.Step7,
                    profileCardRect,
                    arrowStartOffset = Offset(-160f, 5f),
                    arrowEndOffset = Offset(-30f, -55f),
                    arrowCurvature = -0.3f
                ),
                TutorialStep(
                    8,
                    "획득한 도토리를 통해 '도토리\n",
                    "",
                    "상점'에서 큐링이를 위한 아이\n템을 구매할 수 있게 돼요!",
                    TooltipAlignment.Step8,
                    profileCardRect,
                    arrowStartOffset = Offset(100f, 0f),
                    arrowEndOffset = Offset(170f, 50f),
                    arrowCurvature = 0.3f
                )
            )
            HomeTutorialOverlay(
                currentPage = tutorialPage,
                firstPageSteps = firstPageSteps,
                secondPageSteps = secondPageSteps,
                thirdPageSteps = thirdPageSteps,
                onNext = {
                    when (tutorialPage) {
                        TutorialPage.FIRST -> tutorialPage = TutorialPage.SECOND
                        TutorialPage.SECOND -> tutorialPage = TutorialPage.THIRD
                        TutorialPage.THIRD -> showTutorial = false
                    }
                },
                onSkip = { showTutorial = false },
                statusBarHeight = statusBarHeight
            )
        }

        if (isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Gray900.copy(alpha = 0.6f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { isExpanded = false }
            )
        }

        ExpandableFab(
            isExpanded = isExpanded,
            onFabClick = { isExpanded = !isExpanded },
            onItemClick = {
                isExpanded = false
                onFabItemClick(it)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .onGloballyPositioned { coords ->
                    val pos = coords.positionInRoot()
                    fabRect =
                        Rect(pos.x, pos.y, pos.x + coords.size.width, pos.y + coords.size.height)
                }
        )
    }
}

@Composable
private fun rememberQuizGeneratingText(isGenerating: Boolean): String {
    var dotCount by remember { mutableIntStateOf(1) }

    LaunchedEffect(isGenerating) {
        if (!isGenerating) {
            dotCount = 1
            return@LaunchedEffect
        }

        dotCount = 1
        while (true) {
            delay(1_000L)
            dotCount = if (dotCount == 3) 1 else dotCount + 1
        }
    }

    return if (isGenerating) {
        "퀴즈 생성 중${".".repeat(dotCount)}"
    } else {
        "퀴즈 만들기"
    }
}

@Composable
private fun QuizGeneratingSparkleOverlay(
    modifier: Modifier = Modifier,
) {
    val hasSparkleAsset = rememberAssetAvailable(QUIZ_GENERATING_SPARKLE_ASSET)

    if (!hasSparkleAsset) {
        QuizGeneratingSparkleFallback(
            modifier = modifier,
        )
        return
    }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset(QUIZ_GENERATING_SPARKLE_ASSET),
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Int.MAX_VALUE,
        ignoreSystemAnimatorScale = true,
    )

    val loadedComposition = composition
    if (loadedComposition != null) {
        LottieAnimation(
            composition = loadedComposition,
            progress = { progress },
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    } else {
        QuizGeneratingSparkleFallback(
            modifier = modifier,
        )
    }
}

@Composable
private fun rememberAssetAvailable(assetPath: String): Boolean {
    val context = LocalContext.current
    return remember(context, assetPath) {
        runCatching {
            context.assets.open(assetPath).use { }
        }.isSuccess
    }
}

@Composable
private fun QuizGeneratingSparkleFallback(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "quiz_generating_sparkle")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1_200,
                easing = LinearEasing,
            ),
        ),
        label = "quiz_generating_sparkle_phase",
    )

    Canvas(modifier = modifier) {
        QuizGeneratingSparkleDots.forEach { sparkle ->
            val rawPulse = (phase + sparkle.phaseOffset) % 1f
            val pulse = if (rawPulse < 0.5f) rawPulse * 2f else (1f - rawPulse) * 2f
            drawCircle(
                color = White.copy(alpha = 0.18f + pulse * 0.28f),
                radius = sparkle.radiusDp.dp.toPx() * (0.75f + pulse * 0.3f),
                center = Offset(
                    x = size.width * sparkle.xFraction,
                    y = size.height * sparkle.yFraction,
                ),
            )
        }
    }
}

@Preview(name = "Home — 기본", showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    QuiketTheme {
        HomeScreen(
            uiState = HomeState(isLoading = false, showOnboarding = false),
            onBoardingDone = {},
            onQuizCardClick = {},
            onFabItemClick = {},
        )
    }
}

@Preview(name = "Home — 온보딩 툴팁", showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenOnboardingPreview() {
    QuiketTheme {
        HomeScreen(
            uiState = HomeState(isLoading = false, showOnboarding = true),
            onBoardingDone = {},
            onQuizCardClick = {},
            onFabItemClick = {},
        )
    }
}

@Preview(name = "Home — 퀴즈 생성 중", showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenQuizGeneratingPreview() {
    QuiketTheme {
        HomeScreen(
            uiState = HomeState(isLoading = false, showOnboarding = false),
            isQuizGenerating = true,
            onBoardingDone = {},
            onQuizCardClick = {},
            onFabItemClick = {},
        )
    }
}

@Composable
fun rememberTooltipOffset(
    noteIconOffset: Offset,
    noteIconSize: IntSize
): Pair<Dp, Dp> {
    val density = LocalDensity.current
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val yDp = with(density) { (noteIconOffset.y + noteIconSize.height).toDp() } - statusBarHeight
    val noteIconCenterDp = with(density) { (noteIconOffset.x + noteIconSize.width / 2).toDp() }
    val endPadding = screenWidth - noteIconCenterDp - 19.dp
    return Pair(yDp, endPadding)
}

private const val QUIZ_GENERATING_SPARKLE_ASSET = "lottie/anim_quiz_generating_sparkle.json"

private data class SparkleDot(
    val xFraction: Float,
    val yFraction: Float,
    val radiusDp: Float,
    val phaseOffset: Float,
)

private val QuizGeneratingSparkleDots = listOf(
    SparkleDot(xFraction = 0.46f, yFraction = 0.28f, radiusDp = 4.5f, phaseOffset = 0.00f),
    SparkleDot(xFraction = 0.62f, yFraction = 0.32f, radiusDp = 5.0f, phaseOffset = 0.17f),
    SparkleDot(xFraction = 0.82f, yFraction = 0.42f, radiusDp = 4.0f, phaseOffset = 0.34f),
    SparkleDot(xFraction = 0.58f, yFraction = 0.64f, radiusDp = 4.0f, phaseOffset = 0.51f),
    SparkleDot(xFraction = 0.90f, yFraction = 0.72f, radiusDp = 5.0f, phaseOffset = 0.68f),
)
