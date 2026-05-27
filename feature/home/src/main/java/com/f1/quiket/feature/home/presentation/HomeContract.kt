package com.f1.quiket.feature.home.presentation

import com.f1.quiket.feature.home.domain.model.HomeData

data class HomeState(
    val isLoading: Boolean = true,
    val showOnboarding: Boolean = false,
    val hasSubjects: Boolean = false,
    val homeData: HomeData? = null,
    val errorMessage: String? = null,
)

sealed interface HomeIntent {
    data object LoadHomeData : HomeIntent
    data object OnboardingDoneClick : HomeIntent
}

sealed interface HomeSideEffect {
    data class ShowToast(val message: String) : HomeSideEffect
}
