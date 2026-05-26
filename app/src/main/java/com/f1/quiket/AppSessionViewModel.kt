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
    suspend fun hasSavedToken(): Boolean = authTokenStore.getTokenPair() != null

    suspend fun logout() {
        val result = authRepository.logout()
        if (result is AuthResult.Failure) {
            authTokenStore.clear()
        }
    }
}
