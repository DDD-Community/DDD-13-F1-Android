package com.f1.quiket.feature.home.presentation

data class HomeState(
    val isLoading: Boolean = true,
    val showOnboarding: Boolean = false
)

sealed interface HomeIntent {
    data object LoadHomeData : HomeIntent
    data object OnboardingDoneClick : HomeIntent
}

sealed interface HomeSideEffect {
    data class ShowToast(val message: String) : HomeSideEffect
}