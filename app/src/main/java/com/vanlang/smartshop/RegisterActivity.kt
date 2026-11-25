package com.vanlang.smartshop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vanlang.smartshop.databinding.ActivityRegisterBinding
import com.vanlang.smartshop.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        authViewModel.onSuccess = { user ->
            if (user != null) {
                Toast.makeText(
                    this,
                    "Đăng ký tài khoản thành công!",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        authViewModel.onError = { message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        binding.btnRegister.setOnClickListener {
            val fullName = binding.edtFullName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString()
            val confirmPassword =
                binding.edtConfirmPassword.text.toString()

            when {
                fullName.isEmpty() ->
                    binding.edtFullName.error = "Vui lòng nhập họ tên"

                email.isEmpty() ->
                    binding.edtEmail.error = "Vui lòng nhập email"

                password.isEmpty() ->
                    binding.edtPassword.error = "Vui lòng nhập mật khẩu"

                password.length < 6 ->
                    binding.edtPassword.error =
                        "Mật khẩu phải có ít nhất 6 ký tự"

                confirmPassword != password ->
                    binding.edtConfirmPassword.error =
                        "Mật khẩu xác nhận không khớp"

                else -> authViewModel.register(fullName, email, password)
            }
        }

        binding.tvGoLogin.setOnClickListener {
            finish()
        }
    }
}
