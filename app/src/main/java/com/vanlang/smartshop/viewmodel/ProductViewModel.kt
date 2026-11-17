package com.vanlang.smartshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vanlang.smartshop.model.Product
import com.vanlang.smartshop.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProductViewModel : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow("Tất cả")

    val products = combine(
        searchQuery,
        selectedCategory
    ) { query, category ->

        ProductRepository.searchProducts(query, category)

    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ProductRepository.products
    )

    val categories = ProductRepository.getCategories()

    fun search(keyword: String) {
        searchQuery.value = keyword
    }

    fun selectCategory(category: String) {
        selectedCategory.value = category
    }

    fun getProduct(id: Int): Product? {
        return ProductRepository.getProductById(id)
    }
}
