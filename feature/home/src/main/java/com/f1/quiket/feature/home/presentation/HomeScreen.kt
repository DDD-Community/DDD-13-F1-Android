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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.f1.quiket.core.designsystem.component.HomeActionButton
import com.f1.quiket.core.designsystem.component.HomeProfileCard
import com.f1.quiket.core.designsystem.component.SubjectShortCard
import com.f1.quiket.core.designsystem.theme.Black
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray600
import com.f1.quiket.core.designsystem.theme.Gray800
import com.f1.quiket.core.designsystem.theme.Gray900
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.Orange500
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.home.R
import com.f1.quiket.feature.home.component.ExpandableFab
import com.f1.quiket.feature.home.component.HomeActivityCard
import com.f1.quiket.feature.home.component.HomeEmptyActivityButton
import com.f1.quiket.feature.home.component.HomeEmptySubjectButton
import com.f1.quiket.feature.home.component.HomeExamCard
import com.f1.quiket.feature.home.component.HomeGuideTooltip
import com.f1.quiket.feature.home.model.Activity
import com.f1.quiket.feature.home.model.ActivityType
import com.f1.quiket.feature.home.model.Exam
import com.f1.quiket.feature.home.model.FabAction
import com.f1.quiket.feature.home.model.Subject

@Composable
fun HomeScreen(
    uiState: HomeState,
    onBoardingDone: () -> Unit,
    onFabItemClick: (FabAction) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    var noteIconOffset by remember { mutableStateOf(Offset.Zero) }
    var noteIconSize by remember { mutableStateOf(IntSize.Zero) }

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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단 흰색 컨테이너 영역
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = White,
                // 하단 라운딩
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp,
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 24.dp
                        )
                ) {
                    // 상단바
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(com.f1.quiket.core.designsystem.R.drawable.ic_quiket_logo),
                            contentDescription = "Home Quiket Logo",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(width = 90.dp, height = 28.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(R.drawable.ic_home_note),
                            contentDescription = "Home Quiket Note",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                                .onGloballyPositioned{ coordinates ->
                                    noteIconOffset = coordinates.positionInRoot()
                                    noteIconSize = coordinates.size
                                }
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_home_alert),
                            contentDescription = "Home Quiket Alert",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(24.dp)
                        )
                    }
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
                        HomeActionButton(
                            text = "자료 업로드",
                            iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_upload,
                            backgroundColor = Gray100,
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        )
                        HomeActionButton(
                            text = "퀴즈 만들기",
                            iconRes = com.f1.quiket.core.designsystem.R.drawable.ic_home_make,
                            backgroundColor = Orange500,
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            HomeProfileCard(
                "송미짱짱짱",
                1200,
                com.f1.quiket.core.designsystem.R.drawable.ic_profile,
                { },
                modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) { page ->

                val exam = exams[page]

                HomeExamCard(
                    examName = exam.name,
                    date = exam.date,
                    dDay = exam.dDay,
                    onClick = {}
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TabItem(
                    "내 과목",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(0.7f)
                )
                TabItem(
                    "최근 활동",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(0.7f)
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
                        //EmptySubjectContent()
                        ActiveSubjectContent()
                    } else {
                        //EmptyActivityContent()
                        ActiveActivityContent()
                    }
                }
            }
        }

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

@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = if (isSelected) White else Gray100,
        contentColor = if (isSelected) Gray950 else Gray800
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun EmptySubjectContent() {
    QuiketTheme {
        Column {
            HomeEmptySubjectButton(
                {},
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
            )
        }
    }
}

@Composable
fun ActiveSubjectContent() {

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

    QuiketTheme {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .height(24.dp)
                    .padding(top = 10.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "전체 보기",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray600
                )
                Icon(
                    painter = painterResource(R.drawable.ic_home_subject_total),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                val chunked = subjects.chunked(2)

                items(chunked.size) { rowIndex ->

                    val rowItems = chunked[rowIndex]

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowItems.forEachIndexed { index, subject ->
                            SubjectShortCard(
                                title = subject.title,
                                chapter = subject.chapter,
                                isStarred = subject.isStarred,
                                modifier = Modifier.weight(1f),
                                onStarToggle = {
                                    val realIndex = rowIndex * 2 + index
                                    subjects = subjects.mapIndexed { i, item ->
                                        if (i == realIndex) item.copy(isStarred = !item.isStarred)
                                        else item
                                    }
                                },
                                onClick = {}
                            )
                        }

                        // 짝 안 맞을 때 빈칸 유지
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyActivityContent() {
    QuiketTheme {
        Column {
            HomeEmptyActivityButton(
                {},
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp)
            )
        }
    }
}

@Composable
fun ActiveActivityContent() {
    val activities = listOf(
        Activity(
            title = "서양철학사",
            questionCount = 30,
            activityType = ActivityType.MULTIPLE_CHOICE,
            description = "잘하고 있어요!",
            progressPercent = 56,
            isQuizCreated = true
        ),
        Activity(
            title = "기획자의 피그마 실무 워크...",
            questionCount = 10,
            activityType = ActivityType.SHORT_ANSWER,
            description = "아직 퀴즈 문제를 풀지 않았어요!",
            progressPercent = null,
            isQuizCreated = false
        ),
        Activity(
            title = "SQLD",
            questionCount = 10,
            activityType = ActivityType.OX_QUIZ,
            description = "나머지 퀴즈로 이어서 풀어볼까요?",
            progressPercent = 70,
            isQuizCreated = false
        )
    )

    QuiketTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            // 전체 보기 헤더
            Row(
                modifier = Modifier
                    .height(24.dp)
                    .padding(top = 10.dp, end = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "전체 보기",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray600
                )
                Icon(
                    painter = painterResource(R.drawable.ic_home_subject_total),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(16.dp)
                )
            }

            // 활동 카드 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(activities) { activity ->
                    HomeActivityCard(
                        title = activity.title,
                        questionCount = activity.questionCount,
                        activityType = activity.activityType,
                        description = activity.description,
                        progressPercent = activity.progressPercent,
                        isQuizCreated = activity.isQuizCreated,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val uiState = HomeState(
        isLoading = false,
        showOnboarding = true
    )

    QuiketTheme {
        HomeScreen(
            uiState = uiState,
            onBoardingDone = {},
            onFabItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActiveActivityContentPreview() {
    QuiketTheme {
        ActiveActivityContent()
    }
}

