package com.vanlang.smartshop.repository

import com.vanlang.smartshop.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

object FavoriteRepository {

    private val _favoriteIds =
        MutableStateFlow<Set<Int>>(emptySet())

    val favoriteIds = _favoriteIds.asStateFlow()

    fun toggle(productId: Int): Boolean {

        val willBeFavorite =
            productId !in _favoriteIds.value

        _favoriteIds.update { current ->

            if (productId in current) {
                current - productId
            } else {
                current + productId
            }
        }

        return willBeFavorite
    }

    fun isFavorite(productId: Int): Boolean {
        return productId in favoriteIds.value
    }

    fun observeFavoriteProducts(): Flow<List<Product>> {

        return favoriteIds.map { ids ->

            ProductRepository.products.filter { product ->
                product.id in ids
            }
        }
    }

    fun getFavoriteProducts(): List<Product> {
        return ProductRepository.products.filter {
            it.id in favoriteIds.value
        }
    }
}
