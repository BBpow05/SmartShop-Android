package com.vanlang.smartshop.ui.favorite

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanlang.smartshop.adapter.ProductAdapter
import com.vanlang.smartshop.repository.CartManager
import com.vanlang.smartshop.repository.FavoriteManager

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private lateinit var emptyMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }

        val title = TextView(this).apply {
            text = "❤️ Sản phẩm yêu thích"
            textSize = 24f
            setPadding(0, 0, 0, 16)
        }

        emptyMessage = TextView(this).apply {
            text = "Bạn chưa có sản phẩm yêu thích."
            textSize = 16f
            gravity = Gravity.CENTER
            visibility = View.GONE
        }

        adapter = ProductAdapter(
            onAddCart = { product ->
                CartManager.add(product.id)
            },
            isFavorite = { product ->
                FavoriteManager.isFavorite(product.id)
            },
            onToggleFavorite = { product ->
                FavoriteManager.toggle(product.id)
                refreshFavorites()
            }
        )

        val recyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = this@FavoriteActivity.adapter
        }

        root.addView(title)
        root.addView(
            emptyMessage,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 120
            )
        )
        root.addView(
            recyclerView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
            )
        )

        setContentView(root)
        refreshFavorites()
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            refreshFavorites()
        }
    }

    private fun refreshFavorites() {
        val favorites = FavoriteManager.getFavoriteProducts().toList()
        adapter.submitList(favorites)
        emptyMessage.visibility =
            if (favorites.isEmpty()) View.VISIBLE
            else View.GONE
    }
}