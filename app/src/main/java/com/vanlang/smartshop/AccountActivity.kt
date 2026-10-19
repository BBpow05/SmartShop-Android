package com.vanlang.smartshop

import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val padding = (24 * resources.displayMetrics.density).toInt()

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(padding, padding * 2, padding, padding)
            setBackgroundColor(Color.rgb(248, 250, 252))
        }

        val title = TextView(this).apply {
            text = "Tài khoản của tôi"
            textSize = 26f
            setTextColor(Color.rgb(21, 128, 61))
            gravity = Gravity.CENTER
        }

        val avatar = TextView(this).apply {
            text = "👤"
            textSize = 64f
            gravity = Gravity.CENTER
            setPadding(0, padding, 0, padding)
        }

        val name = TextView(this).apply {
            text = "Khách hàng SmartShop"
            textSize = 20f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.CENTER
        }

        val email = TextView(this).apply {
            text = "Thông tin tài khoản"
            textSize = 16f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, padding / 2, 0, padding)
        }

        val btnBack = Button(this).apply {
            text = "Quay lại cửa hàng"
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(21, 128, 61))
            setOnClickListener {
                finish()
            }
        }

        layout.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        layout.addView(avatar)
        layout.addView(name)
        layout.addView(email)
        layout.addView(btnBack)

        setContentView(layout)
    }
}
