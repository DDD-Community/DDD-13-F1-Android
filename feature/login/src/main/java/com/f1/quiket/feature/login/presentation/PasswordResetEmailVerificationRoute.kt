package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

private const val VerificationTimeoutSeconds = 180
private const val MockValidVerificationCode = "123456"
private const val VerificationCodeMismatchErrorMessage = "인증번호가 일치하지 않아요"
private const val VerificationExpiredErrorMessage = "인증 시간이 만료되었어요"

@Composable
fun PasswordResetEmailVerificationRoute(
    onCloseClick: () -> Unit,
    onVerificationComplete: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var verificationCode by rememberSaveable { mutableStateOf("") }
    var isVerificationRequested by rememberSaveable { mutableStateOf(false) }
    var isEmailVerified by rememberSaveable { mutableStateOf(false) }
    var isVerifyingCode by rememberSaveable { mutableStateOf(false) }
    var verificationRequestCount by rememberSaveable { mutableStateOf(0) }
    var timerSeconds by rememberSaveable { mutableStateOf(VerificationTimeoutSeconds) }
    var showVerificationSentMessage by rememberSaveable { mutableStateOf(false) }
    var shouldShowEmailFormatError by rememberSaveable { mutableStateOf(false) }
    var verificationCodeErrorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val trimmedEmail = email.trim()
    val emailErrorMessage = if (shouldShowEmailFormatError && !isValidEmail(trimmedEmail)) {
        EmailFormatErrorMessage
    } else {
        null
    }

    fun requestVerificationCode() {
        isVerificationRequested = true
        isEmailVerified = false
        isVerifyingCode = false
        verificationCode = ""
        verificationCodeErrorMessage = null
        timerSeconds = VerificationTimeoutSeconds
        verificationRequestCount += 1
        showVerificationSentMessage = true
    }

    LaunchedEffect(verificationRequestCount) {
        if (verificationRequestCount == 0) return@LaunchedEffect

        timerSeconds = VerificationTimeoutSeconds
        while (timerSeconds > 0 && isVerificationRequested && !isEmailVerified) {
            delay(1_000L)
            timerSeconds -= 1
        }
        if (timerSeconds == 0 && isVerificationRequested && !isEmailVerified) {
            isVerifyingCode = false
            verificationCodeErrorMessage = VerificationExpiredErrorMessage
        }
    }

    LaunchedEffect(showVerificationSentMessage) {
        if (!showVerificationSentMessage) return@LaunchedEffect

        delay(2_500L)
        showVerificationSentMessage = false
    }

    LaunchedEffect(isVerifyingCode) {
        if (!isVerifyingCode) return@LaunchedEffect

        delay(800L)
        isEmailVerified = true
        delay(900L)
        onVerificationComplete()
        isVerifyingCode = false
    }

    PasswordResetEmailVerificationScreen(
        email = email,
        verificationCode = verificationCode,
        timerText = timerSeconds.toTimerText(),
        isEmailVerificationRequested = isVerificationRequested,
        isEmailVerified = isEmailVerified,
        isEmailVerificationButtonEnabled = email.isNotBlank(),
        emailErrorMessage = emailErrorMessage,
        verificationCodeErrorMessage = verificationCodeErrorMessage,
        isCodeVerifying = isVerifyingCode,
        showVerificationSentMessage = showVerificationSentMessage,
        onEmailChange = {
            email = it
            shouldShowEmailFormatError = false
            isEmailVerified = false
            isVerificationRequested = false
            isVerifyingCode = false
            verificationCode = ""
            verificationCodeErrorMessage = null
        },
        onVerificationCodeChange = { input ->
            verificationCode = input.filter(Char::isDigit).take(6)
            if (verificationCodeErrorMessage != VerificationExpiredErrorMessage) {
                verificationCodeErrorMessage = null
            }
        },
        onEmailVerificationRequestClick = {
            if (!isValidEmail(trimmedEmail)) {
                shouldShowEmailFormatError = true
                isVerificationRequested = false
                isEmailVerified = false
                verificationCode = ""
                verificationCodeErrorMessage = null
            } else {
                requestVerificationCode()
            }
        },
        onCodeActionClick = {
            if (verificationCodeErrorMessage != null || verificationCode.length < 6) {
                requestVerificationCode()
            } else if (timerSeconds <= 0) {
                verificationCodeErrorMessage = VerificationExpiredErrorMessage
            } else if (verificationCode == MockValidVerificationCode) {
                isVerifyingCode = true
            } else {
                verificationCodeErrorMessage = VerificationCodeMismatchErrorMessage
            }
        },
        onCloseClick = onCloseClick,
    )
}

private fun Int.toTimerText(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}
