package com.vanlang.smartshop.repository

import com.vanlang.smartshop.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object CartRepository {
    private val _quantities = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val quantities = _quantities.asStateFlow()

    fun restore() {
        _quantities.value = ShopStorage.readCart()
    }

    private fun change(transform: (Map<Int, Int>) -> Map<Int, Int>) {
        _quantities.update(transform)
        ShopStorage.saveCart(_quantities.value)
    }

    fun add(productId: Int) {
        if (ProductRepository.getProductById(productId) == null) return
        change { current -> current + (productId to ((current[productId] ?: 0) + 1)) }
    }

    fun decrease(productId: Int) {
        change { current ->
            val currentQuantity = current[productId] ?: return@change current
            if (currentQuantity <= 1) current - productId
            else current + (productId to currentQuantity - 1)
        }
    }

    fun remove(productId: Int) = change { it - productId }
    fun clear() = change { emptyMap() }

    fun items(): List<CartItem> = quantities.value.mapNotNull { (id, quantity) ->
        ProductRepository.getProductById(id)?.let { CartItem(it, quantity) }
    }

    fun observeCartItems(): Flow<List<CartItem>> = quantities.map { current ->
        current.mapNotNull { (id, quantity) ->
            ProductRepository.getProductById(id)?.let { CartItem(it, quantity) }
        }
    }

    fun totalQuantity(): Int = quantities.value.values.sum()
    fun totalPrice(): Long = items().sumOf { it.totalPrice }
}
