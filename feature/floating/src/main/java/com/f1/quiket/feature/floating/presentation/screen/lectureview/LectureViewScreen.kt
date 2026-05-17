package com.f1.quiket.feature.floating.presentation.screen.lectureview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.f1.quiket.core.designsystem.theme.Dimmed
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray700
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.floating.R
import com.f1.quiket.feature.floating.domain.model.Chapter
import com.f1.quiket.feature.floating.domain.model.LectureItem
import com.f1.quiket.feature.floating.domain.model.TocChapter
import com.f1.quiket.feature.floating.domain.model.TocPart
import com.f1.quiket.feature.floating.presentation.component.LectureItemCard
import com.f1.quiket.feature.floating.presentation.component.LectureViewBottomBar
import com.f1.quiket.feature.floating.presentation.component.LectureViewTopBar
import com.f1.quiket.feature.floating.presentation.component.TocChapterHeader
import com.f1.quiket.feature.floating.presentation.component.TocPartItem

// Sample Data
private val sampleLectureItems = listOf(
    LectureItem(1, "1.", "데이터베이스의 개념", "여러 사람이 공유하여 사용할 목적으로 체계화해 통합·관리하는 데이터의 집합"),
    LectureItem(2, "1-1.", "DBMS의 특징", "독립성, 무결성, 보안성, 일관성, 중복 최소화", isClipped = true),
    LectureItem(3, "1-2.", "데이터 모델의 구성 요소", "구조(Structure), 연산(Operation), 제약(Constraint)"),
    LectureItem(4, "2.", "관계형 데이터베이스", "릴레이션으로 데이터를 표현하는 데이터베이스 모델"),
    LectureItem(5, "2-1.", "키(Key)의 종류", "기본키, 외래키, 후보키, 슈퍼키, 대리키", isClipped = true),
)

private val sampleTocChapters = listOf(
    TocChapter(
        id = 1,
        title = "SQLD 기본",
        parts = listOf(
            TocPart(1, "파트 1 — 데이터베이스 개념", isSelected = true),
            TocPart(2, "파트 2 — 데이터 모델링"),
            TocPart(3, "파트 3 — 정규화"),
        ),
    ),
    TocChapter(
        id = 2,
        title = "데이터 모델",
        parts = listOf(
            TocPart(4, "파트 1 — 엔터티"),
            TocPart(5, "파트 2 — 속성"),
            TocPart(6, "파트 3 — 관계"),
            TocPart(7, "파트 4 — 식별자"),
        ),
    ),
)

@Composable
fun LectureViewScreen(
    chapter: Chapter,
    onBackClick: () -> Unit,
) {
    val allParts = remember { sampleTocChapters.flatMap { it.parts } }
    var currentPartIndex by remember { mutableIntStateOf(0) }
    var showTocSidebar by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Gray100,
            contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0),
            topBar = {
                LectureViewTopBar(
                    title = chapter.name,
                    onBackClick = onBackClick,
                    onTocClick = { showTocSidebar = true },
                    onEditClick = {},
                    onDeletePartClick = {},
                )
            },
            bottomBar = {
                LectureViewBottomBar(
                    currentIndex = currentPartIndex,
                    totalCount = allParts.size,
                    onPrevious = { if (currentPartIndex > 0) currentPartIndex-- },
                    onNext = { if (currentPartIndex < allParts.size - 1) currentPartIndex++ },
                )
            },
        ) { innerPadding ->
            LectureList(
                items = sampleLectureItems,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        }

        if (showTocSidebar) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Dimmed)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { showTocSidebar = false },
                    )
                    .zIndex(1f),
            )
        }

        AnimatedVisibility(
            visible = showTocSidebar,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
                .zIndex(2f),
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }),
        ) {
            TocSidePanel(
                chapters = sampleTocChapters,
                selectedPartId = allParts.getOrNull(currentPartIndex)?.id,
                onPartClick = { partId ->
                    val idx = allParts.indexOfFirst { it.id == partId }
                    if (idx >= 0) currentPartIndex = idx
                    showTocSidebar = false
                },
                onClose = { showTocSidebar = false },
            )
        }
    }
}


@Composable
private fun LectureList(
    items: List<LectureItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }
        items(items, key = { it.id }) { item ->
            LectureItemCard(item = item)
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun TocSidePanel(
    chapters: List<TocChapter>,
    selectedPartId: Int?,
    onPartClick: (Int) -> Unit,
    onClose: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(White)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_addsubject_close),
                contentDescription = "null",
                tint = Color.Transparent,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "목차",
                style = MaterialTheme.typography.titleSmall,
                color = Gray950
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onClose) {
                Icon(
                    painter = painterResource(R.drawable.ic_addsubject_close),
                    contentDescription = "닫기",
                    tint = Gray700,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        HorizontalDivider(color = Gray100, thickness = 1.dp)

        // 챕터 > 파트 목차 리스트
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            chapters.forEach { chapter ->
                item(key = "chapter_${chapter.id}") {
                    TocChapterHeader(chapter = chapter)
                }
                items(chapter.parts, key = { "part_${it.id}" }) { part ->
                    TocPartItem(
                        part = part.copy(isSelected = part.id == selectedPartId),
                        onClick = { onPartClick(part.id) },
                    )
                    HorizontalDivider(color = Gray100, thickness = 1.dp)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LectureViewScreenPreview() {
    QuiketTheme {
        LectureViewScreen(
            chapter = Chapter(number = 1, name = "SQLD", partCount = 3),
            onBackClick = {},
        )
    }
}