package com.vanlang.smartshop

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.vanlang.smartshop.databinding.ActivityCheckoutBinding
import java.text.NumberFormat
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    private val currencyFormat =
        NumberFormat.getNumberInstance(Locale("vi", "VN"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtCheckoutTotal.text =
            "Tổng thanh toán: ${
                currencyFormat.format(CartManager.totalPrice())
            } đ"

        binding.btnConfirmPayment.setOnClickListener {
            confirmPayment()
        }

        binding.btnCancelPayment.setOnClickListener {
            finish()
        }
    }

    private fun confirmPayment() {
        val name = binding.edtFullName.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val address = binding.edtAddress.text.toString().trim()

        if (name.isEmpty()) {
            binding.edtFullName.error = "Vui lòng nhập họ và tên"
            binding.edtFullName.requestFocus()
            return
        }

        if (phone.isEmpty()) {
            binding.edtPhone.error = "Vui lòng nhập số điện thoại"
            binding.edtPhone.requestFocus()
            return
        }

        if (!Patterns.PHONE.matcher(phone).matches() ||
            phone.count { it.isDigit() } < 10
        ) {
            binding.edtPhone.error = "Số điện thoại chưa hợp lệ"
            binding.edtPhone.requestFocus()
            return
        }

        if (address.isEmpty()) {
            binding.edtAddress.error = "Vui lòng nhập địa chỉ"
            binding.edtAddress.requestFocus()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Xác nhận đơn hàng")
            .setMessage(
                "Người nhận: $name\n" +
                        "Số điện thoại: $phone\n" +
                        "Địa chỉ: $address\n" +
                        "Tổng tiền: ${
                            currencyFormat.format(CartManager.totalPrice())
                        } đ\n\n" +
                        "Bạn xác nhận đặt hàng chứ?"
            )
            .setPositiveButton("Xác nhận") { _, _ ->
                // Bản demo: xác nhận đơn hàng, chưa xử lý thanh toán thật.
                CartManager.clear()

                Toast.makeText(
                    this,
                    "Đặt hàng thành công!",
                    Toast.LENGTH_LONG
                ).show()

                finishAffinity()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
