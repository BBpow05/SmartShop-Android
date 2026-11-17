package com.vanlang.smartshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanlang.smartshop.model.Product
import com.vanlang.smartshop.repository.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel : ViewModel() {

    val favoriteProducts =
        FavoriteRepository.observeFavoriteProducts()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

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
