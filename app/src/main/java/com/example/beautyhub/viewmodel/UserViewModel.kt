package com.example.beautyhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.beautyhub.repository.UserRepository
import com.example.beautyhub.model.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val _authResult = MutableStateFlow<Result<String>?>(null)
    val authResult: StateFlow<Result<String>?> = _authResult

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = repo.register(email, password)
        }
    }

    fun login(email: String, password: String, onCompleted: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = repo.login(email, password)
            result.fold(
                onSuccess = { onCompleted(true, "Login successful") },
                onFailure = { onCompleted(false, it.localizedMessage ?: "Login failed") }
            )
        }
    }

    fun sendResetPasswordEmail(email: String, onCompleted: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = repo.sendResetPasswordEmail(email)
            result.fold(
                onSuccess = { onCompleted(true, "Password reset email sent") },
                onFailure = { onCompleted(false, it.localizedMessage ?: "Failed to send reset email") }
            )
        }
    }

    fun addUserProfile(user: UserModel, onCompleted: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = repo.addUserToDatabase(user)
            result.fold(
                onSuccess = { onCompleted(true, "User profile saved") },
                onFailure = { onCompleted(false, it.localizedMessage ?: "Failed to save user profile") }
            )
        }
    }

    fun logout() {
        repo.logout()
    }
}
