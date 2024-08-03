package com.example.measuremate.di

import com.example.measuremate.data.repository.AuthRepositoryImpl
import com.example.measuremate.data.repository.DatabaseRepositoryImpl
import com.example.measuremate.domain.repository.AuthRepository
import com.example.measuremate.domain.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(): DatabaseRepository {
        return DatabaseRepositoryImpl()
    }
}