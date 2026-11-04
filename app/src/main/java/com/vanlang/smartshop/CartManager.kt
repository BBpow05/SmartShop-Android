package com.vanlang.smartshop

object CartManager {
    val products = listOf(
        Product(1, "Điện thoại Samsung", 5990000, "Điện thoại", "📱"),
        Product(2, "Tai nghe Bluetooth", 450000, "Phụ kiện", "🎧"),
        Product(3, "Laptop văn phòng", 12990000, "Laptop", "💻"),
        Product(4, "Đồng hồ thông minh", 990000, "Phụ kiện", "⌚"),
        Product(5, "Bàn phím cơ", 750000, "Phụ kiện", "⌨️"),
        Product(6, "Máy tính bảng", 4990000, "Máy tính bảng", "📲")
    )

    val quantities = mutableMapOf<Int, Int>()

    fun add(productId: Int) {
        quantities[productId] = (quantities[productId] ?: 0) + 1
    }

    fun decrease(productId: Int) {
        val current = quantities[productId] ?: return

        if (current <= 1) {
            quantities.remove(productId)
        } else {
            quantities[productId] = current - 1
        }
    }

    fun remove(productId: Int) {
        quantities.remove(productId)
    }

    fun clear() {
        quantities.clear()
    }

    fun totalQuantity(): Int = quantities.values.sum()

    fun totalPrice(): Long {
        return products.sumOf { product ->
            product.price.toLong() *
                    (quantities[product.id] ?: 0)
        }
    }
}
