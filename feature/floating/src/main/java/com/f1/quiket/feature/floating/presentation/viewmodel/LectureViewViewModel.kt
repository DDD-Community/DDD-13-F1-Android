package com.f1.quiket.feature.floating.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1.quiket.core.network.model.NetworkResult
import com.f1.quiket.feature.floating.domain.model.TocChapter
import com.f1.quiket.feature.floating.domain.model.TocPart
import com.f1.quiket.feature.floating.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LectureViewViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
) : ViewModel() {

    private val _tocChapters = MutableStateFlow<List<TocChapter>>(emptyList())
    val tocChapters: StateFlow<List<TocChapter>> = _tocChapters.asStateFlow()

    // Flat ordered list of all part IDs for prev/next navigation
    private val _allPartIds = MutableStateFlow<List<String>>(emptyList())
    val allPartIds: StateFlow<List<String>> = _allPartIds.asStateFlow()

    private val _currentPartId = MutableStateFlow<String?>(null)
    val currentPartId: StateFlow<String?> = _currentPartId.asStateFlow()

    private val _currentPartName = MutableStateFlow<String?>(null)
    val currentPartName: StateFlow<String?> = _currentPartName.asStateFlow()

    private val _currentPartContent = MutableStateFlow<String?>(null)
    val currentPartContent: StateFlow<String?> = _currentPartContent.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isPartLoading = MutableStateFlow(false)
    val isPartLoading: StateFlow<Boolean> = _isPartLoading.asStateFlow()

    fun loadSubject(subjectId: String, initialChapterId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = subjectRepository.getSubject(subjectId)) {
                is NetworkResult.Success -> {
                    val detail = result.data

                    _tocChapters.value = detail.chapters.map { chapter ->
                        TocChapter(
                            id = chapter.id,
                            number = chapter.displayOrder,
                            title = chapter.name,
                            parts = chapter.parts.map { part ->
                                TocPart(id = part.id, title = part.name)
                            },
                        )
                    }

                    val allPartIds = detail.chapters.flatMap { chapter ->
                        chapter.parts.map { it.id }
                    }
                    _allPartIds.value = allPartIds

                    // Start at first part of initial chapter, fallback to first part overall
                    val initialPartId = detail.chapters
                        .firstOrNull { it.id == initialChapterId }
                        ?.parts?.firstOrNull()?.id
                        ?: allPartIds.firstOrNull()

                    initialPartId?.let { selectPart(it) }
                }
                is NetworkResult.Failure -> {}
            }
            _isLoading.value = false
        }
    }

    fun selectPart(partId: String) {
        if (_currentPartId.value == partId) return
        _currentPartId.value = partId
        viewModelScope.launch {
            _isPartLoading.value = true
            when (val result = subjectRepository.getPart(partId)) {
                is NetworkResult.Success -> {
                    _currentPartName.value = result.data.name
                    _currentPartContent.value = result.data.content
                }
                is NetworkResult.Failure -> {}
            }
            _isPartLoading.value = false
        }
    }

    fun updatePartName(partId: String, name: String) {
        viewModelScope.launch {
            val content = _currentPartContent.value ?: ""
            when (val result = subjectRepository.updatePart(partId, name, content)) {
                is NetworkResult.Success -> {
                    _currentPartName.value = result.data.name
                    _tocChapters.value = _tocChapters.value.map { chapter ->
                        chapter.copy(
                            parts = chapter.parts.map { part ->
                                if (part.id == partId) part.copy(title = name) else part
                            }
                        )
                    }
                }
                is NetworkResult.Failure -> {}
            }
        }
    }
}
