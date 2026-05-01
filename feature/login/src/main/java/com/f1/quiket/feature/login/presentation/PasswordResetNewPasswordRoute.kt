package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

private const val EmailVerifiedMessageDurationMillis = 2_500L
private const val PasswordConfirmMismatchMessage = "비밀번호가 일치하지 않아요"
private val ResetPasswordRegex = Regex(
    pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,64}$",
)

@Composable
fun PasswordResetNewPasswordRoute(
    onCloseClick: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isPasswordConfirmVisible by rememberSaveable { mutableStateOf(false) }
    var showEmailVerifiedMessage by rememberSaveable { mutableStateOf(true) }

    val passwordConfirmErrorMessage = if (
        passwordConfirm.isNotBlank() &&
        passwordConfirm != password
    ) {
        PasswordConfirmMismatchMessage
    } else {
        null
    }
    val isCompleteEnabled = isValidResetPassword(password) &&
        passwordConfirm.isNotBlank() &&
        passwordConfirmErrorMessage == null

    LaunchedEffect(Unit) {
        delay(EmailVerifiedMessageDurationMillis)
        showEmailVerifiedMessage = false
    }

    PasswordResetNewPasswordScreen(
        password = password,
        passwordConfirm = passwordConfirm,
        isPasswordVisible = isPasswordVisible,
        isPasswordConfirmVisible = isPasswordConfirmVisible,
        passwordConfirmErrorMessage = passwordConfirmErrorMessage,
        isCompleteEnabled = isCompleteEnabled,
        showEmailVerifiedMessage = showEmailVerifiedMessage,
        onPasswordChange = { password = it },
        onPasswordConfirmChange = { passwordConfirm = it },
        onPasswordVisibilityClick = { isPasswordVisible = !isPasswordVisible },
        onPasswordConfirmVisibilityClick = { isPasswordConfirmVisible = !isPasswordConfirmVisible },
        onCloseClick = onCloseClick,
        onCompleteClick = onCompleteClick,
    )
}

private fun isValidResetPassword(password: String): Boolean =
    password.matches(ResetPasswordRegex)
