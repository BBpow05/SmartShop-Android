package com.vanlang.smartshop.ui.cart

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vanlang.smartshop.databinding.ActivityCheckoutBinding
import com.vanlang.smartshop.repository.CartRepository
import com.vanlang.smartshop.repository.OrderRepository
import com.vanlang.smartshop.ui.order.OrderHistoryActivity
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private val formatter = NumberFormat.getNumberInstance(Locale("vi", "VN"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnConfirmPayment.setOnClickListener { confirmOrder() }
        binding.btnCancelPayment.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        val total = CartRepository.totalPrice()
        binding.txtCheckoutTotal.text = "Tổng đặt hàng: ${formatter.format(total)} đ"
        binding.btnConfirmPayment.isEnabled = CartRepository.items().isNotEmpty()
    }

    private fun confirmOrder() {
        if (CartRepository.items().isEmpty()) {
            Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show()
            return
        }
        val name = binding.edtFullName.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val address = binding.edtAddress.text.toString().trim()

        if (name.isBlank()) {
            binding.edtFullName.error = "Vui lòng nhập họ tên"
            binding.edtFullName.requestFocus()
            return
        }
        if (!Patterns.PHONE.matcher(phone).matches() || phone.count { it.isDigit() } < 10) {
            binding.edtPhone.error = "Số điện thoại không hợp lệ"
            binding.edtPhone.requestFocus()
            return
        }
        if (address.isBlank()) {
            binding.edtAddress.error = "Vui lòng nhập địa chỉ giao hàng"
            binding.edtAddress.requestFocus()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Xác nhận đặt hàng")
            .setMessage(
                "Người nhận: $name\nĐiện thoại: $phone\nĐịa chỉ: $address\n" +
                "Số lượng: ${CartRepository.totalQuantity()}\n" +
                "Tổng tiền: ${formatter.format(CartRepository.totalPrice())} đ\n\n" +
                "Đây là đơn hàng demo, chưa thanh toán thực tế."
            )
            .setNegativeButton("Kiểm tra lại", null)
            .setPositiveButton("Đặt hàng") { _, _ ->
                val saved = OrderRepository.placeOrder(name, phone, address)
                if (saved) {
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, OrderHistoryActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Không thể lưu đơn hàng. Vui lòng thử lại.", Toast.LENGTH_LONG).show()
                }
            }
            .show()
    }
}
