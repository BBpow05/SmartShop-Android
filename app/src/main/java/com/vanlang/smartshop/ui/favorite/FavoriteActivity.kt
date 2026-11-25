package com.vanlang.smartshop.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanlang.smartshop.adapter.ProductAdapter
import com.vanlang.smartshop.databinding.ActivityFavoriteBinding
import com.vanlang.smartshop.repository.CartRepository
import com.vanlang.smartshop.repository.FavoriteRepository
import com.vanlang.smartshop.ui.product.ProductDetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener { finish() }
        adapter = ProductAdapter(
            onAddCart = { product ->
                CartRepository.add(product.id)
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show()
            },
            isFavorite = { FavoriteRepository.isFavorite(it.id) },
            onToggleFavorite = { product ->
                FavoriteRepository.toggle(product.id)
                showFavorites()
            },
            onProductClick = { product ->
                startActivity(Intent(this, ProductDetailActivity::class.java).apply {
                    putExtra(ProductDetailActivity.EXTRA_PRODUCT, product)
                })
            }
        )
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)
        binding.rvFavorites.adapter = adapter
        showFavorites()
    }

    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) showFavorites()
    }

    private fun showFavorites() {
        val favorites = FavoriteRepository.getFavoriteProducts()
        adapter.submitList(favorites)
        binding.txtFavoriteCount.text = "${favorites.size} sản phẩm"
        binding.txtEmpty.visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
    }
}
