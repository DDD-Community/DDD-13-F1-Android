package com.f1.quiket.feature.mypage.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.f1.quiket.core.designsystem.theme.Orange100
import com.f1.quiket.core.designsystem.theme.QuiketTheme
import com.f1.quiket.core.designsystem.theme.White
import com.f1.quiket.feature.mypage.component.AcornShopButton
import com.f1.quiket.feature.mypage.component.CharacterRoomSection
import com.f1.quiket.feature.mypage.component.LevelProfileSection
import com.f1.quiket.feature.mypage.component.MyPageTopBar
import com.f1.quiket.feature.mypage.component.StatsRow
import com.f1.quiket.feature.mypage.data.model.CharacterLevel
import com.f1.quiket.feature.mypage.data.model.UserProfile

@Composable
fun MyPageScreen(
    state: MyPageState,
    onIntent: (MyPageIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState()),
    ) {
        MyPageTopBar(
            onSettingsClick = { onIntent(MyPageIntent.NavigateToSettings) },
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Orange100),
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            LevelProfileSection(
                nickname = state.profile.nickname,
                level = state.characterLevel.level,
                progress = state.characterLevel.progressIn(state.totalQuizCount),
                totalQuizCount = state.totalQuizCount,
            )

            CharacterRoomSection(
                level = state.characterLevel.level,
                unlockedItems = state.unlockedRoomItems,
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        StatsRow(
            acornCount = state.acornCount,
            streakDays = state.streakDays,
        )

        Spacer(modifier = Modifier.height(16.dp))

        AcornShopButton(
            onClick = { onIntent(MyPageIntent.NavigateToAcornShop) },
            isLocked = true
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "MyPage — 기본")
@Composable
private fun MyPageScreenPreview() {
    QuiketTheme {
        MyPageScreen(
            state = MyPageState(
                isLoading = false,
                profile = UserProfile(nickname = "큐링이"),
                characterLevel = CharacterLevel.STUDIOUS,
                totalQuizCount = 20,
                acornCount = 42,
                streakDays = 7,
                unlockedRoomItems = setOf(RoomItem.BASIC_ROOM),
            ),
            onIntent = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "MyPage — Lv3 아이템 해금")
@Composable
private fun MyPageScreenLv3Preview() {
    QuiketTheme {
        MyPageScreen(
            state = MyPageState(
                isLoading = false,
                profile = UserProfile(nickname = "큐링큐링"),
                characterLevel = CharacterLevel.STUDIOUS,
                totalQuizCount = 200,
                acornCount = 120,
                streakDays = 30,
                unlockedRoomItems = setOf(RoomItem.BASIC_ROOM, RoomItem.RUG, RoomItem.SOFA),
            ),
            onIntent = {},
        )
    }
}