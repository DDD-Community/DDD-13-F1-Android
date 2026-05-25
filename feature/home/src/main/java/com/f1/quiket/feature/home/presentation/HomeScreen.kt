package com.f1.quiket.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.f1.quiket.core.designsystem.component.HomeActionButton
import com.f1.quiket.core.designsystem.component.HomeExamCard
import com.f1.quiket.core.designsystem.component.HomeProfileCard
import com.f1.quiket.core.designsystem.component.NoSubjectPopup
import com.f1.quiket.core.designsystem.component.QuiketTopBar
import com.f1.quiket.core.designsystem.theme.Black
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray600
import com.f1.quiket.core.designsystem.theme.Gray900
import com.f1.quiket.core.designsystem.theme.Orange500
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.home.component.ExpandableFab
import com.f1.quiket.feature.home.component.HomeGuideTooltip
import com.f1.quiket.feature.home.component.HomeTutorialOverlay
import com.f1.quiket.feature.home.model.Exam
import com.f1.quiket.feature.floating.presentation.screen.UploadScreen
import com.f1.quiket.feature.floating.presentation.screen.lectureselect.LectureSelectScreen
import com.f1.quiket.feature.floating.presentation.screen.subjectdetail.SubjectDetailScreen
import com.f1.quiket.feature.home.model.FabAction
import com.f1.quiket.feature.home.model.Subject
import com.f1.quiket.feature.home.model.TutorialPage
import com.f1.quiket.feature.home.model.buildTutorialPages

@Composable
fun HomeScreen(
    uiState: HomeState,
    onBoardingDone: () -> Unit,
    onFabItemClick: (FabAction) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var showNoSubjectPopup by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf<Subject?>(null) }
    var depth by remember { mutableStateOf(0) }
    var subjects by remember {
        mutableStateOf(
            listOf(
                Subject("오픽 2주만에 IH 달성", "챕터 3", false),
                Subject("Android 앱 개발", "챕터 7", true),
                Subject("자료구조", "챕터 2", false),
                Subject("운영체제", "챕터 5", false),
            )
        )
    }

    // 강의 선택 후 UploadScreen에 넘길 정보
    var selectedLectureTitle by remember { mutableStateOf("") }
    var selectedLectureChapterCount by remember { mutableStateOf(0) }

    // depth 1 ~ 3: SubjectDetail / LectureSelect / Upload
    when (depth) {
        1 -> {
            SubjectDetailScreen(
                subjectName = selectedSubject?.title ?: "",
                studyPurposeLabel = "",
                examTypeLabel = "",
                detailLabel = "",
                onBackClick = { depth = 0 },
                onUploadClick = { depth = 2 },
                onChapterAddClick = { depth = 3 },
                onSubjectNameChanged = { newName ->
                    subjects = subjects.map { s ->
                        if (s.title == selectedSubject?.title) s.copy(title = newName) else s
                    }
                    selectedSubject = selectedSubject?.copy(title = newName)
                },
            )
            return
        }
        2 -> {
            LectureSelectScreen(
                onBackClick = { depth = 1 },
                onLectureSelected = { _, title, count ->
                    selectedLectureTitle = title
                    selectedLectureChapterCount = count
                    depth = 4
                },
            )
            return
        }
        3 -> {
            UploadScreen(
                lectureTitle = selectedSubject?.title,
                lecturePurpose = selectedSubject?.chapter,
                chapterCount = 0,
                onBackClick = { depth = 1 },
                onNextClick = { depth = 1 },
            )
            return
        }
        4 -> {
            UploadScreen(
                lectureTitle = selectedLectureTitle,
                lecturePurpose = null,
                chapterCount = selectedLectureChapterCount,
                onBackClick = { depth = 2 },
                onNextClick = { depth = 1 },
            )
            return
        }
    }

    // 온보딩 툴팁
    var noteIconOffset by remember { mutableStateOf(Offset.Zero) }
    var noteIconSize by remember { mutableStateOf(IntSize.Zero) }

    // 튜토리얼 위치
    var uploadButtonRect by remember { mutableStateOf<Rect?>(null) }
    var quizButtonRect by remember { mutableStateOf<Rect?>(null) }
    var profileCardRect by remember { mutableStateOf<Rect?>(null) }
    var activityTabRect by remember { mutableStateOf<Rect?>(null) }
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
            // ── TopBar (고정) ────────────────────────────────────────────
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsTopHeight(WindowInsets.statusBars),
                    )
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
                }
            }

            // ── TopBar 아래 스크롤 영역 ──────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                // 상단 흰 카드 (제목 + 액션 버튼)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = White,
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                    ) {
                        Text(
                            "오늘의 공부, 시작해 볼까요?",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Black,
                            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                        )
                        Text(
                            "내 강의 노트를 업로드 하거나 퀴즈를 만들어 보세요 !",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = Gray600
                        )

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
                                    onClick = {
                                        if (uiState.hasSubjects) onFabItemClick(FabAction.Upload)
                                        else showNoSubjectPopup = true
                                    },
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
                    com.f1.quiket.core.designsystem.R.drawable.ic_qring_profile,
                    {},
                    modifier = Modifier
                        .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                        .onGloballyPositioned { coords ->
                            val pos = coords.positionInRoot()
                            profileCardRect = Rect(
                                pos.x, pos.y,
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
                        modifier = Modifier
                            .weight(0.7f)
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
                                    pos.x, pos.y,
                                    pos.x + coords.size.width,
                                    pos.y + coords.size.height
                                )
                            }
                    )
                    Spacer(modifier = Modifier.weight(1.6f))
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = White,
                    shape = RoundedCornerShape(topEnd = 24.dp)
                ) {
                    Box {
                        if (selectedTab == 0) {
                            ActiveSubjectContent(
                                subjects = subjects,
                                onSubjectsChange = { subjects = it },
                                onSubjectAreaPositioned = {},
                                onSubjectClick = { subject ->
                                    selectedSubject = subject
                                    depth = 1
                                },
                            )
                        } else {
                            ActiveActivityContent()
                        }
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
            val (firstPageSteps, secondPageSteps, thirdPageSteps) = buildTutorialPages(
                subjectTabRect = subjectTabRect,
                uploadButtonRect = uploadButtonRect,
                quizButtonRect = quizButtonRect,
                profileCardRect = profileCardRect,
                activityTabRect = activityTabRect,
                fabRect = fabRect,
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
            onItemClick = { action ->
                isExpanded = false
                if (action == FabAction.Upload && !uiState.hasSubjects) {
                    showNoSubjectPopup = true
                } else {
                    onFabItemClick(action)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .onGloballyPositioned { coords ->
                    val pos = coords.positionInRoot()
                    fabRect = Rect(pos.x, pos.y, pos.x + coords.size.width, pos.y + coords.size.height)
                }
        )

        if (showNoSubjectPopup) {
            NoSubjectPopup(
                onAddSubject = {
                    showNoSubjectPopup = false
                    onFabItemClick(FabAction.AddSubject)
                },
                onDismiss = { showNoSubjectPopup = false },
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