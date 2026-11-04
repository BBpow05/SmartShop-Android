
package com.vanlang.smartshop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanlang.smartshop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var productAdapter: ProductAdapter

    private val allProducts = CartManager.products

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()
        setupBottomNavigation()

        binding.txtCart.setOnClickListener {
            openCart()
        }

        updateCartCount()
    }

    override fun onResume() {
        super.onResume()

        if (::productAdapter.isInitialized) {
            productAdapter.notifyDataSetChanged()
        }

        updateCartCount()
    }

    // Hiển thị danh sách sản phẩm
    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            onAddCart = { product ->
                CartManager.add(product.id)
                updateCartCount()

                Toast.makeText(
                    this,
                    "Đã thêm ${product.name} vào giỏ hàng",
                    Toast.LENGTH_SHORT
                ).show()
            },

            isFavorite = { product ->
                FavoriteManager.isFavorite(product.id)
            },

            onToggleFavorite = { product ->
                val added = FavoriteManager.toggle(product.id)

                productAdapter.notifyDataSetChanged()

                Toast.makeText(
                    this,
                    if (added) {
                        "❤️ Đã thêm ${product.name} vào yêu thích"
                    } else {
                        "Đã bỏ ${product.name} khỏi yêu thích"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        binding.rvProducts.layoutManager =
            LinearLayoutManager(this)

        binding.rvProducts.adapter = productAdapter
        productAdapter.submitList(allProducts)
    }

    // Tìm kiếm theo tên hoặc danh mục
    private fun setupSearch() {
        binding.edtSearch.doAfterTextChanged { editable ->
            val keyword = editable.toString().trim()

            val filteredProducts = allProducts.filter { product ->
                product.name.contains(keyword, ignoreCase = true) ||
                        product.category.contains(keyword, ignoreCase = true)
            }

            productAdapter.submitList(filteredProducts)
        }
    }

    // Thanh điều hướng phía dưới
    private fun setupBottomNavigation() {
        binding.navHome.setOnClickListener {
            Toast.makeText(
                this,
                "Bạn đang ở Trang chủ",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.navFavorite.setOnClickListener {
            startActivity(
                Intent(this, FavoriteActivity::class.java)
            )
        }

        binding.navCart.setOnClickListener {
            openCart()
        }

        binding.navAccount.setOnClickListener {
            startActivity(
                Intent(this, AccountActivity::class.java)
            )
        }
    }

    private fun openCart() {
        startActivity(
            Intent(this, CartActivity::class.java)
        )
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    private fun updateCartCount() {
        binding.txtCart.text =
            "🛒 Giỏ hàng: ${CartManager.totalQuantity()}"
    }
}
