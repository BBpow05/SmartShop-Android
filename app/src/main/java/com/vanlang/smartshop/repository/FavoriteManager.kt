package com.vanlang.smartshop.repository

import com.vanlang.smartshop.model.Product

@Deprecated("Use FavoriteRepository instead")
object FavoriteManager {

    fun toggle(productId: Int): Boolean {
        return FavoriteRepository.toggle(productId)
    }

    fun isFavorite(productId: Int): Boolean {
        return FavoriteRepository.isFavorite(productId)
    }

    fun getFavoriteProducts(): List<Product> {
        return FavoriteRepository.getFavoriteProducts()
    }
}
