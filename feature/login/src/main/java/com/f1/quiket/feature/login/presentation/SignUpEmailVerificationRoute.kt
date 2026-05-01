package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

private const val SignUpVerificationTimeoutSeconds = 180
private const val MockValidSignUpVerificationCode = "123456"
private const val SignUpVerificationSentMessageDurationMillis = 2_500L
private const val SignUpEmailVerifiedMessageDurationMillis = 2_500L
private const val SignUpVerificationCodeMismatchErrorMessage = "인증번호가 일치하지 않아요"
private const val SignUpVerificationExpiredErrorMessage = "인증 시간이 만료되었어요"
private const val SignUpPasswordConfirmMismatchMessage = "비밀번호가 일치하지 않아요"
private val SignUpPasswordRegex = Regex(
    pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{8,64}$",
)

@Composable
fun SignUpEmailVerificationRoute(
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var verificationCode by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }
    var isVerificationRequested by rememberSaveable { mutableStateOf(false) }
    var isEmailVerified by rememberSaveable { mutableStateOf(false) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isPasswordConfirmVisible by rememberSaveable { mutableStateOf(false) }
    var verificationRequestCount by rememberSaveable { mutableStateOf(0) }
    var timerSeconds by rememberSaveable { mutableStateOf(SignUpVerificationTimeoutSeconds) }
    var showVerificationSentMessage by rememberSaveable { mutableStateOf(false) }
    var showEmailVerifiedMessage by rememberSaveable { mutableStateOf(false) }
    var shouldShowEmailFormatError by rememberSaveable { mutableStateOf(false) }
    var verificationCodeErrorMessage by rememberSaveable { mutableStateOf<String?>(null) }

    val trimmedEmail = email.trim()
    val emailErrorMessage = if (shouldShowEmailFormatError && !isValidEmail(trimmedEmail)) {
        EmailFormatErrorMessage
    } else {
        null
    }
    val passwordConfirmErrorMessage = if (
        passwordConfirm.isNotBlank() &&
        passwordConfirm != password
    ) {
        SignUpPasswordConfirmMismatchMessage
    } else {
        null
    }

    fun requestVerificationCode() {
        isVerificationRequested = true
        isEmailVerified = false
        verificationCode = ""
        verificationCodeErrorMessage = null
        password = ""
        passwordConfirm = ""
        isPasswordVisible = false
        isPasswordConfirmVisible = false
        showEmailVerifiedMessage = false
        timerSeconds = SignUpVerificationTimeoutSeconds
        verificationRequestCount += 1
        showVerificationSentMessage = true
    }

    LaunchedEffect(verificationRequestCount) {
        if (verificationRequestCount == 0) return@LaunchedEffect

        timerSeconds = SignUpVerificationTimeoutSeconds
        while (timerSeconds > 0 && isVerificationRequested && !isEmailVerified) {
            delay(1_000L)
            timerSeconds -= 1
        }
        if (timerSeconds == 0 && isVerificationRequested && !isEmailVerified) {
            verificationCodeErrorMessage = SignUpVerificationExpiredErrorMessage
        }
    }

    LaunchedEffect(showVerificationSentMessage) {
        if (!showVerificationSentMessage) return@LaunchedEffect

        delay(SignUpVerificationSentMessageDurationMillis)
        showVerificationSentMessage = false
    }

    LaunchedEffect(showEmailVerifiedMessage) {
        if (!showEmailVerifiedMessage) return@LaunchedEffect

        delay(SignUpEmailVerifiedMessageDurationMillis)
        showEmailVerifiedMessage = false
    }

    SignUpEmailVerificationScreen(
        email = email,
        verificationCode = verificationCode,
        password = password,
        passwordConfirm = passwordConfirm,
        timerText = timerSeconds.toTimerText(),
        isEmailVerificationRequested = isVerificationRequested,
        isEmailVerified = isEmailVerified,
        isPasswordVisible = isPasswordVisible,
        isPasswordConfirmVisible = isPasswordConfirmVisible,
        isEmailVerificationButtonEnabled = email.isNotBlank() && !isVerificationRequested,
        emailErrorMessage = emailErrorMessage,
        verificationCodeErrorMessage = verificationCodeErrorMessage,
        passwordConfirmErrorMessage = passwordConfirmErrorMessage,
        showVerificationSentMessage = showVerificationSentMessage,
        showEmailVerifiedMessage = showEmailVerifiedMessage,
        isNextEnabled = isEmailVerified &&
            isValidSignUpPassword(password) &&
            passwordConfirm.isNotBlank() &&
            passwordConfirmErrorMessage == null,
        onEmailChange = {
            email = it
            shouldShowEmailFormatError = false
            isVerificationRequested = false
            isEmailVerified = false
            verificationCode = ""
            verificationCodeErrorMessage = null
            password = ""
            passwordConfirm = ""
            isPasswordVisible = false
            isPasswordConfirmVisible = false
            showEmailVerifiedMessage = false
        },
        onVerificationCodeChange = { input ->
            verificationCode = input.filter(Char::isDigit).take(6)
            if (verificationCodeErrorMessage != SignUpVerificationExpiredErrorMessage) {
                verificationCodeErrorMessage = null
            }
        },
        onPasswordChange = {
            password = it
        },
        onPasswordConfirmChange = {
            passwordConfirm = it
        },
        onEmailVerificationRequestClick = {
            if (!isValidEmail(trimmedEmail)) {
                shouldShowEmailFormatError = true
            } else {
                requestVerificationCode()
            }
        },
        onCodeActionClick = {
            if (verificationCodeErrorMessage != null || verificationCode.length < 6) {
                requestVerificationCode()
            } else if (timerSeconds <= 0) {
                verificationCodeErrorMessage = SignUpVerificationExpiredErrorMessage
            } else if (verificationCode == MockValidSignUpVerificationCode) {
                isEmailVerified = true
                verificationCodeErrorMessage = null
                showVerificationSentMessage = false
                showEmailVerifiedMessage = true
            } else {
                verificationCodeErrorMessage = SignUpVerificationCodeMismatchErrorMessage
            }
        },
        onPasswordVisibilityClick = { isPasswordVisible = !isPasswordVisible },
        onPasswordConfirmVisibilityClick = { isPasswordConfirmVisible = !isPasswordConfirmVisible },
        onBackClick = onBackClick,
        onNextClick = onNextClick,
    )
}

private fun Int.toTimerText(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "%02d:%02d".format(minutes, seconds)
}

private fun isValidSignUpPassword(password: String): Boolean =
    password.matches(SignUpPasswordRegex)
