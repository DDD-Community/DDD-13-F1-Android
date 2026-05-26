package com.f1.quiket.core.network.di

import android.util.Log
import com.f1.quiket.core.network.BuildConfig
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
interface NetworkInterceptorModule {
    @Multibinds
    fun bindInterceptors(): Set<Interceptor>
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptors: Set<@JvmSuppressWildcards Interceptor>,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        interceptors.forEach(builder::addInterceptor)

        builder.addInterceptor(provideLoggingInterceptor())

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.QUIKET_API_BASE_URL.ensureTrailingSlash())
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private fun String.ensureTrailingSlash(): String =
        if (endsWith("/")) this else "$this/"

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(RedactingHttpLogger()).apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            redactHeader("Authorization")
            redactHeader("Cookie")
            redactHeader("Set-Cookie")
            redactHeader("X-Device-Id")
            redactHeader("X-Device-Name")
        }

    private class RedactingHttpLogger : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.d("QuiketNetwork", message.redactSensitiveValues())
        }

        private fun String.redactSensitiveValues(): String =
            replace(SensitiveJsonValueRegex) { result ->
                "${result.groupValues[1]}\"██\""
            }.replace(SensitiveFormValueRegex) { result ->
                "${result.groupValues[1]}██"
            }

        private companion object {
            private val SensitiveJsonValueRegex = Regex(
                pattern = """("(?i:password|passwordConfirm|newPassword|currentPassword|verificationCode|accessToken|refreshToken|idToken|oauthAccessToken|kakaoAccessToken|token)"\s*:\s*)"[^"]*"""",
            )
            private val SensitiveFormValueRegex = Regex(
                pattern = """((?i:password|passwordConfirm|newPassword|currentPassword|verificationCode|accessToken|refreshToken|idToken|oauthAccessToken|kakaoAccessToken|token)=)[^&\s]+""",
            )
        }
    }
}
