package com.example.beautyhub.repository

import com.example.beautyhub.model.UserModel

interface UserRepository {
    fun getUsers(): List<UserModel>
    fun addUser(user: UserModel)
    fun getUserByEmail(email: String): UserModel?
}
