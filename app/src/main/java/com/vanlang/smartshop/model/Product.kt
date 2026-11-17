package com.vanlang.smartshop.model

import java.io.Serializable

data class Product(
    val id: Int,
    val name: String,
    val price: Long,
    val category: String,
    val emoji: String,
    val imageUrl: String = "",
    val description: String = ""
) : Serializable
