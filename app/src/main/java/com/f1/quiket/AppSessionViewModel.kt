package com.f1.quiket

import androidx.lifecycle.ViewModel
import com.f1.quiket.core.network.auth.AuthTokenStore
import com.f1.quiket.feature.login.domain.model.AuthResult
import com.f1.quiket.feature.login.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppSessionViewModel @Inject constructor(
    private val authTokenStore: AuthTokenStore,
    private val authRepository: AuthRepository,
) : ViewModel() {
    suspend fun hasValidSession(): Boolean {
        authTokenStore.getTokenPair() ?: return false

        return when (val result = authRepository.getMe()) {
            is AuthResult.Success -> true
            is AuthResult.Failure -> {
                if (result.isUnauthorized()) {
                    authTokenStore.clear()
                }
                false
            }
        }
    }

    suspend fun logout() {
        val result = authRepository.logout()
        if (result is AuthResult.Failure) {
            authTokenStore.clear()
        }
    }

    private fun AuthResult.Failure.isUnauthorized(): Boolean =
        httpCode == HTTP_UNAUTHORIZED || code.contains("UNAUTHORIZED", ignoreCase = true)

    private companion object {
        const val HTTP_UNAUTHORIZED = 401
    }
}
