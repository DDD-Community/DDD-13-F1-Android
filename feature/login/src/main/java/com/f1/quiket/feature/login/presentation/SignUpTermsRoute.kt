package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable

@Composable
fun SignUpTermsRoute(
    onBackClick: () -> Unit,
) {
    SignUpTermsScreen(
        termsState = SignUpTermsState(),
        onBackClick = onBackClick,
    )
}

data class SignUpTermsState(
    val serviceTermsAgreed: Boolean = false,
    val privacyTermsAgreed: Boolean = false,
    val marketingTermsAgreed: Boolean = false,
) {
    val allAgreed: Boolean
        get() = serviceTermsAgreed && privacyTermsAgreed && marketingTermsAgreed

    val requiredAgreed: Boolean
        get() = serviceTermsAgreed && privacyTermsAgreed
}
