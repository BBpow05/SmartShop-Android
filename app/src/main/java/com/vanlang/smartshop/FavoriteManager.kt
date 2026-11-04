package com.vanlang.smartshop

object FavoriteManager {

    private val favoriteIds = mutableSetOf<Int>()

    fun toggle(productId: Int): Boolean {
        return if (favoriteIds.contains(productId)) {
            favoriteIds.remove(productId)
            false
        } else {
            favoriteIds.add(productId)
            true
        }
    }

    fun isFavorite(productId: Int): Boolean {
        return productId in favoriteIds
    }

    fun getFavoriteProducts(): List<Product> {
        return CartManager.products.filter { product ->
            product.id in favoriteIds
        }
    }
}
