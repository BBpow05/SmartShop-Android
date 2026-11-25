package com.vanlang.smartshop.ui.cart

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.vanlang.smartshop.databinding.ActivityCartBinding
import com.vanlang.smartshop.model.CartItem
import com.vanlang.smartshop.repository.CartRepository
import com.vanlang.smartshop.ui.order.OrderHistoryActivity
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.btnContinueShopping.setOnClickListener { finish() }
        binding.btnOrderHistory.setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }
        binding.btnCheckout.setOnClickListener {
            if (CartRepository.items().isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, CheckoutActivity::class.java))
            }
        }
        renderCart()
    }

    override fun onResume() {
        super.onResume()
        if (::binding.isInitialized) renderCart()
    }

    private fun renderCart() {
        val items = CartRepository.items()
        binding.cartItems.removeAllViews()
        binding.txtItemCount.text = "${CartRepository.totalQuantity()} sản phẩm"
        binding.txtTotal.text = "Tổng cộng: ${formatter.format(CartRepository.totalPrice())} đ"
        binding.btnCheckout.isEnabled = items.isNotEmpty()

        if (items.isEmpty()) {
            val empty = TextView(this).apply {
                text = "🛒\nGiỏ hàng đang trống\nHãy chọn sản phẩm bạn yêu thích nhé!"
                textSize = 17f
                setTextColor(Color.rgb(100, 116, 139))
                gravity = Gravity.CENTER
                setPadding(dp(12), dp(70), dp(12), dp(70))
            }
            binding.cartItems.addView(empty)
            return
        }
        items.forEach { item -> addCartRow(item) }
    }

    private fun addCartRow(item: CartItem) {
        val product = item.product
        val card = CardView(this).apply {
            radius = dp(15).toFloat()
            cardElevation = dp(3).toFloat()
            setCardBackgroundColor(Color.WHITE)
            useCompatPadding = true
        }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }
        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        if (product.imageUrl.isNotBlank()) {
            val photo = ImageView(this).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                setBackgroundColor(Color.rgb(241, 248, 243))
                contentDescription = product.name
            }
            top.addView(photo, LinearLayout.LayoutParams(dp(85), dp(90)))
            Glide.with(this).load(product.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image).into(photo)
        } else {
            top.addView(TextView(this).apply {
                text = product.emoji
                textSize = 34f
                gravity = Gravity.CENTER
                setBackgroundColor(Color.rgb(241, 248, 243))
            }, LinearLayout.LayoutParams(dp(85), dp(90)))
        }
        val info = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), 0, 0, 0)
        }
        info.addView(TextView(this).apply {
            text = product.name
            textSize = 15f
            setTextColor(Color.rgb(15, 23, 42))
            setTypeface(null, Typeface.BOLD)
            maxLines = 2
        })
        info.addView(TextView(this).apply {
            text = product.category
            textSize = 12f
            setTextColor(Color.GRAY)
        })
        info.addView(TextView(this).apply {
            text = "${formatter.format(product.price)} đ"
            textSize = 16f
            setTextColor(Color.rgb(21, 128, 61))
            setTypeface(null, Typeface.BOLD)
        })
        top.addView(info, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        content.addView(top)

        val actions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(0, dp(8), 0, 0)
        }
        actions.addView(TextView(this).apply {
            text = "Thành tiền: ${formatter.format(item.totalPrice)} đ"
            textSize = 13f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.rgb(15, 23, 42))
        }, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        actions.addView(quantityButton("−") {
            CartRepository.decrease(product.id)
            renderCart()
        })
        actions.addView(TextView(this).apply {
            text = item.quantity.toString()
            gravity = Gravity.CENTER
            textSize = 15f
        }, LinearLayout.LayoutParams(dp(30), dp(40)))
        actions.addView(quantityButton("+") {
            CartRepository.add(product.id)
            renderCart()
        })
        actions.addView(TextView(this).apply {
            text = "✕"
            gravity = Gravity.CENTER
            textSize = 18f
            setTextColor(Color.rgb(220, 38, 38))
            contentDescription = "Xóa ${product.name}"
            isClickable = true
            isFocusable = true
            setOnClickListener {
                CartRepository.remove(product.id)
                renderCart()
            }
        }, LinearLayout.LayoutParams(dp(42), dp(42)))
        content.addView(actions)
        card.addView(content)
        binding.cartItems.addView(card, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { bottomMargin = dp(8) })
    }

    private fun quantityButton(label: String, onClick: () -> Unit): Button = Button(this).apply {
        text = label
        textSize = 18f
        isAllCaps = false
        minWidth = 0
        minimumWidth = 0
        minHeight = 0
        minimumHeight = 0
        setPadding(0, 0, 0, 0)
        setTextColor(Color.rgb(21, 128, 61))
        backgroundTintList = android.content.res.ColorStateList.valueOf(Color.rgb(220, 252, 231))
        layoutParams = LinearLayout.LayoutParams(dp(38), dp(38))
        setOnClickListener { onClick() }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
