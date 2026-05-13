package com.f1.quiket.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.f1.quiket.core.database.datastore.OnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val onboardingUseCase: OnboardingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    // SideEffect 처리를 위한 Channel
    private val _sideEffect = Channel<HomeSideEffect>()
    val sideEffect: Flow<HomeSideEffect> = _sideEffect.receiveAsFlow()

    init {
        observeOnboarding()
    }

    private fun observeOnboarding() {
        viewModelScope.launch {
            onboardingUseCase.getFirstLaunch()
                .collect { isFirstLaunch ->
                    _uiState.update { it.copy(
                        showOnboarding = isFirstLaunch,
                        isLoading = false
                    )}
                }
        }
    }

    fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadHomeData -> { /* 데이터 로딩 로직 */ }
            is HomeIntent.OnboardingDoneClick -> {
                completeOnboarding()
            }
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            onboardingUseCase.setDone()
            _uiState.update {
                it.copy(showOnboarding = false)
            }

            _sideEffect.send(
                HomeSideEffect.ShowToast("Quiket에 오신 것을 환영합니다 !")
            )
        }
    }
}