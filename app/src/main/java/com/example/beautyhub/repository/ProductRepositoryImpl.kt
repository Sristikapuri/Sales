package com.example.beautyhub.repository

import com.example.beautyhub.model.UserModel

class UserRepositoryImpl : UserRepository {
    private val users = mutableListOf<UserModel>()

    override fun getUsers(): List<UserModel> = users

    override fun addUser(user: UserModel) {
        users.add(user)
    }

    override fun getUserByEmail(email: String): UserModel? {
        return users.find { it.email == email }
    }
}
