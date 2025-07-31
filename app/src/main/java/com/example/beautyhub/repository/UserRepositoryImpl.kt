package com.example.beautyhub.repository

import com.example.beautyhub.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val userDb = FirebaseDatabase.getInstance().reference.child("users")

    override suspend fun register(email: String, password: String): Result<String> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        Result.success(result.user?.uid ?: throw Exception("User UID is null"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun login(email: String, password: String): Result<Unit> = try {
        auth.signInWithEmailAndPassword(email, password).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun sendResetPasswordEmail(email: String): Result<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun addUserToDatabase(user: UserModel): Result<Unit> = try {
        userDb.child(user.uid).setValue(user).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? = auth.currentUser?.uid
}
