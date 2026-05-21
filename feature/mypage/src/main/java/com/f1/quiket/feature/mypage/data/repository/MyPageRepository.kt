package com.f1.quiket.feature.mypage.data.repository

import com.f1.quiket.feature.mypage.data.model.UserProfile
import javax.inject.Inject

interface MyPageRepository {
    suspend fun getUserProfile(): UserProfile
}

class MyPageRepositoryImpl @Inject constructor() : MyPageRepository {
    override suspend fun getUserProfile(): UserProfile = UserProfile(
        userId = "user_0405",
        nickname = "User0405",
        totalQuizCount = 63,
        streakDays = 10,
        acornCount = 63,
    )
}