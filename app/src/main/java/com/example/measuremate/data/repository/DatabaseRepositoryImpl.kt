package com.example.measuremate.data.repository

import com.example.measuremate.data.mapper.BodyPartDto
import com.example.measuremate.data.mapper.BodyPartValueDto
import com.example.measuremate.data.mapper.UserDto
import com.example.measuremate.data.mapper.toBodyPart
import com.example.measuremate.data.mapper.toBodyPartDto
import com.example.measuremate.data.mapper.toBodyPartValue
import com.example.measuremate.data.mapper.toBodyPartValueDto
import com.example.measuremate.data.mapper.toUser
import com.example.measuremate.data.util.Constants.BODY_PART_COLLECTION
import com.example.measuremate.data.util.Constants.BODY_PART_NAME_FIELD
import com.example.measuremate.data.util.Constants.BODY_PART_VALUE_COLLECTION
import com.example.measuremate.data.util.Constants.BODY_PART_VALUE_DATE_FIELD
import com.example.measuremate.data.util.Constants.USER_COLLECTION
import com.example.measuremate.domain.model.BodyPart
import com.example.measuremate.domain.model.BodyPartValue
import com.example.measuremate.domain.model.User
import com.example.measuremate.domain.repository.DatabaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
): DatabaseRepository {

    private fun userCollection(): CollectionReference {
        return firebaseFirestore
            .collection(USER_COLLECTION)
    }

    private fun bodyPartCollection(
        userId: String = firebaseAuth.currentUser?.uid.orEmpty()
    ): CollectionReference {
        return firebaseFirestore
            .collection(USER_COLLECTION)
            .document(userId)
            .collection(BODY_PART_COLLECTION)
    }

    private fun bodyPartValueCollection(
        bodyPartId: String,
        userId: String = firebaseAuth.currentUser?.uid.orEmpty()
    ): CollectionReference {
        return firebaseFirestore
            .collection(USER_COLLECTION)
            .document(userId)
            .collection(BODY_PART_COLLECTION)
            .document(bodyPartId)
            .collection(BODY_PART_VALUE_COLLECTION)
    }

    override suspend fun addUser(): Result<Boolean> {
        return try {
            val firebaseUser = firebaseAuth.currentUser
                ?: throw IllegalArgumentException("No current user logged in.")
            var userDto = UserDto(
                userId = firebaseUser.uid,
                anonymous = firebaseUser.isAnonymous
            )
            firebaseUser.providerData.forEach { profile ->
                userDto = userDto.copy(
                    name = profile.displayName ?: userDto.name,
                    email = profile.email ?: userDto.email,
                    profilePictureUrl = profile.photoUrl?.toString() ?: userDto.profilePictureUrl
                )
            }
            userCollection()
                .document(firebaseUser.uid)
                .set(userDto)
                .await()
            Result.success(value = true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getSignedInUser(): Flow<User?> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid.orEmpty()
                userCollection()
                    .document(userId)
                    .snapshots()
                    .collect { snapshot ->
                        val userDto = snapshot.toObject(UserDto::class.java)
                        emit(userDto?.toUser())
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getBodyPart(bodyPartId: String): Flow<BodyPart?> {
        return flow {
            try {
                bodyPartCollection()
                    .document(bodyPartId)
                    .snapshots()
                    .collect { snapshot ->
                        val bodyPartDto = snapshot.toObject(BodyPartDto::class.java)
                        emit(bodyPartDto?.toBodyPart())
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getAllBodyParts(): Flow<List<BodyPart>> {
        return flow {
            try {
                bodyPartCollection()
                    .orderBy(BODY_PART_NAME_FIELD)
                    .snapshots()
                    .collect { snapshot ->
                        val bodyPartDtos = snapshot.toObjects(BodyPartDto::class.java)
                        emit(bodyPartDtos.map { it.toBodyPart() })
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getAllBodyPartsWithLatestValue(): Flow<List<BodyPart>> {
        return flow {
            try {
                bodyPartCollection()
                    .orderBy(BODY_PART_NAME_FIELD)
                    .snapshots()
                    .collect { snapshot ->
                        val bodyPartDtos = snapshot.toObjects(BodyPartDto::class.java)
                        val bodyParts = bodyPartDtos.mapNotNull { bodyPartDto ->
                            bodyPartDto.bodyPartId?.let { bodyPartId ->
                                val latestValue = getLatestBodyPartValue(bodyPartId)
                                bodyPartDto.copy(latestValue = latestValue?.value).toBodyPart()
                            }
                        }
                        emit(bodyParts)
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private suspend fun getLatestBodyPartValue(bodyPartId: String): BodyPartValueDto? {
        val querySnapshot = bodyPartValueCollection(bodyPartId)
            .orderBy(BODY_PART_VALUE_DATE_FIELD, Query.Direction.DESCENDING)
            .limit(1)
            .snapshots()
            .firstOrNull()
        return querySnapshot?.documents?.firstOrNull()?.toObject(BodyPartValueDto::class.java)
    }

    override fun getAllBodyPartValues(bodyPartId: String): Flow<List<BodyPartValue>> {
        return flow {
            try {
                bodyPartValueCollection(bodyPartId)
                    .orderBy(BODY_PART_VALUE_DATE_FIELD, Query.Direction.DESCENDING)
                    .snapshots()
                    .collect { snapshot ->
                        val bodyPartValueDto = snapshot.toObjects(BodyPartValueDto::class.java)
                        emit(bodyPartValueDto.map { it.toBodyPartValue() })
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun upsertBodyPart(bodyPart: BodyPart): Result<Boolean> {
        return try {
            val documentId = bodyPart.bodyPartId ?: bodyPartCollection().document().id
            val bodyPartDto = bodyPart.toBodyPartDto().copy(bodyPartId = documentId)
            bodyPartCollection()
                .document(documentId)
                .set(bodyPartDto)
                .await()
            Result.success(value = true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBodyPart(bodyPartId: String): Result<Boolean> {
        return try {
            bodyPartCollection()
                .document(bodyPartId)
                .delete()
                .await()
            Result.success(value = true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun upsertBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean> {
        return try {
            val bodyPartValueCollection = bodyPartValueCollection(bodyPartValue.bodyPartId.orEmpty())
            val documentId = bodyPartValue.bodyPartValueId ?: bodyPartValueCollection.document().id
            val bodyPartValueDto = bodyPartValue.toBodyPartValueDto().copy(bodyPartValueId = documentId)

            bodyPartValueCollection
                .document(documentId)
                .set(bodyPartValueDto)
                .await()
            Result.success(value = true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean> {
        return try {
            val bodyPartId = bodyPartValue.bodyPartId.orEmpty()
            val bodyPartValueId = bodyPartValue.bodyPartValueId.orEmpty()
            bodyPartValueCollection(bodyPartId)
                .document(bodyPartValueId)
                .delete()
                .await()
            Result.success(value = true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}




