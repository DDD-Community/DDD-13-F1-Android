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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.Dimmed
import com.f1.quiket.core.designsystem.theme.Gray100
import com.f1.quiket.core.designsystem.theme.Gray50
import com.f1.quiket.core.designsystem.theme.Gray500
import com.f1.quiket.core.designsystem.theme.Gray950
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.feature.floating.domain.model.Chapter
import com.f1.quiket.feature.floating.domain.model.LectureItem
import com.f1.quiket.feature.floating.presentation.component.LectureViewBottomBar
import com.f1.quiket.feature.floating.presentation.component.LectureViewTopBar
import com.f1.quiket.feature.floating.presentation.component.TocSidePanel
import com.f1.quiket.feature.floating.presentation.viewmodel.LectureViewViewModel


@Composable
fun LectureViewScreen(
    subjectId: String,
    chapter: Chapter,
    onBackClick: () -> Unit,
    viewModel: LectureViewViewModel = hiltViewModel(),
) {
    val tocChapters by viewModel.tocChapters.collectAsStateWithLifecycle()
    val allPartIds by viewModel.allPartIds.collectAsStateWithLifecycle()
    val currentPartId by viewModel.currentPartId.collectAsStateWithLifecycle()
    val currentPartName by viewModel.currentPartName.collectAsStateWithLifecycle()
    val currentPartContent by viewModel.currentPartContent.collectAsStateWithLifecycle()

    var showTocSidebar by remember { mutableStateOf(false) }

    LaunchedEffect(subjectId, chapter.id) {
        if (subjectId.isNotEmpty() && chapter.id.isNotEmpty()) {
            viewModel.loadSubject(subjectId, chapter.id)
        }
    }

    val currentPartIndex = remember(currentPartId, allPartIds) {
        allPartIds.indexOfFirst { it == currentPartId }.coerceAtLeast(0)
    }

    val displayItems = remember(currentPartContent, currentPartName) {
        val content = currentPartContent
        val name = currentPartName ?: ""
        if (content.isNullOrBlank()) emptyList()
        else listOf(LectureItem(id = 1, number = "", title = name, content = content))
    }

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
                    totalCount = allPartIds.size.coerceAtLeast(1),
                    onPrevious = {
                        if (currentPartIndex > 0) viewModel.selectPart(allPartIds[currentPartIndex - 1])
                    },
                    onNext = {
                        if (currentPartIndex < allPartIds.size - 1) viewModel.selectPart(allPartIds[currentPartIndex + 1])
                    },
                )
            },
        ) { innerPadding ->
            LectureList(
                items = displayItems,
                chapterNumber = chapter.number,
                partTitle = currentPartName ?: "",
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
                chapters = tocChapters,
                selectedPartId = currentPartId,
                onPartClick = { partId ->
                    viewModel.selectPart(partId)
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
    chapterNumber: Int,
    partTitle: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray50)
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "챕터 $chapterNumber",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Gray500,
            )
            Text(
                text = ">",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Gray500,
            )
            Text(
                text = partTitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Gray500,
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Brown50)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item { Spacer(modifier = Modifier.height(12.dp)) }
            items(items, key = { it.id }) { item ->
                LectureTextItem(item = item)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun LectureTextItem(
    item: LectureItem,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = item.number,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Gray950,
                modifier = Modifier.width(30.dp),
            )
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Gray950,
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = item.content,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Gray950,
            lineHeight = 20.sp,
            modifier = Modifier.padding(start = 36.dp),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LectureViewScreenPreview() {
    QuiketTheme {
        LectureViewScreen(
            subjectId = "",
            chapter = Chapter(number = 1, name = "SQLD", partCount = 3),
            onBackClick = {},
        )
    }
}