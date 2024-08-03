package com.example.measuremate.domain.repository

interface AuthRepository {
    suspend fun signInAnonymously(): Result<Boolean>
}