package com.example.beautyhub.repository
import com.example.beautyhub.model.UserModel

interface UserRepository {
    suspend fun register(email: String, password: String): Result<String>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun sendResetPasswordEmail(email: String): Result<Unit>
    suspend fun addUserToDatabase(user: UserModel): Result<Unit>
    fun logout()
    fun getCurrentUserId(): String?
}
