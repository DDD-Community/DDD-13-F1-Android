package com.f1.quiket.feature.floating.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.feature.floating.domain.model.SubjectDetail
import com.f1.quiket.feature.floating.domain.model.SubjectExamSchedule
import com.f1.quiket.feature.floating.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubjectDetailViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
) : ViewModel() {

    private val _subjectDetail = MutableStateFlow<SubjectDetail?>(null)
    val subjectDetail: StateFlow<SubjectDetail?> = _subjectDetail.asStateFlow()

    private val _examSchedule = MutableStateFlow<SubjectExamSchedule?>(null)
    val examSchedule: StateFlow<SubjectExamSchedule?> = _examSchedule.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // API 성공 후 홈 화면 새로고침 트리거
    private val _examScheduleSaved = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val examScheduleSaved: SharedFlow<Unit> = _examScheduleSaved.asSharedFlow()

    fun loadSubject(subjectId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = subjectRepository.getSubject(subjectId)) {
                is NetworkResult.Success -> _subjectDetail.value = result.data
                is NetworkResult.Failure -> {}
            }
            _isLoading.value = false
        }
    }

    fun updateSubjectName(subjectId: String, name: String) {
        viewModelScope.launch {
            subjectRepository.updateSubjectName(subjectId, name)
        }
    }

    fun updateChapterName(chapterId: String, name: String) {
        viewModelScope.launch {
            when (val result = subjectRepository.updateChapterName(chapterId, name)) {
                is NetworkResult.Success -> {
                    _subjectDetail.value = _subjectDetail.value?.let { detail ->
                        detail.copy(
                            chapters = detail.chapters.map { chapter ->
                                if (chapter.id == chapterId) chapter.copy(name = name) else chapter
                            },
                        )
                    }
                }
                is NetworkResult.Failure -> {}
            }
        }
    }

    fun upsertExamSchedule(subjectId: String, examName: String?, examDate: String) {
        viewModelScope.launch {
            when (val result = subjectRepository.upsertExamSchedule(subjectId, examName, examDate)) {
                is NetworkResult.Success -> {
                    _examSchedule.value = result.data
                    _examScheduleSaved.emit(Unit)
                }
                is NetworkResult.Failure -> {}
            }
        }
    }
}