package com.example.measuremate.data.repository

import com.example.measuremate.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth

class DatabaseRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
): DatabaseRepository {

}