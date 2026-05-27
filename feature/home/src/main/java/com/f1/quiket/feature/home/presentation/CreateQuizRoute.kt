package com.f1.quiket.feature.home.presentation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.f1.quiket.feature.home.domain.model.QuizCreate
import com.f1.quiket.feature.home.domain.model.QuizDifficulty
import com.f1.quiket.feature.home.domain.model.QuizPlayMode
import com.f1.quiket.feature.home.domain.model.ServerQuizType

@Composable
fun CreateQuizRoute(
    onBackClick: () -> Unit,
    onAddSubjectClick: () -> Unit,
    onQuizGenerationStarted: () -> Unit = {},
    onQuizGenerationFinished: () -> Unit = {},
    onQuizCreated: (String) -> Unit = {},
    viewModel: CreateQuizViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val subjects = state.subjects
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
    var shouldAutoSelectParts by rememberSaveable { mutableStateOf(false) }

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
    }

    fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun buildCreateRequest(): QuizCreate? {
        val subject = selectedSubject ?: return null
        val quizType = selectedQuizType?.toServerQuizTypeOrNull()
        if (quizType == null) {
            showMessage("아직 지원하지 않는 퀴즈 유형이에요.")
            return null
        }
        val questionCount = selectedQuestionCountOption.toQuestionCount(customQuestionCount)
        if (questionCount == null) {
            showMessage("문제수를 선택해주세요.")
            return null
        }
        val difficulty = selectedDifficulty?.toDomain()
        if (difficulty == null) {
            showMessage("난이도를 선택해주세요.")
            return null
        }
        if (selectedPartIds.isEmpty()) {
            showMessage("출제 범위를 선택해주세요.")
            return null
        }

        return QuizCreate(
            subjectId = subject.id,
            partIds = selectedPartIds,
            quizType = quizType,
            choiceCount = if (quizType == ServerQuizType.MultipleChoice) {
                selectedChoiceCount ?: 4
            } else {
                null
            },
            questionCount = questionCount.coerceIn(1, 100),
            playMode = QuizPlayMode.AllAtOnce,
            timerEnabled = false,
            difficulty = difficulty,
        )
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CreateQuizEffect.ShowMessage -> showMessage(effect.message)
                CreateQuizEffect.QuizGenerationFinished -> onQuizGenerationFinished()
                is CreateQuizEffect.QuizCreated -> {
                    onQuizGenerationFinished()
                    onQuizCreated(effect.quizSessionId)
                }
            }
        }
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

    LaunchedEffect(selectedSubject?.id, selectedSubject?.chapters) {
        val subject = selectedSubject ?: return@LaunchedEffect
        if (shouldAutoSelectParts && selectedPartIds.isEmpty()) {
            val allPartIds = subject.allPartIds()
            if (allPartIds.isNotEmpty()) {
                selectedPartIds = allPartIds
                shouldAutoSelectParts = false
            }
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
                        shouldAutoSelectParts = selectedPartIds.isEmpty()
                        resetQuizOptions()
                    }
                },
                onAddSubjectClick = onAddSubjectClick,
                onBackClick = onBackClick,
                onNextClick = {
                    selectedSubject?.let { subject ->
                        viewModel.onIntent(CreateQuizIntent.LoadQuizScope(subject.id))
                        if (selectedPartIds.isEmpty()) {
                            selectedPartIds = subject.allPartIds()
                            shouldAutoSelectParts = selectedPartIds.isEmpty()
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
                    quizCreationRequested = state.isCreatingQuiz,
                    customQuestionCountDialogVisible = customQuestionCountDialogVisible,
                    customQuestionCountText = customQuestionCountText,
                    onBackClick = {
                        currentStep = CreateQuizStep.Scope
                    },
                    onQuizTypeClick = { quizType ->
                        selectedQuizType = quizType
                        if (!quizType.requiresChoiceCount) {
                            selectedChoiceCount = null
                        }
                    },
                    onChoiceCountClick = { count ->
                        selectedChoiceCount = count
                    },
                    onQuestionCountClick = { option ->
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
                            customQuestionCount = count.coerceIn(1, 100)
                            selectedQuestionCountOption = QuizQuestionCountOption.Custom
                            customQuestionCountDialogVisible = false
                        }
                    },
                    onDifficultyClick = { difficulty ->
                        selectedDifficulty = difficulty
                    },
                    onCreateQuizClick = {
                        buildCreateRequest()?.let { request ->
                            currentStep = CreateQuizStep.Loading
                            onQuizGenerationStarted()
                            viewModel.createQuiz(request)
                        }
                    },
                )
            }
        }

        CreateQuizStep.Loading -> {
            CreateQuizLoadingScreen(
                progress = state.generationProgress.coerceIn(0f, 1f),
                rewardCount = state.rewardCount,
                onBrowseClick = onBackClick,
            )
        }
    }
}

data class QuizSubjectUiModel(
    val id: String,
    val name: String,
    val chapters: List<QuizScopeChapterUiModel>,
    val chapterCountOverride: Int? = null,
    val partCountOverride: Int? = null,
) {
    val chapterCount: Int
        get() = chapterCountOverride ?: chapters.size

    val partCount: Int
        get() = partCountOverride ?: chapters.sumOf { chapter -> chapter.parts.size }
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

private fun QuizTypeOption.toServerQuizTypeOrNull(): ServerQuizType? = when (this) {
    QuizTypeOption.MultipleChoice -> ServerQuizType.MultipleChoice
    QuizTypeOption.Ox -> ServerQuizType.Ox
    QuizTypeOption.Flashcard,
    QuizTypeOption.ShortAnswer,
    -> null
}

private fun QuizQuestionCountOption?.toQuestionCount(customQuestionCount: Int?): Int? = when (this) {
    QuizQuestionCountOption.Five -> 5
    QuizQuestionCountOption.Ten -> 10
    QuizQuestionCountOption.Twenty -> 20
    QuizQuestionCountOption.Custom -> customQuestionCount
    null -> null
}

private fun QuizDifficultyOption.toDomain(): QuizDifficulty = when (this) {
    QuizDifficultyOption.Easy -> QuizDifficulty.Easy
    QuizDifficultyOption.Normal -> QuizDifficulty.Medium
    QuizDifficultyOption.Hard -> QuizDifficulty.Hard
}
