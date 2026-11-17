package com.vanlang.smartshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanlang.smartshop.repository.CartRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CartViewModel : ViewModel() {

    val cartItems = CartRepository.observeCartItems()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val totalQuantity = CartRepository.quantities
        .map { quantities -> quantities.values.sum() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    val totalPrice = cartItems
        .map { items -> items.sumOf { it.totalPrice } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0L
        )

    fun add(productId: Int) {
        CartRepository.add(productId)
    }

    fun decrease(productId: Int) {
        CartRepository.decrease(productId)
    }

    fun remove(productId: Int) {
        CartRepository.remove(productId)
    }

    fun clear() {
        CartRepository.clear()
    }
}
