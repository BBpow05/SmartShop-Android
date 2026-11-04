package com.vanlang.smartshop

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vanlang.smartshop.databinding.ActivityCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    private val currencyFormat =
        NumberFormat.getNumberInstance(Locale("vi", "VN"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheckout.setOnClickListener {
            if (CartManager.totalQuantity() == 0) {
                Toast.makeText(
                    this,
                    "Giỏ hàng đang trống!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(
                    Intent(this, CheckoutActivity::class.java)
                )
            }
        }

        binding.btnContinueShopping.setOnClickListener {
            finish()
        }

        renderCart()
    }

    override fun onResume() {
        super.onResume()

        if (::binding.isInitialized) {
            renderCart()
        }
    }

    private fun renderCart() {
        binding.cartItems.removeAllViews()

        val selectedProducts = CartManager.products.filter {
            (CartManager.quantities[it.id] ?: 0) > 0
        }

        if (selectedProducts.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "Giỏ hàng đang trống."
                textSize = 18f
                gravity = Gravity.CENTER
                setPadding(12, 32, 12, 32)
            }

            binding.cartItems.addView(emptyText)
        }

        selectedProducts.forEach { product ->
            val quantity = CartManager.quantities[product.id] ?: 0

            val itemLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(12, 12, 12, 12)
                setBackgroundColor(0xFFFFFFFF.toInt())
            }

            val name = TextView(this).apply {
                text = product.name
                textSize = 18f
            }

            val price = TextView(this).apply {
                text = "Đơn giá: ${currencyFormat.format(product.price)} đ"
                textSize = 14f
            }

            val quantityText = TextView(this).apply {
                text = "Số lượng: $quantity"
                textSize = 16f
            }

            val subtotal = TextView(this).apply {
                text = "Thành tiền: ${
                    currencyFormat.format(product.price.toLong() * quantity)
                } đ"
                textSize = 16f
            }

            val buttons = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            val decreaseButton = Button(this).apply {
                text = "−"
                setOnClickListener {
                    CartManager.decrease(product.id)
                    renderCart()
                }
            }

            val increaseButton = Button(this).apply {
                text = "+"
                setOnClickListener {
                    CartManager.add(product.id)
                    renderCart()
                }
            }

            val removeButton = Button(this).apply {
                text = "Xóa"
                setOnClickListener {
                    CartManager.remove(product.id)
                    renderCart()
                }
            }

            buttons.addView(decreaseButton)
            buttons.addView(increaseButton)
            buttons.addView(removeButton)

            itemLayout.addView(name)
            itemLayout.addView(price)
            itemLayout.addView(quantityText)
            itemLayout.addView(subtotal)
            itemLayout.addView(buttons)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            params.bottomMargin = 16
            binding.cartItems.addView(itemLayout, params)
        }

        binding.txtTotal.text =
            "Tổng tiền: ${currencyFormat.format(CartManager.totalPrice())} đ"
    }
}
