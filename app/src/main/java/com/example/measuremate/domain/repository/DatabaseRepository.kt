package com.example.measuremate.domain.repository

import com.example.measuremate.domain.model.BodyPart
import com.example.measuremate.domain.model.BodyPartValue
import com.example.measuremate.domain.model.User
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getSignedInUser(): Flow<User?>
    suspend fun addUser(): Result<Boolean>
    fun getBodyPart(bodyPartId: String): Flow<BodyPart?>
    fun getAllBodyParts(): Flow<List<BodyPart>>
    fun getAllBodyPartsWithLatestValue(): Flow<List<BodyPart>>
    fun getAllBodyPartValues(bodyPartId: String): Flow<List<BodyPartValue>>
    suspend fun upsertBodyPart(bodyPart: BodyPart): Result<Boolean>
    suspend fun deleteBodyPart(bodyPartId: String): Result<Boolean>
    suspend fun upsertBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean>
    suspend fun deleteBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean>
}