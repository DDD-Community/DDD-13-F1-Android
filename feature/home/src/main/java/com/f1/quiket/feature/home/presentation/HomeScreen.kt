package com.f1.quiket.feature.home.presentation

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.f1.quiket.core.designsystem.component.HomeActionButton
import com.f1.quiket.core.designsystem.component.HomeProfileCard
import com.f1.quiket.core.designsystem.theme.*
import com.f1.quiket.feature.home.component.*
import com.f1.quiket.feature.home.model.*

@Composable
fun HomeScreen(
    uiState: HomeState,
    onBoardingDone: () -> Unit,
    onFabItemClick: (FabAction) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

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
                    HomeTopBar(
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
                                text = "퀴즈 만들기",
                                iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_make,
                                backgroundColor = Orange500,
                                onClick = {},
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            HomeProfileCard(
                "송미짱짱짱",
                1200,
                com.f1.quiket.core.designsystem.R.drawable.ic_profile,
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
                    subjectTabRect
                ),
                TutorialStep(
                    2,
                    "나의 강의 자료를 ",
                    "pdf, 이미지,\n텍스트",
                    "로 업로드할 수 있어요",
                    TooltipAlignment.Step2,
                    uploadButtonRect
                ),
                TutorialStep(
                    3,
                    "업로드한 강의를 기반으로",
                    "\nAI가 퀴즈를 만들어줘요",
                    "",
                    TooltipAlignment.Step3,
                    quizButtonRect
                )
            )
            val secondPageSteps = listOf(
                TutorialStep(
                    4,
                    "누르면 마이페이지로 이동해요.퀴즈로\n모은 ",
                    "도토리",
                    "를 쓸 수 있어요!",
                    TooltipAlignment.Step4,
                    profileCardRect
                ),
                TutorialStep(
                    5,
                    "최근에 생성하고 풀어본 퀴즈",
                    " 항목",
                    "을 볼 수 있어요",
                    TooltipAlignment.Step5,
                    activityTabRect
                ),
                TutorialStep(
                    6,
                    "플로팅 버튼으로도 ",
                    "과목 추가,\n강의 업로드, 퀴즈 만들기",
                    " 등을 \n모두 할 수 있어요!",
                    TooltipAlignment.Step6,
                    fabRect
                )
            )
            HomeTutorialOverlay(
                currentPage = tutorialPage,
                firstPageSteps = firstPageSteps,
                secondPageSteps = secondPageSteps,
                onNext = {
                    when (tutorialPage) {
                        TutorialPage.FIRST -> tutorialPage = TutorialPage.SECOND
                        TutorialPage.SECOND -> showTutorial = false
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