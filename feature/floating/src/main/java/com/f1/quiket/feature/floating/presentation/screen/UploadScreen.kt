package com.f1.quiket.feature.floating.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.floating.presentation.component.upload.ChapterNameInput
import com.f1.quiket.feature.floating.presentation.component.upload.LectureShortCard
import com.f1.quiket.feature.floating.presentation.component.upload.LectureUploadSection
import com.f1.quiket.feature.floating.presentation.component.upload.PartClassifyMethod
import com.f1.quiket.feature.floating.presentation.component.upload.PartClassifySection
import com.f1.quiket.feature.floating.presentation.component.upload.UploadNextButton
import com.f1.quiket.feature.floating.presentation.component.upload.UploadTab
import com.f1.quiket.feature.floating.presentation.component.upload.UploadTopBar

@Composable
fun UploadScreen(
    lectureTitle: String? = null,
    lecturePurpose: String? = null,
    chapterCount: Int? = null,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    var chapterName by remember { mutableStateOf("") }
    var classifyMethod by remember { mutableStateOf(PartClassifyMethod.AI) }
    var selectedTab by remember { mutableStateOf(UploadTab.FILE) }
    var isContentReady by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brown50)
    ) {
        // ── TopBar (고정) ─────────────────────────────────────────────
        Surface(color = White) {
            Column {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsTopHeight(WindowInsets.statusBars),
                )
                UploadTopBar(onBackClick = onBackClick)
            }
        }

        // ── TopBar 아래 스크롤 영역 ───────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            // 상단 흰 영역 (챕터명 + 파트 분류)
            Surface(
                color = White,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
                ) {
                    if (lectureTitle != null && chapterCount != null && lecturePurpose != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            LectureShortCard(
                                title = lectureTitle,
                                chapterCount = chapterCount,
                                purpose = lecturePurpose,
                                onClick = {},
                                modifier = Modifier.width(142.dp),
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    ChapterNameInput(
                        value = chapterName,
                        onValueChange = { chapterName = it },
                        nextChapterNumber = (chapterCount ?: 0) + 1,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    PartClassifySection(
                        selected = classifyMethod,
                        onSelect = { classifyMethod = it },
                    )
                }
            }

            // 하단 Brown50 영역 (탭 + 업로드 영역)
            LectureUploadSection(
                selectedTab = selectedTab,
                onTabSelect = { selectedTab = it },
                onReadyChange = { isContentReady = it },
                modifier = Modifier.heightIn(min = 400.dp),
            )
        }

        // ── 다음 버튼 (고정) ─────────────────────────────────────────
        Surface(color = White) {
            UploadNextButton(
                enabled = chapterName.isNotBlank() && isContentReady,
                onClick = onNextClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "자료추가 — 과목선택 후 진입")
@Composable
private fun UploadScreenWithLecturePreview() {
    QuiketTheme {
        UploadScreen(
            lectureTitle = "SQLD",
            lecturePurpose = "시험·자격증 대비",
            chapterCount = 3,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "자료추가 — 바로 진입")
@Composable
private fun UploadScreenDirectPreview() {
    QuiketTheme {
        UploadScreen()
    }
}