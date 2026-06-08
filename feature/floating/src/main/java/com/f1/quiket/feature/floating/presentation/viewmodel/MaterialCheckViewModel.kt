package com.f1.quiket.feature.floating.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.feature.floating.domain.model.PartSummary
import com.f1.quiket.feature.floating.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialCheckViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
) : ViewModel() {

    private val _chapterName = MutableStateFlow("")
    val chapterName: StateFlow<String> = _chapterName.asStateFlow()

    private val _parts = MutableStateFlow<List<PartSummary>>(emptyList())
    val parts: StateFlow<List<PartSummary>> = _parts.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var loadedSubjectId = ""
    private var loadedChapterId = ""

    fun load(subjectId: String, chapterId: String) {
        if (loadedSubjectId == subjectId && loadedChapterId == chapterId) return
        loadedSubjectId = subjectId
        loadedChapterId = chapterId
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = subjectRepository.getSubject(subjectId)) {
                is NetworkResult.Success -> {
                    val chapter = result.data.chapters.find { it.id == chapterId }
                    if (chapter != null) {
                        _chapterName.value = chapter.name
                        _parts.value = chapter.parts
                    }
                }
                is NetworkResult.Failure -> {}
            }
            _isLoading.value = false
        }
    }

    fun updateChapterName(newName: String) {
        viewModelScope.launch {
            when (val result = subjectRepository.updateChapterName(loadedChapterId, newName)) {
                is NetworkResult.Success -> _chapterName.value = result.data.name
                is NetworkResult.Failure -> {}
            }
        }
    }

    fun updatePartName(partId: String, newName: String) {
        viewModelScope.launch {
            when (val partResult = subjectRepository.getPart(partId)) {
                is NetworkResult.Success -> {
                    val content = partResult.data.content ?: ""
                    when (subjectRepository.updatePart(partId, newName, content)) {
                        is NetworkResult.Success -> {
                            _parts.value = _parts.value.map { part ->
                                if (part.id == partId) part.copy(name = newName) else part
                            }
                        }
                        is NetworkResult.Failure -> {}
                    }
                }
                is NetworkResult.Failure -> {}
            }
        }
    }
}