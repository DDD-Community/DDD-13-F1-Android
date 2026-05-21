package com.f1.quiket.core.network.auth

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

@Singleton
class AuthHeaderInterceptor @Inject constructor(
    private val tokenStore: AuthTokenStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requiresAuth = request.header(AuthenticatedRequest.HEADER_NAME) != null

        if (!requiresAuth) {
            return chain.proceed(request)
        }

        val tokenPair = runBlocking { tokenStore.getTokenPair() }
        val authenticatedRequest = request.newBuilder()
            .removeHeader(AuthenticatedRequest.HEADER_NAME)
            .apply {
                if (tokenPair != null) {
                    header(AUTHORIZATION_HEADER, tokenPair.toAuthorizationHeader())
                }
            }
            .build()

        return chain.proceed(authenticatedRequest)
    }

    private fun TokenPair.toAuthorizationHeader(): String = "$tokenType $accessToken"

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }
}
