package com.example.measuremate.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.example.measuremate.data.repository.AuthRepositoryImpl
import com.example.measuremate.data.repository.DatabaseRepositoryImpl
import com.example.measuremate.domain.repository.AuthRepository
import com.example.measuremate.domain.repository.DatabaseRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext context: Context
    ): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        credentialManager: CredentialManager
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, credentialManager)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        firebaseAuth: FirebaseAuth
    ): DatabaseRepository {
        return DatabaseRepositoryImpl(firebaseAuth)
    }
}