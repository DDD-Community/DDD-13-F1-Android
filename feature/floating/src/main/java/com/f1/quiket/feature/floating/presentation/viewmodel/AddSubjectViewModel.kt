package com.f1.quiket.feature.floating.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1.quiket.feature.floating.presentation.contract.AddSubjectContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSubjectViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AddSubjectContract.State())
    val state: StateFlow<AddSubjectContract.State> = _state.asStateFlow()

    private val _effect = Channel<AddSubjectContract.Effect>()
    val effect = _effect.receiveAsFlow()

    fun handleIntent(intent: AddSubjectContract.Intent) {
        when (intent) {
            is AddSubjectContract.Intent.UpdateSubjectName ->
                _state.update { it.copy(subjectName = intent.name) }

            is AddSubjectContract.Intent.SelectStudyPurpose ->
                _state.update { it.copy(studyPurpose = intent.purpose) }

            is AddSubjectContract.Intent.SelectExamType ->
                _state.update { it.copy(examType = intent.type) }

            is AddSubjectContract.Intent.SelectStudyField ->
                _state.update { it.copy(studyField = intent.field) }

            is AddSubjectContract.Intent.SelectUsagePurpose ->
                _state.update { it.copy(usagePurpose = intent.purpose) }

            AddSubjectContract.Intent.GoToNextStep ->
                _state.update { it.copy(currentStep = (it.currentStep + 1).coerceAtMost(3)) }

            AddSubjectContract.Intent.GoToPreviousStep ->
                _state.update { it.copy(currentStep = (it.currentStep - 1).coerceAtLeast(1)) }

            AddSubjectContract.Intent.Skip ->
                viewModelScope.launch { _effect.send(AddSubjectContract.Effect.NavigateToSuccess) }

            AddSubjectContract.Intent.Finish ->
                viewModelScope.launch { _effect.send(AddSubjectContract.Effect.NavigateToSuccess) }
        }
    }
}