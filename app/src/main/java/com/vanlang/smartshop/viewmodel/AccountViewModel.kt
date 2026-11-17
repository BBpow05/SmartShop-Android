package com.vanlang.smartshop.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.vanlang.smartshop.model.User

class AccountViewModel : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)

    val currentUser = _currentUser.asStateFlow()

    fun updateUser(user: User) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }
}
