package com.vanlang.smartshop.repository

import com.vanlang.smartshop.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object FavoriteRepository {
    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds = _favoriteIds.asStateFlow()

    fun restore() {
        _favoriteIds.value = ShopStorage.readFavorites()
    }

    fun toggle(productId: Int): Boolean {
        if (ProductRepository.getProductById(productId) == null) return false
        val willBeFavorite = productId !in _favoriteIds.value
        _favoriteIds.update { current ->
            if (productId in current) current - productId else current + productId
        }
        ShopStorage.saveFavorites(_favoriteIds.value)
        return willBeFavorite
    }

    fun isFavorite(productId: Int): Boolean = productId in _favoriteIds.value

    fun observeFavoriteProducts(): Flow<List<Product>> = favoriteIds.map { ids ->
        ids.mapNotNull { ProductRepository.getProductById(it) }
    }

    fun getFavoriteProducts(): List<Product> =
        favoriteIds.value.mapNotNull { ProductRepository.getProductById(it) }
}
