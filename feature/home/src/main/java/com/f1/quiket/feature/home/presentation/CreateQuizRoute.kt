package com.f1.quiket.feature.home.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun CreateQuizRoute(
    onBackClick: () -> Unit,
    onAddSubjectClick: () -> Unit,
    onCreateQuizClick: () -> Unit = {},
) {
    val subjects = remember { createQuizSubjectSamples() }
    var selectedSubjectId by rememberSaveable { mutableStateOf<String?>(null) }
    var currentStep by rememberSaveable { mutableStateOf(CreateQuizStep.Subject) }
    var expandedChapterId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedPartIds by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var selectedQuizType by rememberSaveable { mutableStateOf<QuizTypeOption?>(null) }
    var selectedChoiceCount by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedQuestionCountOption by rememberSaveable { mutableStateOf<QuizQuestionCountOption?>(null) }
    var customQuestionCount by rememberSaveable { mutableStateOf<Int?>(null) }
    var customQuestionCountDialogVisible by rememberSaveable { mutableStateOf(false) }
    var customQuestionCountText by rememberSaveable { mutableStateOf("") }
    var selectedDifficulty by rememberSaveable { mutableStateOf<QuizDifficultyOption?>(null) }
    var quizCreationRequested by rememberSaveable { mutableStateOf(false) }

    val selectedSubject = subjects.firstOrNull { subject ->
        subject.id == selectedSubjectId
    }
    val selectedPartIdSet = selectedPartIds.toSet()
    val selectedChapterCount = selectedSubject?.chapters?.count { chapter ->
        chapter.parts.any { part -> part.id in selectedPartIdSet }
    } ?: 0
    val selectedPartCount = selectedPartIds.size

    fun resetQuizOptions() {
        selectedQuizType = null
        selectedChoiceCount = null
        selectedQuestionCountOption = null
        customQuestionCount = null
        customQuestionCountDialogVisible = false
        customQuestionCountText = ""
        selectedDifficulty = null
        quizCreationRequested = false
    }

    BackHandler(
        enabled = currentStep != CreateQuizStep.Subject && !customQuestionCountDialogVisible,
    ) {
        if (currentStep == CreateQuizStep.Loading) {
            onBackClick()
        } else {
            currentStep = when (currentStep) {
                CreateQuizStep.Subject -> CreateQuizStep.Subject
                CreateQuizStep.Scope -> CreateQuizStep.Subject
                CreateQuizStep.Options -> CreateQuizStep.Scope
                CreateQuizStep.Loading -> CreateQuizStep.Options
            }
        }
    }

    LaunchedEffect(currentStep, selectedSubject?.id, selectedPartCount) {
        if (currentStep != CreateQuizStep.Subject && selectedSubject == null) {
            currentStep = CreateQuizStep.Subject
        } else if (currentStep == CreateQuizStep.Options && selectedPartCount == 0) {
            currentStep = CreateQuizStep.Scope
        }
    }

    when (currentStep) {
        CreateQuizStep.Subject -> {
            CreateQuizSubjectScreen(
                subjects = subjects,
                selectedSubjectId = selectedSubjectId,
                onSubjectClick = { selectedSubject ->
                    val isDifferentSubject = selectedSubjectId != selectedSubject.id
                    selectedSubjectId = selectedSubject.id
                    if (isDifferentSubject) {
                        selectedPartIds = selectedSubject.allPartIds()
                        expandedChapterId = null
                        resetQuizOptions()
                    }
                },
                onAddSubjectClick = onAddSubjectClick,
                onBackClick = onBackClick,
                onNextClick = {
                    selectedSubject?.let { subject ->
                        if (selectedPartIds.isEmpty()) {
                            selectedPartIds = subject.allPartIds()
                        }
                        currentStep = CreateQuizStep.Scope
                    }
                },
            )
        }

        CreateQuizStep.Scope -> {
            selectedSubject?.let { subject ->
                CreateQuizScopeScreen(
                    subject = subject,
                    selectedPartIds = selectedPartIds.toSet(),
                    expandedChapterId = expandedChapterId,
                    onBackClick = {
                        currentStep = CreateQuizStep.Subject
                    },
                    onChapterExpandClick = { chapterId ->
                        expandedChapterId = if (expandedChapterId == chapterId) null else chapterId
                    },
                    onChapterSelectionClick = { chapter ->
                        val currentSelectedPartIds = selectedPartIds.toSet()
                        val chapterPartIds = chapter.parts.map { part -> part.id }.toSet()
                        val nextSelectedPartIds = if (chapterPartIds.all { it in currentSelectedPartIds }) {
                            currentSelectedPartIds - chapterPartIds
                        } else {
                            currentSelectedPartIds + chapterPartIds
                        }
                        selectedPartIds = nextSelectedPartIds.toList()
                    },
                    onPartClick = { part ->
                        selectedPartIds = selectedPartIds.toggle(part.id)
                    },
                    onClearAllClick = {
                        selectedPartIds = emptyList()
                    },
                    onNextClick = {
                        currentStep = CreateQuizStep.Options
                    },
                )
            }
        }

        CreateQuizStep.Options -> {
            selectedSubject?.let { subject ->
                CreateQuizOptionsScreen(
                    subject = subject,
                    selectedChapterCount = selectedChapterCount,
                    selectedPartCount = selectedPartCount,
                    selectedQuizType = selectedQuizType,
                    selectedChoiceCount = selectedChoiceCount,
                    selectedQuestionCountOption = selectedQuestionCountOption,
                    customQuestionCount = customQuestionCount,
                    selectedDifficulty = selectedDifficulty,
                    quizCreationRequested = quizCreationRequested,
                    customQuestionCountDialogVisible = customQuestionCountDialogVisible,
                    customQuestionCountText = customQuestionCountText,
                    onBackClick = {
                        currentStep = CreateQuizStep.Scope
                    },
                    onQuizTypeClick = { quizType ->
                        quizCreationRequested = false
                        selectedQuizType = quizType
                        if (!quizType.requiresChoiceCount) {
                            selectedChoiceCount = null
                        }
                    },
                    onChoiceCountClick = { count ->
                        quizCreationRequested = false
                        selectedChoiceCount = count
                    },
                    onQuestionCountClick = { option ->
                        quizCreationRequested = false
                        selectedQuestionCountOption = option
                    },
                    onCustomQuestionCountClick = {
                        customQuestionCountText = customQuestionCount?.toString().orEmpty()
                        customQuestionCountDialogVisible = true
                    },
                    onCustomQuestionCountTextChange = { text ->
                        customQuestionCountText = text.filter { character -> character.isDigit() }
                    },
                    onCustomQuestionCountDismiss = {
                        customQuestionCountDialogVisible = false
                    },
                    onCustomQuestionCountApply = {
                        val count = customQuestionCountText.toIntOrNull()
                        if (count != null && count > 0) {
                            quizCreationRequested = false
                            customQuestionCount = count
                            selectedQuestionCountOption = QuizQuestionCountOption.Custom
                            customQuestionCountDialogVisible = false
                        }
                    },
                    onDifficultyClick = { difficulty ->
                        quizCreationRequested = false
                        selectedDifficulty = difficulty
                    },
                    onCreateQuizClick = {
                        quizCreationRequested = true
                        currentStep = CreateQuizStep.Loading
                        onCreateQuizClick()
                    },
                )
            }
        }

        CreateQuizStep.Loading -> {
            CreateQuizLoadingScreen(
                progress = 0.4f,
                rewardCount = 10,
                onBrowseClick = onBackClick,
            )
        }
    }
}

data class QuizSubjectUiModel(
    val id: String,
    val name: String,
    val chapters: List<QuizScopeChapterUiModel>,
) {
    val chapterCount: Int
        get() = chapters.size

    val partCount: Int
        get() = chapters.sumOf { chapter -> chapter.parts.size }
}

data class QuizScopeChapterUiModel(
    val id: String,
    val title: String,
    val chapterNumber: Int,
    val parts: List<QuizScopePartUiModel>,
)

data class QuizScopePartUiModel(
    val id: String,
    val title: String,
)

private enum class CreateQuizStep {
    Subject,
    Scope,
    Options,
    Loading,
}

internal fun createQuizSubjectSamples(): List<QuizSubjectUiModel> = listOf(
    QuizSubjectUiModel(
        id = "sqld",
        name = "SQLD",
        chapters = listOf(
            QuizScopeChapterUiModel(
                id = "sqld-basic",
                title = "SQLD 기본",
                chapterNumber = 1,
                parts = listOf(
                    QuizScopePartUiModel(id = "sqld-basic-overview", title = "SQLD 개요"),
                    QuizScopePartUiModel(id = "sqld-basic-modeling", title = "데이터모델링"),
                    QuizScopePartUiModel(id = "sqld-basic-sql", title = "핵심 SQL 문법"),
                ),
            ),
            QuizScopeChapterUiModel(
                id = "sqld-data-model",
                title = "데이터 모델",
                chapterNumber = 2,
                parts = listOf(
                    QuizScopePartUiModel(id = "sqld-data-model-concept", title = "데이터 모델의 이해"),
                    QuizScopePartUiModel(id = "sqld-data-model-entity", title = "엔터티와 속성"),
                    QuizScopePartUiModel(id = "sqld-data-model-relation", title = "관계와 식별자"),
                    QuizScopePartUiModel(id = "sqld-data-model-performance", title = "모델링과 성능"),
                ),
            ),
            QuizScopeChapterUiModel(
                id = "sqld-usage",
                title = "SQL 활용",
                chapterNumber = 3,
                parts = listOf(
                    QuizScopePartUiModel(id = "sqld-usage-join", title = "JOIN 활용"),
                    QuizScopePartUiModel(id = "sqld-usage-subquery", title = "서브쿼리"),
                ),
            ),
        ),
    ),
    QuizSubjectUiModel(
        id = "figma-workshop",
        name = "기획자의 피그마 실무 워크숍",
        chapters = listOf(
            QuizScopeChapterUiModel(
                id = "figma-basic",
                title = "피그마 기본",
                chapterNumber = 1,
                parts = listOf(
                    QuizScopePartUiModel(id = "figma-basic-interface", title = "인터페이스"),
                    QuizScopePartUiModel(id = "figma-basic-frame", title = "프레임과 오토레이아웃"),
                    QuizScopePartUiModel(id = "figma-basic-component", title = "컴포넌트"),
                ),
            ),
            QuizScopeChapterUiModel(
                id = "figma-prototype",
                title = "프로토타입",
                chapterNumber = 2,
                parts = listOf(
                    QuizScopePartUiModel(id = "figma-prototype-flow", title = "화면 흐름"),
                    QuizScopePartUiModel(id = "figma-prototype-interaction", title = "인터랙션"),
                    QuizScopePartUiModel(id = "figma-prototype-test", title = "테스트"),
                ),
            ),
            QuizScopeChapterUiModel(
                id = "figma-handoff",
                title = "실무 핸드오프",
                chapterNumber = 3,
                parts = listOf(
                    QuizScopePartUiModel(id = "figma-handoff-token", title = "디자인 토큰"),
                    QuizScopePartUiModel(id = "figma-handoff-spec", title = "스펙 정리"),
                    QuizScopePartUiModel(id = "figma-handoff-review", title = "리뷰"),
                ),
            ),
        ),
    ),
    QuizSubjectUiModel(
        id = "western-philosophy",
        name = "서양철학사",
        chapters = listOf(
            QuizScopeChapterUiModel(
                id = "western-philosophy-intro",
                title = "서양철학 입문",
                chapterNumber = 1,
                parts = listOf(
                    QuizScopePartUiModel(id = "western-philosophy-intro-history", title = "철학사의 흐름"),
                ),
            ),
        ),
    ),
)

private fun QuizSubjectUiModel.allPartIds(): List<String> =
    chapters.flatMap { chapter -> chapter.parts.map { part -> part.id } }

private fun List<String>.toggle(id: String): List<String> =
    if (id in this) {
        this - id
    } else {
        this + id
    }
