package com.vanlang.smartshop.repository

import com.vanlang.smartshop.model.Product

@Deprecated("Use CartRepository instead")
object CartManager {

    val products: List<Product>
        get() = ProductRepository.products

    val quantities: Map<Int, Int>
        get() = CartRepository.quantities.value

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

    fun totalQuantity(): Int {
        return CartRepository.totalQuantity()
    }

    fun totalPrice(): Long {
        return CartRepository.totalPrice()
    }
}
