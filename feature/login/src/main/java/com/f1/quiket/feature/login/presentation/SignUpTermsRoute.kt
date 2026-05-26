package com.f1.quiket.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpTermsRoute(
    onBackClick: () -> Unit,
    onSignupSubmitted: () -> Unit,
    viewModel: SignupTermsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SignupTermsEffect.NavigateToEmailVerification -> onSignupSubmitted()
                is SignupTermsEffect.ShowMessage -> context.showAuthToast(effect.message)
            }
        }
    }

    SignUpTermsScreen(
        termsState = state.termsState,
        onBackClick = onBackClick,
        onAllTermsClick = { viewModel.onIntent(SignupTermsIntent.ToggleAllTerms) },
        onServiceTermsClick = { viewModel.onIntent(SignupTermsIntent.ToggleServiceTerms) },
        onPrivacyTermsClick = { viewModel.onIntent(SignupTermsIntent.TogglePrivacyTerms) },
        onMarketingTermsClick = { viewModel.onIntent(SignupTermsIntent.ToggleMarketingTerms) },
        onSubmitClick = { viewModel.onIntent(SignupTermsIntent.Submit) },
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
