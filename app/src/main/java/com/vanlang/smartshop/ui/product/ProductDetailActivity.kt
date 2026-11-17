package com.vanlang.smartshop.ui.product

import android.R
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ProductDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PRODUCT = "extra_product"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        val product = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra(
                EXTRA_PRODUCT,
                ShopProduct::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_PRODUCT) as? ShopProduct
        }

        if (product == null) {
            finish()
            return
        }

        val scrollView = ScrollView(this)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(15), dp(18), dp(20))
            setBackgroundColor(Color.WHITE)
        }

        val backButton = Button(this).apply {
            text = "‹  Quay lại"
            isAllCaps = false
            setOnClickListener {
                finish()
            }
        }

        root.addView(backButton)

        val image = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(285)
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
            setBackgroundColor(Color.WHITE)
        }

        Glide.with(this)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_menu_gallery)
            .error(R.drawable.ic_menu_report_image)
            .centerInside()
            .into(image)

        root.addView(image)

        val category = TextView(this).apply {
            text = product.category
            textSize = 13f
            setTextColor(Color.GRAY)
        }

        root.addView(category)

        val name = TextView(this).apply {
            text = product.name
            textSize = 22f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.rgb(35, 45, 40))
            setPadding(0, dp(9), 0, dp(10))
        }

        root.addView(name)

        val price = TextView(this).apply {
            text = product.formattedPrice()
            textSize = 24f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.rgb(21, 128, 61))
        }

        root.addView(price)

        val divider = View(this).apply {
            setBackgroundColor(Color.rgb(230, 235, 230))
        }

        val dividerParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(1)
        )
        dividerParams.topMargin = dp(17)
        dividerParams.bottomMargin = dp(17)

        root.addView(divider, dividerParams)

        val descriptionTitle = TextView(this).apply {
            text = "Mô tả sản phẩm"
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.BLACK)
        }

        root.addView(descriptionTitle)

        val description = TextView(this).apply {
            text = product.description
            textSize = 15f
            setTextColor(Color.DKGRAY)
            setLineSpacing(dp(5).toFloat(), 1f)
            setPadding(0, dp(10), 0, dp(20))
        }

        root.addView(description)

        val note = TextView(this).apply {
            text = "Sản phẩm chính hãng - Thông tin tham khảo"
            textSize = 13f
            setTextColor(Color.GRAY)
            gravity = Gravity.CENTER
            setPadding(0, dp(12), 0, dp(12))
        }

        root.addView(note)

        scrollView.addView(root)
        setContentView(scrollView)
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}