package com.vanlang.smartshop

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this).apply {
            text = "Danh sách sản phẩm yêu thích"
            textSize = 22f
            setPadding(32, 48, 32, 32)
        }

        setContentView(textView)
    }
}