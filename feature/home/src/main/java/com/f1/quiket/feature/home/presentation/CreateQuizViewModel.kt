package com.f1.quiket.feature.home.presentation

import com.f1.quiket.core.common.mvi.MviViewModel
import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.feature.home.domain.model.QuizCreate
import com.f1.quiket.feature.home.domain.model.QuizGenerationStatus
import com.f1.quiket.feature.home.domain.model.QuizScope
import com.f1.quiket.feature.home.domain.model.QuizSubjectSummary
import com.f1.quiket.feature.home.domain.repository.HomeRepository
import com.f1.quiket.feature.home.domain.repository.QuizGenerationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val quizGenerationRepository: QuizGenerationRepository,
) : MviViewModel<CreateQuizState, CreateQuizIntent, CreateQuizEffect>(
    initialState = CreateQuizState(),
) {
    init {
        loadSubjects()
    }

    override fun handleIntent(intent: CreateQuizIntent) {
        when (intent) {
            CreateQuizIntent.LoadSubjects -> loadSubjects()
            is CreateQuizIntent.LoadQuizScope -> loadQuizScope(intent.subjectId)
        }
    }

    fun createQuiz(request: QuizCreate) {
        if (currentState.isCreatingQuiz) return

        launch {
            updateState {
                copy(
                    isCreatingQuiz = true,
                    generationProgress = 0.05f,
                )
            }

            when (val result = quizGenerationRepository.createQuizSession(request)) {
                is NetworkResult.Success -> {
                    updateState { copy(generationProgress = 0.1f) }
                    if (result.data.status == QuizGenerationStatus.Completed) {
                        updateState {
                            copy(
                                isCreatingQuiz = false,
                                generationProgress = 1f,
                            )
                        }
                        sendEffect(CreateQuizEffect.QuizCreated(result.data.quizSessionId))
                    } else {
                        pollGenerationStatus(result.data.quizSessionId)
                    }
                }

                is NetworkResult.Failure -> {
                    updateState {
                        copy(
                            isCreatingQuiz = false,
                            generationProgress = 0f,
                        )
                    }
                    sendEffect(CreateQuizEffect.QuizGenerationFinished)
                    sendEffect(CreateQuizEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun loadSubjects() {
        if (currentState.isLoadingSubjects) return

        launch {
            updateState { copy(isLoadingSubjects = true) }

            when (val result = homeRepository.getHome()) {
                is NetworkResult.Success -> {
                    updateState {
                        copy(
                            subjects = result.data.subjects.map { subject -> subject.toUiModel() },
                            isLoadingSubjects = false,
                        )
                    }
                }

                is NetworkResult.Failure -> {
                    updateState { copy(isLoadingSubjects = false) }
                    sendEffect(CreateQuizEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private fun loadQuizScope(subjectId: String) {
        val subject = currentState.subjects.firstOrNull { it.id == subjectId } ?: return
        if (subject.chapters.isNotEmpty() || currentState.loadingScopeSubjectId == subjectId) return

        launch {
            updateState { copy(loadingScopeSubjectId = subjectId) }

            when (val result = quizGenerationRepository.getQuizScope(subjectId)) {
                is NetworkResult.Success -> {
                    val scopedSubject = result.data.toUiModel(summary = subject)
                    updateState {
                        copy(
                            subjects = subjects.map { item ->
                                if (item.id == subjectId) scopedSubject else item
                            },
                            loadingScopeSubjectId = null,
                        )
                    }
                }

                is NetworkResult.Failure -> {
                    updateState { copy(loadingScopeSubjectId = null) }
                    sendEffect(CreateQuizEffect.ShowMessage(result.message))
                }
            }
        }
    }

    private suspend fun pollGenerationStatus(quizSessionId: String) {
        repeat(GENERATION_POLL_MAX_ATTEMPTS) { attempt ->
            if (attempt > 0) {
                delay(GENERATION_POLL_INTERVAL_MILLIS)
            }

            when (val status = quizGenerationRepository.getGenerationStatus(quizSessionId)) {
                is NetworkResult.Success -> {
                    val progress = status.data.progressPct
                        ?.coerceIn(0, 100)
                        ?.div(100f)
                        ?: currentState.generationProgress

                    updateState {
                        copy(
                            generationProgress = progress.coerceAtLeast(generationProgress),
                        )
                    }

                    when (status.data.status) {
                        QuizGenerationStatus.Completed -> {
                            updateState {
                                copy(
                                    isCreatingQuiz = false,
                                    generationProgress = 1f,
                                )
                            }
                            sendEffect(CreateQuizEffect.QuizCreated(quizSessionId))
                            return
                        }

                        QuizGenerationStatus.Failed -> {
                            updateState { copy(isCreatingQuiz = false) }
                            sendEffect(CreateQuizEffect.QuizGenerationFinished)
                            sendEffect(
                                CreateQuizEffect.ShowMessage(
                                    status.data.failReason ?: "퀴즈 생성에 실패했어요.",
                                ),
                            )
                            return
                        }

                        QuizGenerationStatus.Pending,
                        QuizGenerationStatus.InProgress,
                        QuizGenerationStatus.Unknown,
                        -> Unit
                    }
                }

                is NetworkResult.Failure -> {
                    updateState { copy(isCreatingQuiz = false) }
                    sendEffect(CreateQuizEffect.QuizGenerationFinished)
                    sendEffect(CreateQuizEffect.ShowMessage(status.message))
                    return
                }
            }
        }

        updateState { copy(isCreatingQuiz = false) }
        sendEffect(CreateQuizEffect.QuizGenerationFinished)
        sendEffect(CreateQuizEffect.ShowMessage("퀴즈 생성 상태 확인 시간이 초과됐어요."))
    }

    private companion object {
        const val GENERATION_POLL_INTERVAL_MILLIS = 1_000L
        const val GENERATION_POLL_MAX_ATTEMPTS = 120
    }
}

private fun QuizSubjectSummary.toUiModel(): QuizSubjectUiModel = QuizSubjectUiModel(
    id = id,
    name = name,
    chapters = emptyList(),
    chapterCountOverride = chapterCount,
    partCountOverride = partCount,
)

private fun QuizScope.toUiModel(summary: QuizSubjectUiModel): QuizSubjectUiModel = QuizSubjectUiModel(
    id = subjectId,
    name = subjectName.ifBlank { summary.name },
    chapters = chapters
        .sortedBy { chapter -> chapter.displayOrder }
        .map { chapter ->
            QuizScopeChapterUiModel(
                id = chapter.id,
                title = chapter.name,
                chapterNumber = chapter.displayOrder,
                parts = chapter.parts
                    .sortedBy { part -> part.partNumber }
                    .map { part ->
                        QuizScopePartUiModel(
                            id = part.id,
                            title = part.name,
                        )
                    },
            )
        },
    chapterCountOverride = null,
    partCountOverride = null,
)
