package com.vanlang.smartshop.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.vanlang.smartshop.model.Product

@Entity(
    tableName = "products",
    indices = [
        Index(value = ["name"]),
        Index(value = ["category"])
    ]
)
data class ProductEntity(

    @PrimaryKey
    val id: Int,

    val name: String,

    val price: Long,

    val category: String,

    val emoji: String,

    val imageUrl: String = "",

    val description: String = ""

) {

    // Chuyển Entity sang Model
    fun toProduct(): Product {
        return Product(
            id = id,
            name = name,
            price = price,
            category = category,
            emoji = emoji,
            imageUrl = imageUrl,
            description = description
        )
    }

    companion object {

        // Chuyển Model sang Entity
        fun fromProduct(product: Product): ProductEntity {

            return ProductEntity(
                id = product.id,
                name = product.name,
                price = product.price,
                category = product.category,
                emoji = product.emoji,
                imageUrl = product.imageUrl,
                description = product.description
            )
        }
    }
}
