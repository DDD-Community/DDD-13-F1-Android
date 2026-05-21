package com.f1.quiket.feature.mypage.di

import com.f1.quiket.feature.mypage.data.repository.MyPageRepository
import com.f1.quiket.feature.mypage.data.repository.MyPageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MyPageModule {

    @Binds
    @Singleton
    abstract fun bindMyPageRepository(
        impl: MyPageRepositoryImpl,
    ): MyPageRepository
}
