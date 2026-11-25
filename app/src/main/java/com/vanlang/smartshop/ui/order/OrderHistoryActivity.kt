package com.vanlang.smartshop.ui.order

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.vanlang.smartshop.databinding.ActivityOrderHistoryBinding
import com.vanlang.smartshop.model.CustomerOrder
import com.vanlang.smartshop.ui.main.MainActivity
import com.vanlang.smartshop.repository.OrderRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderHistoryBinding
    private val money = NumberFormat.getNumberInstance(Locale("vi", "VN"))
    private val date = SimpleDateFormat("dd/MM/yyyy • HH:mm", Locale("vi", "VN"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityOrderHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }
        binding.btnContinueShopping.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        showOrders()
    }

    private fun showOrders() {
        binding.orderItems.removeAllViews()
        val orders = OrderRepository.orders.value
        if (orders.isEmpty()) {
            binding.orderItems.addView(TextView(this).apply {
                text = "📦\nChưa có đơn hàng nào\nĐơn đã đặt sẽ xuất hiện tại đây."
                textSize = 17f
                gravity = Gravity.CENTER
                setTextColor(Color.GRAY)
                setPadding(dp(12), dp(70), dp(12), dp(70))
            })
            return
        }
        orders.forEach { showOrder(it) }
    }

    private fun showOrder(order: CustomerOrder) {
        val card = CardView(this).apply {
            radius = dp(16).toFloat()
            cardElevation = dp(3).toFloat()
            useCompatPadding = true
            setCardBackgroundColor(Color.WHITE)
        }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(15), dp(15), dp(15), dp(15))
        }
        content.addView(label("Đơn #${order.orderId}", 18f, Color.rgb(21, 128, 61), true))
        content.addView(label(date.format(Date(order.createdAt)), 12f, Color.GRAY))
        content.addView(label("Trạng thái: Đã đặt (demo)", 13f, Color.rgb(22, 163, 74), true))
        order.items.forEach { item ->
            content.addView(label(
                "${item.productName}  ×${item.quantity}\n" +
                "${money.format(item.unitPrice)} đ × ${item.quantity} = ${money.format(item.subtotal)} đ",
                13f, Color.rgb(51, 65, 85)
            ).apply { setPadding(0, dp(8), 0, dp(5)) })
        }
        content.addView(label(
            "Tổng cộng: ${money.format(order.totalPrice)} đ",
            17f, Color.rgb(21, 128, 61), true
        ).apply { setPadding(0, dp(10), 0, dp(6)) })
        content.addView(label("Người nhận: ${order.recipient} • ${order.phone}", 12f, Color.DKGRAY))
        content.addView(label("Địa chỉ: ${order.address}", 12f, Color.DKGRAY))
        card.addView(content)
        binding.orderItems.addView(card, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { bottomMargin = dp(10) })
    }

    private fun label(value: String, size: Float, color: Int, bold: Boolean = false) =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(null, Typeface.BOLD)
        }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}
