package com.vanlang.smartshop

import com.vanlang.smartshop.model.CustomerOrder
import com.vanlang.smartshop.model.OrderLine
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderTotalsTest {
    @Test fun totalsAreCalculatedFromQuantityAndUnitPrice() {
        val order = CustomerOrder(
            "DEMO1234", 0L, "Nguyen A", "0900000000", "Test",
            listOf(
                OrderLine(1, "Phone", 1_200_000L, 2),
                OrderLine(2, "Cable", 150_000L, 3)
            )
        )
        assertEquals(5, order.totalQuantity)
        assertEquals(2_850_000L, order.totalPrice)
        assertEquals(2_400_000L, order.items[0].subtotal)
    }
}
