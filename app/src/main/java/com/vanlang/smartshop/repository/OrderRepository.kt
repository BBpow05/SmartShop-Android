package com.vanlang.smartshop.repository

import com.vanlang.smartshop.model.CustomerOrder
import com.vanlang.smartshop.model.OrderLine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

/** Order history is LOCAL DEMO data, not a real payment or backend order. */
object OrderRepository {
    private val _orders = MutableStateFlow<List<CustomerOrder>>(emptyList())
    val orders = _orders.asStateFlow()

    fun restore() {
        _orders.value = try {
            val json = JSONArray(ShopStorage.readOrderJson())
            buildList {
                for (i in 0 until json.length()) {
                    val order = json.optJSONObject(i) ?: continue
                    val lines = order.optJSONArray("items") ?: continue
                    val products = buildList {
                        for (j in 0 until lines.length()) {
                            val line = lines.optJSONObject(j) ?: continue
                            val qty = line.optInt("quantity", 0)
                            if (qty <= 0) continue
                            add(OrderLine(
                                line.optInt("productId"),
                                line.optString("name"),
                                line.optLong("unitPrice"),
                                qty
                            ))
                        }
                    }
                    if (products.isEmpty()) continue
                    add(CustomerOrder(
                        order.optString("orderId"),
                        order.optLong("createdAt"),
                        order.optString("recipient"),
                        order.optString("phone"),
                        order.optString("address"),
                        products
                    ))
                }
            }
        } catch (_: Exception) { emptyList() }
    }

    /** Returns false if cart is empty or the order could not be stored. */
    @Synchronized
    fun placeOrder(recipient: String, phone: String, address: String): Boolean {
        if (recipient.isBlank() || phone.isBlank() || address.isBlank()) return false
        val lines = CartRepository.items().map {
            OrderLine(it.product.id, it.product.name, it.product.price, it.quantity)
        }
        if (lines.isEmpty()) return false

        val order = CustomerOrder(
            UUID.randomUUID().toString().take(8).uppercase(),
            System.currentTimeMillis(), recipient, phone, address, lines
        )
        val updated = listOf(order) + _orders.value
        val json = JSONArray()
        updated.forEach { saved ->
            val lineArray = JSONArray()
            saved.items.forEach { line ->
                lineArray.put(JSONObject().apply {
                    put("productId", line.productId)
                    put("name", line.productName)
                    put("unitPrice", line.unitPrice)
                    put("quantity", line.quantity)
                })
            }
            json.put(JSONObject().apply {
                put("orderId", saved.orderId)
                put("createdAt", saved.createdAt)
                put("recipient", saved.recipient)
                put("phone", saved.phone)
                put("address", saved.address)
                put("items", lineArray)
            })
        }
        if (!ShopStorage.saveOrderJson(json.toString())) return false
        _orders.value = updated
        CartRepository.clear()
        return true
    }
}
