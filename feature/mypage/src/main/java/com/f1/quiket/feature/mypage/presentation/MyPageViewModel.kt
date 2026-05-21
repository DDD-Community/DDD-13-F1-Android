package com.f1.quiket.feature.mypage.presentation

import androidx.lifecycle.viewModelScope
import com.f1.quiket.core.common.mvi.MviViewModel
import com.f1.quiket.feature.mypage.data.model.CharacterLevel
import com.f1.quiket.feature.mypage.data.repository.MyPageRepository
import com.f1.quiket.feature.mypage.presentation.MyPageIntent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val repository: MyPageRepository,
) : MviViewModel<MyPageState, MyPageIntent, MyPageEffect>(initialState = MyPageState()) {

    init {
        onIntent(LoadProfile)
    }

    override fun handleIntent(intent: MyPageIntent) {
        when (intent) {
            is LoadProfile -> loadProfile()
            is NavigateToSettings -> viewModelScope.launch { sendEffect(MyPageEffect.GoToSettings) }
            is NavigateToAcornShop -> viewModelScope.launch { sendEffect(MyPageEffect.GoToAcornShop) }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            runCatching { repository.getUserProfile() }
                .onSuccess { profile ->
                    val level = CharacterLevel.from(profile.totalQuizCount)
                    val unlockedItems = resolveUnlockedItems(level)
                    updateState {
                        copy(
                            isLoading = false,
                            profile = profile,
                            characterLevel = level,
                            totalQuizCount = profile.totalQuizCount,
                            streakDays = profile.streakDays,
                            acornCount = profile.acornCount,
                            unlockedRoomItems = unlockedItems,
                        )
                    }
                }
                .onFailure {
                    updateState { copy(isLoading = false) }
                    sendEffect(MyPageEffect.ShowSnackBar("프로필을 불러오지 못했어요."))
                }
        }
    }

    private fun resolveUnlockedItems(level: CharacterLevel): Set<RoomItem> = buildSet {
        add(RoomItem.BASIC_ROOM)
        if (level.level >= 2) add(RoomItem.RUG)
        if (level.level >= 3) add(RoomItem.SOFA)
        if (level.level >= 4) add(RoomItem.BOOKSHELF)
        if (level.level >= 5) add(RoomItem.FULL_ROOM)
    }
}