package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

private const val MockDuplicateNickname = "퀴켓"
private const val DuplicateNicknameErrorMessage = "앗! 중복되는 닉네임이에요"
private val NicknameRegex = Regex(
    pattern = "^[A-Za-z가-힣 ]{2,12}$",
)

@Composable
fun SignUpNicknameRoute(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    var nickname by rememberSaveable { mutableStateOf("") }

    val trimmedNickname = nickname.trim()
    val nicknameErrorMessage = if (isDuplicateNickname(trimmedNickname)) {
        DuplicateNicknameErrorMessage
    } else {
        null
    }

    SignUpNicknameScreen(
        nickname = nickname,
        nicknameErrorMessage = nicknameErrorMessage,
        isNextEnabled = isValidNickname(trimmedNickname) && nicknameErrorMessage == null,
        onNicknameChange = {
            nickname = it.take(SignUpNicknameMaxLength)
        },
        onBackClick = onBackClick,
        onNextClick = onNextClick,
    )
}

private const val SignUpNicknameMaxLength = 12

private fun isValidNickname(nickname: String): Boolean =
    nickname.matches(NicknameRegex)

private fun isDuplicateNickname(nickname: String): Boolean =
    nickname == MockDuplicateNickname
