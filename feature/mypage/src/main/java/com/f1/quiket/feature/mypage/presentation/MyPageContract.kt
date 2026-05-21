package com.f1.quiket.feature.mypage.presentation

import com.f1.quiket.core.common.mvi.UiEffect
import com.f1.quiket.core.common.mvi.UiIntent
import com.f1.quiket.core.common.mvi.UiState
import com.f1.quiket.feature.mypage.data.model.UserProfile
import com.f1.quiket.feature.mypage.data.model.CharacterLevel

data class MyPageState(
    val isLoading: Boolean = true,
    val profile: UserProfile = UserProfile(),
    val characterLevel: CharacterLevel = CharacterLevel.FIRST_STEP,
    val totalQuizCount: Int = 0,
    val streakDays: Int = 0,
    val acornCount: Int = 0,
    val unlockedRoomItems: Set<RoomItem> = setOf(RoomItem.BASIC_ROOM),
): UiState

enum class RoomItem {
    BASIC_ROOM,
    RUG,
    SOFA,
    BOOKSHELF,
    FULL_ROOM,
}

sealed interface MyPageIntent: UiIntent {
    data object LoadProfile : MyPageIntent
    data object NavigateToSettings : MyPageIntent
    data object NavigateToAcornShop : MyPageIntent
}

sealed interface MyPageEffect: UiEffect {
    data object GoToSettings : MyPageEffect
    data object GoToAcornShop : MyPageEffect
    data class ShowSnackBar(val message: String) : MyPageEffect
}