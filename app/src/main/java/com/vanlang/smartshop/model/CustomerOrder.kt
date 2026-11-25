package com.vanlang.smartshop.model

data class OrderLine(
    val productId: Int,
    val productName: String,
    val unitPrice: Long,
    val quantity: Int
) {
    val subtotal: Long get() = unitPrice * quantity
}

data class CustomerOrder(
    val orderId: String,
    val createdAt: Long,
    val recipient: String,
    val phone: String,
    val address: String,
    val items: List<OrderLine>
) {
    val totalPrice: Long get() = items.sumOf { it.subtotal }
    val totalQuantity: Int get() = items.sumOf { it.quantity }
}
