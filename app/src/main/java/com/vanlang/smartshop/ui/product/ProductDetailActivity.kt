package com.vanlang.smartshop.ui.product

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.vanlang.smartshop.model.Product
import com.vanlang.smartshop.repository.CartRepository
import com.vanlang.smartshop.repository.FavoriteRepository
import com.vanlang.smartshop.ui.cart.CartActivity
import java.text.NumberFormat
import java.util.Locale

/** Handles the same Product model that MainActivity passes via EXTRA_PRODUCT. */
class ProductDetailActivity : AppCompatActivity() {
    companion object { const val EXTRA_PRODUCT = "extra_product" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val product: Product? = if (Build.VERSION.SDK_INT >= 33) {
            intent.getSerializableExtra(EXTRA_PRODUCT, Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            (intent.getSerializableExtra(EXTRA_PRODUCT) as? Product)
        }
        if (product == null) { finish(); return }

        val scroll = ScrollView(this).apply { setBackgroundColor(Color.WHITE) }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(15), dp(18), dp(22))
        }
        root.addView(Button(this).apply {
            text = "‹  Quay lại"
            isAllCaps = false
            setOnClickListener { finish() }
        })

        if (product.imageUrl.isBlank()) {
            root.addView(TextView(this).apply {
                text = product.emoji
                textSize = 82f
                gravity = Gravity.CENTER
                setBackgroundColor(Color.rgb(241, 248, 243))
            }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(250)))
        } else {
            val image = ImageView(this).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                setBackgroundColor(Color.rgb(241, 248, 243))
                contentDescription = product.name
            }
            root.addView(image, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(250)))
            Glide.with(this).load(product.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image).into(image)
        }
        root.addView(TextView(this).apply {
            text = product.category
            textSize = 13f
            setTextColor(Color.GRAY)
            setPadding(0, dp(13), 0, 0)
        })
        root.addView(TextView(this).apply {
            text = product.name
            textSize = 22f
            setTextColor(Color.rgb(15, 23, 42))
            setTypeface(null, Typeface.BOLD)
        })
        root.addView(TextView(this).apply {
            text = "${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(product.price)} đ"
            textSize = 24f
            setTextColor(Color.rgb(21, 128, 61))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, dp(8), 0, dp(15))
        })

        val buttons = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val favoriteButton = Button(this).apply {
            isAllCaps = false
            setTextColor(Color.rgb(21, 128, 61))
            backgroundTintList = android.content.res.ColorStateList.valueOf(Color.rgb(220, 252, 231))
        }
        fun updateFavorite() {
            favoriteButton.text = if (FavoriteRepository.isFavorite(product.id)) "♥ Đã thích" else "♡ Yêu thích"
        }
        updateFavorite()
        favoriteButton.setOnClickListener {
            FavoriteRepository.toggle(product.id)
            updateFavorite()
        }
        buttons.addView(favoriteButton, LinearLayout.LayoutParams(0, dp(50), 1f))
        val addButton = Button(this).apply {
            text = "Thêm vào giỏ hàng"
            isAllCaps = false
            setTextColor(Color.WHITE)
            backgroundTintList = android.content.res.ColorStateList.valueOf(Color.rgb(21, 128, 61))
            setOnClickListener {
                CartRepository.add(product.id)
                Toast.makeText(this@ProductDetailActivity, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
            }
        }
        buttons.addView(addButton, LinearLayout.LayoutParams(0, dp(50), 2f))
        root.addView(buttons)
        root.addView(Button(this).apply {
            text = "Xem giỏ hàng ›"
            isAllCaps = false
            setOnClickListener { startActivity(Intent(this@ProductDetailActivity, CartActivity::class.java)) }
        })
        root.addView(TextView(this).apply {
            text = "Mô tả sản phẩm"
            textSize = 18f
            setTextColor(Color.rgb(15, 23, 42))
            setTypeface(null, Typeface.BOLD)
            setPadding(0, dp(20), 0, dp(8))
        })
        root.addView(TextView(this).apply {
            text = product.description.ifBlank { "Sản phẩm thuộc danh mục ${product.category}." }
            textSize = 15f
            setTextColor(Color.DKGRAY)
        })
        scroll.addView(root)
        setContentView(scroll)
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}
