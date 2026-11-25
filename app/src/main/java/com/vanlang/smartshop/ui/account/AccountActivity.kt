package com.vanlang.smartshop.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.vanlang.smartshop.LoginActivity
import com.vanlang.smartshop.viewmodel.AuthViewModel

class AccountActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val user = authViewModel.getCurrentUser()

        if (user == null) {
            openLogin()
            return
        }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(40, 40, 40, 40)
        }

        val title = TextView(this).apply {
            text = "TÀI KHOẢN CỦA TÔI"
            textSize = 24f
            gravity = Gravity.CENTER
        }

        val nameText = TextView(this).apply {
            text = "Đang tải thông tin..."
            textSize = 18f
            setPadding(0, 30, 0, 12)
        }

        val emailText = TextView(this).apply {
            text = "Email: ${user.email ?: ""}"
            textSize = 16f
            setPadding(0, 12, 0, 30)
        }

        val logoutButton = Button(this).apply {
            text = "Đăng xuất"
        }

        layout.addView(title)
        layout.addView(nameText)
        layout.addView(emailText)
        layout.addView(logoutButton)

        setContentView(layout)

        authViewModel.getUserProfile(user.uid) { profile ->
            nameText.text =
                "Họ tên: ${profile?.get("fullName") ?: "Chưa cập nhật"}"
        }

        authViewModel.onError = { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            authViewModel.logout()

            Toast.makeText(
                this,
                "Đã đăng xuất",
                Toast.LENGTH_SHORT
            ).show()

            openLogin()
        }
    }

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }
}
