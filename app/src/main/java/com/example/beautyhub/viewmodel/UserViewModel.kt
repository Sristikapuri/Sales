package com.example.beautyhub.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.beautyhub.model.UserModel
import com.example.beautyhub.repository.UserRepositoryImpl

class UserViewModel : ViewModel() {

    private val userRepository = UserRepositoryImpl()

    var currentUser by mutableStateOf<UserModel?>(null)
        private set

    fun login(email: String, password: String): Boolean {
        val user = userRepository.getUserByEmail(email)
        return if (user?.password == password) {
            currentUser = user
            true
        } else false
    }

    fun register(name: String, email: String, password: String) {
        val newUser = UserModel(
            id = (userRepository.getUsers().maxOfOrNull { it.id } ?: 0) + 1,
            name = name,
            email = email,
            password = password
        )
        userRepository.addUser(newUser)
    }
}
