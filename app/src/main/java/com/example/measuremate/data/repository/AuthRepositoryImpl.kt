package com.example.measuremate.data.repository

import com.example.measuremate.domain.repository.AuthRepository

class AuthRepositoryImpl: AuthRepository {

    override suspend fun signInAnonymously(): Result<Boolean> {
        return try {
            Result.success(value = true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}