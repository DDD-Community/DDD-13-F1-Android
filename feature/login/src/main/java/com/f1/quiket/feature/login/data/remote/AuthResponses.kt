package com.f1.quiket.feature.login.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class AuthTokenDataResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val accessTokenExpiresIn: Long,
    val refreshTokenExpiresIn: Long,
    val user: AuthUserResponse,
)

@Serializable
data class AuthUserResponse(
    val id: String,
    val email: String,
    val nickname: String,
    val dotoriBalance: Int,
    val emailVerified: Boolean,
    val status: String,
    val providers: List<String>,
)

@Serializable
data class SignupDataResponse(
    val userId: String,
    val email: String,
    val nickname: String,
    val emailVerificationRequired: Boolean,
    val emailVerificationSent: Boolean,
)

@Serializable
data class EmailAvailabilityDataResponse(
    val email: String,
    val available: Boolean,
)

@Serializable
data class EmailVerificationSentDataResponse(
    val email: String,
    val expiresInSeconds: Long,
)

@Serializable
data class EmailVerificationConfirmDataResponse(
    val email: String,
    val verified: Boolean,
)

@Serializable
data class PasswordResetRequestedDataResponse(
    val email: String,
    val expiresInSeconds: Long,
)

@Serializable
data class KakaoAccountLinkRequiredDataResponse(
    val email: String,
    val provider: String,
    val linkToken: String,
    val expiresInSeconds: Long,
)

@Serializable
data class KakaoNicknameRequiredDataResponse(
    val signupToken: String,
    val provider: String,
    val suggestedNickname: String? = null,
)

@Serializable
data class FieldErrorResponse(
    val field: String? = null,
    val reason: String? = null,
)

@Serializable
data class ErrorDataResponse(
    val fieldErrors: List<FieldErrorResponse> = emptyList(),
)
