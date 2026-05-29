package com.f1.quiket.feature.floating.presentation.screen

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f1.quiket.core.designsystem.theme.Brown50
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.floating.presentation.component.upload.ChapterNameInput
import com.f1.quiket.feature.floating.presentation.component.upload.LectureShortCard
import com.f1.quiket.feature.floating.presentation.component.upload.LectureUploadSection
import com.f1.quiket.feature.floating.presentation.component.upload.PartClassifyMethod
import com.f1.quiket.feature.floating.presentation.component.upload.PartClassifySection
import com.f1.quiket.feature.floating.presentation.component.upload.UploadFile
import com.f1.quiket.feature.floating.presentation.component.upload.UploadImage
import com.f1.quiket.feature.floating.presentation.component.upload.UploadNextButton
import com.f1.quiket.feature.floating.presentation.component.upload.UploadTab
import com.f1.quiket.feature.floating.presentation.component.upload.UploadTopBar
import com.f1.quiket.feature.floating.presentation.viewmodel.UploadViewModel

@Composable
fun UploadScreen(
    subjectId: String? = null,
    chapterTitle: String? = null,
    lecturePurpose: String? = null,
    chapterCount: Int? = null,
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onUploadSuccess: (subjectId: String, chapterId: String, chapterName: String, chapterNumber: Int) -> Unit = { _, _, _, _ -> },
    viewModel: UploadViewModel = hiltViewModel(),
) {
    var chapterName by remember { mutableStateOf("") }
    var classifyMethod by remember { mutableStateOf(PartClassifyMethod.AI) }
    var selectedTab by remember { mutableStateOf(UploadTab.FILE) }
    var isContentReady by remember { mutableStateOf(false) }
    var manualSections by remember { mutableStateOf<List<String>>(emptyList()) }
    var currentFiles by remember { mutableStateOf<List<UploadFile>>(emptyList()) }
    var currentImages by remember { mutableStateOf<List<UploadImage>>(emptyList()) }
    var currentText by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isSuccess by viewModel.isSuccess.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val uploadedChapterId by viewModel.uploadedChapterId.collectAsStateWithLifecycle()

    // Cancel upload when back is pressed during loading
    BackHandler(enabled = isLoading) {
        viewModel.cancelUpload()
        onBackClick()
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            val sid = subjectId
            val cid = uploadedChapterId
            if (sid != null && cid != null) {
                onUploadSuccess(sid, cid, chapterName, (chapterCount ?: 0) + 1)
            } else {
                onNextClick()
            }
        }
    }

    // Show loading screen while API is processing
    if (isLoading) {
        UploadLoadingScreen(progress = progress)
        return
    }

    val isNextEnabled = chapterName.isNotBlank() && when (classifyMethod) {
        PartClassifyMethod.AI -> isContentReady
        PartClassifyMethod.MANUAL -> manualSections.isNotEmpty()
    }
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
                    if (chapterTitle != null && chapterCount != null && chapterCount >= 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            LectureShortCard(
                                title = chapterTitle,
                                chapterCount = chapterCount,
                                purpose = lecturePurpose.orEmpty(),
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
                        onManualApply = { manualSections = it },
                    )
                }
            }

            // 하단 Brown50 영역 (탭 + 업로드 영역)
            LectureUploadSection(
                selectedTab = selectedTab,
                onTabSelect = { selectedTab = it },
                onReadyChange = { isContentReady = it },
                onFilesChange = { currentFiles = it },
                onImagesChange = { currentImages = it },
                onTextChange = { currentText = it },
                modifier = Modifier.heightIn(min = 400.dp),
            )
        }

        // ── 다음 버튼 (고정) ─────────────────────────────────────────
        Surface(color = White) {
            UploadNextButton(
                enabled = isNextEnabled,
                onClick = {
                    val id = subjectId
                    if (id != null) {
                        viewModel.submit(
                            subjectId = id,
                            chapterName = chapterName,
                            tab = selectedTab,
                            classifyMethod = classifyMethod,
                            manualSections = manualSections,
                            files = currentFiles,
                            images = currentImages,
                            text = currentText,
                        )
                    } else {
                        onNextClick()
                    }
                },
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
            chapterTitle = "SQLD",
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