package com.vanlang.smartshop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vanlang.smartshop.databinding.ActivityLoginBinding
import com.vanlang.smartshop.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        // Kiểm tra tài khoản đã đăng nhập
        if (authViewModel.getCurrentUser() != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // Nhận kết quả từ ViewModel
        authViewModel.onSuccess = { user ->
            if (user != null) {
                Toast.makeText(
                    this,
                    "Đăng nhập thành công!",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(this, MainActivity::class.java)
                )
                finish()
            }
        }

        authViewModel.onError = { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        // Đăng nhập
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Vui lòng nhập đầy đủ email và mật khẩu",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                authViewModel.login(email, password)
            }
        }

        // Quên mật khẩu
        binding.tvForgotPassword.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.edtEmail.error = "Vui lòng nhập email"
            } else {
                authViewModel.forgotPassword(email)
                Toast.makeText(
                    this,
                    "Đang gửi yêu cầu đặt lại mật khẩu...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Chuyển sang đăng ký
        binding.tvGoRegister.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }
}
