package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

private const val MockWrongPassword = "wrong"

@Composable
fun LoginEmailRoute(
    onBackClick: () -> Unit,
    onLoginClick: (email: String, password: String) -> Unit,
    onForgotPasswordClick: () -> Unit = {},
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var failedLoginCount by rememberSaveable { mutableStateOf(0) }
    var showPasswordResetRequiredDialog by rememberSaveable { mutableStateOf(false) }
    var shouldShowEmailFormatError by rememberSaveable { mutableStateOf(false) }

    val trimmedEmail = email.trim()
    val emailErrorMessage = if (shouldShowEmailFormatError && !isValidEmail(trimmedEmail)) {
        EmailFormatErrorMessage
    } else {
        null
    }
    val passwordErrorMessage = if (failedLoginCount > 0) {
        "비밀번호를 다시 입력해주세요 ($failedLoginCount/5)"
    } else {
        null
    }

    LoginEmailScreen(
        email = email,
        password = password,
        isPasswordVisible = isPasswordVisible,
        isLoginEnabled = email.isNotBlank() && password.isNotBlank(),
        emailErrorMessage = emailErrorMessage,
        passwordErrorMessage = passwordErrorMessage,
        showPasswordResetRequiredDialog = showPasswordResetRequiredDialog,
        onEmailChange = {
            email = it
            failedLoginCount = 0
            shouldShowEmailFormatError = false
        },
        onPasswordChange = {
            password = it
            failedLoginCount = 0
        },
        onPasswordVisibilityClick = { isPasswordVisible = !isPasswordVisible },
        onBackClick = onBackClick,
        onForgotPasswordClick = onForgotPasswordClick,
        onLoginClick = {
            if (!isValidEmail(trimmedEmail)) {
                shouldShowEmailFormatError = true
            } else if (password.equals(MockWrongPassword, ignoreCase = true)) {
                // TODO: Replace this temporary UI simulation with Auth API result handling.
                val nextFailedLoginCount = (failedLoginCount + 1).coerceAtMost(5)
                failedLoginCount = nextFailedLoginCount
                if (nextFailedLoginCount >= 5) {
                    showPasswordResetRequiredDialog = true
                }
            } else {
                onLoginClick(trimmedEmail, password)
            }
        },
        onPasswordResetRequiredClick = {
            showPasswordResetRequiredDialog = false
            onForgotPasswordClick()
        },
        onPasswordResetRequiredDismiss = {
            showPasswordResetRequiredDialog = false
        },
    )
}
