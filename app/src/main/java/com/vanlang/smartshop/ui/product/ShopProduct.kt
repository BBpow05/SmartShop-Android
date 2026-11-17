package com.vanlang.smartshop.ui.product

import java.io.Serializable
import java.text.NumberFormat
import java.util.Locale

data class ShopProduct(
    val id: Int,
    val name: String,
    val category: String,
    val price: Long,
    val description: String,
    val imageUrl: String
) : Serializable {

    fun formattedPrice(): String {
        return NumberFormat
            .getNumberInstance(Locale("vi", "VN"))
            .format(price) + "đ"
    }
}