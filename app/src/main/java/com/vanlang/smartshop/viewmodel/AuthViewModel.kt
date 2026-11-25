package com.vanlang.smartshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.vanlang.smartshop.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    var onSuccess: ((FirebaseUser?) -> Unit)? = null
    var onError: ((String) -> Unit)? = null

    fun register(fullName: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = repository.register(fullName, email, password)
                onSuccess?.invoke(user)
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Đăng ký thất bại")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = repository.login(email, password)
                onSuccess?.invoke(user)
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Đăng nhập thất bại")
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            try {
                repository.forgotPassword(email)
                onSuccess?.invoke(null)
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Không thể gửi email đặt lại mật khẩu")
            }
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return repository.getCurrentUser()
    }

    fun getUserProfile(
        uid: String,
        onResult: (Map<String, Any>?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                onResult(repository.getUserProfile(uid))
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Không thể tải hồ sơ")
            }
        }
    }

    fun logout() {
        repository.logout()
    }
}
