package com.vanlang.smartshop.repository

import com.vanlang.smartshop.model.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object CartRepository {

    private val _quantities =
        MutableStateFlow<Map<Int, Int>>(emptyMap())

    val quantities = _quantities.asStateFlow()

    fun add(productId: Int) {
        _quantities.update { current ->
            current + (
                    productId to ((current[productId] ?: 0) + 1)
                    )
        }
    }

    fun decrease(productId: Int) {
        _quantities.update { current ->

            val quantity = current[productId] ?: return@update current

            if (quantity <= 1) {
                current - productId
            } else {
                current + (productId to quantity - 1)
            }
        }
    }

    fun remove(productId: Int) {
        _quantities.update { it - productId }
    }

    fun clear() {
        _quantities.value = emptyMap()
    }

    fun observeCartItems(): Flow<List<CartItem>> {
        return quantities.map { current ->

            current.mapNotNull { (productId, quantity) ->

                val product =
                    ProductRepository.getProductById(productId)

                product?.let {
                    CartItem(it, quantity)
                }
            }
        }
    }

    fun totalQuantity(): Int {
        return quantities.value.values.sum()
    }

    fun totalPrice(): Long {
        return quantities.value.entries.sumOf { entry ->

            val product =
                ProductRepository.getProductById(entry.key)

            (product?.price ?: 0L) * entry.value
        }
    }
}
