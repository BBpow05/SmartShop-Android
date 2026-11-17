package com.vanlang.smartshop.ui.main

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.vanlang.smartshop.adapter.ProductAdapter
import com.vanlang.smartshop.databinding.ActivityMainBinding
import com.vanlang.smartshop.repository.CartManager
import com.vanlang.smartshop.repository.FavoriteManager
import com.vanlang.smartshop.repository.ProductRepository
import com.vanlang.smartshop.ui.account.AccountActivity
import com.vanlang.smartshop.ui.cart.CartActivity
import com.vanlang.smartshop.ui.favorite.FavoriteActivity
import com.vanlang.smartshop.ui.product.ProductDetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var productAdapter: ProductAdapter

    // Thanh danh mục lấy trực tiếp từ XML
    private lateinit var categoryRow: LinearLayout

    // Danh sách sản phẩm
    private val allProducts
        get() = CartManager.products

    // Trạng thái tìm kiếm và danh mục
    private var selectedCategory = "Tất cả"
    private var searchKeyword = ""

    private val categories by lazy {
        ProductRepository.getCategories()
    }

    // =====================================
    // ON CREATE
    // =====================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCategoryBar()
        setupRecyclerView()
        renderCategories()
        setupSearch()
        setupBottomNavigation()

        binding.txtCart.setOnClickListener {
            openCart()
        }

        updateCartCount()
        filterProducts()
    }

    // =====================================
    // ON RESUME
    // =====================================

    override fun onResume() {
        super.onResume()

        if (::productAdapter.isInitialized) {
            filterProducts()
            productAdapter.notifyDataSetChanged()
        }

        updateCartCount()
    }

    // =====================================
    // 1. KẾT NỐI DANH MỤC VỚI XML
    // =====================================

    private fun setupCategoryBar() {

        // Sử dụng LinearLayout đã khai báo
        // trong activity_main.xml
        categoryRow = binding.categoryRow
    }

    // =====================================
    // 2. HIỂN THỊ DANH SÁCH SẢN PHẨM
    // =====================================

    private fun setupRecyclerView() {

        productAdapter = ProductAdapter(

            // Thêm vào giỏ hàng
            onAddCart = { product ->

                CartManager.add(product.id)

                updateCartCount()

                Toast.makeText(
                    this,
                    "Đã thêm ${product.name} vào giỏ hàng",
                    Toast.LENGTH_SHORT
                ).show()
            },

            // Kiểm tra yêu thích
            isFavorite = { product ->

                FavoriteManager.isFavorite(product.id)
            },

            // Thêm hoặc bỏ yêu thích
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
            },

            // Mở chi tiết sản phẩm
            onProductClick = { product ->

                val intent = Intent(
                    this,
                    ProductDetailActivity::class.java
                )

                intent.putExtra(
                    ProductDetailActivity.EXTRA_PRODUCT,
                    product
                )

                startActivity(intent)
            }
        )

        binding.rvProducts.layoutManager =
            LinearLayoutManager(this)

        binding.rvProducts.adapter = productAdapter

        productAdapter.submitList(allProducts)
    }

    // =====================================
    // 3. HIỂN THỊ DANH MỤC
    // =====================================

    private fun renderCategories() {

        categoryRow.removeAllViews()

        categories.forEach { category ->

            val selected = category == selectedCategory

            val chip = TextView(this).apply {

                text = category
                textSize = 12f

                gravity = Gravity.CENTER

                setTypeface(
                    null,
                    if (selected) {
                        Typeface.BOLD
                    } else {
                        Typeface.NORMAL
                    }
                )

                setPadding(
                    dp(15),
                    dp(8),
                    dp(15),
                    dp(8)
                )

                setTextColor(
                    if (selected) {
                        Color.WHITE
                    } else {
                        Color.rgb(65, 80, 70)
                    }
                )

                background = createChipBackground(selected)

                // Lọc sản phẩm khi nhấn danh mục
                setOnClickListener {

                    selectedCategory = category

                    renderCategories()

                    filterProducts()
                }
            }

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                dp(36)
            ).apply {
                marginEnd = dp(8)
            }

            categoryRow.addView(chip, params)
        }
    }

    // =====================================
    // 4. TÌM KIẾM SẢN PHẨM
    // =====================================

    private fun setupSearch() {

        binding.edtSearch.doAfterTextChanged { editable ->

            searchKeyword =
                editable?.toString()?.trim() ?: ""

            filterProducts()
        }
    }

    // =====================================
    // 5. KẾT HỢP TÌM KIẾM VÀ LỌC DANH MỤC
    // =====================================

    private fun filterProducts() {

        val result = allProducts.filter { product ->

            // Kiểm tra từ khóa
            val matchKeyword =
                product.name.contains(
                    searchKeyword,
                    ignoreCase = true
                ) ||
                        product.category.contains(
                            searchKeyword,
                            ignoreCase = true
                        )

            // Kiểm tra danh mục
            val matchCategory =
                selectedCategory == "Tất cả" ||
                        product.category.equals(
                            selectedCategory,
                            ignoreCase = true
                        )

            matchKeyword && matchCategory
        }

        productAdapter.submitList(result)
    }

    // =====================================
    // 6. TẠO GIAO DIỆN NÚT DANH MỤC
    // =====================================

    private fun createChipBackground(
        selected: Boolean
    ): GradientDrawable {

        return GradientDrawable().apply {

            setColor(
                if (selected) {
                    Color.rgb(21, 128, 61)
                } else {
                    Color.WHITE
                }
            )

            cornerRadius = dp(22).toFloat()

            if (!selected) {
                setStroke(
                    dp(1),
                    Color.rgb(225, 235, 228)
                )
            }
        }
    }

    // =====================================
    // 7. BOTTOM NAVIGATION
    // =====================================

    private fun setupBottomNavigation() {

        // Trang chủ
        binding.navHome.setOnClickListener {

            selectedCategory = "Tất cả"
            searchKeyword = ""

            binding.edtSearch.text?.clear()

            renderCategories()
            filterProducts()

            Toast.makeText(
                this,
                "Bạn đang ở Trang chủ",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Yêu thích
        binding.navFavorite.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    FavoriteActivity::class.java
                )
            )
        }

        // Giỏ hàng
        binding.navCart.setOnClickListener {
            openCart()
        }

        // Tài khoản
        binding.navAccount.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    AccountActivity::class.java
                )
            )
        }
    }

    // =====================================
    // 8. MỞ GIỎ HÀNG
    // =====================================

    private fun openCart() {

        startActivity(
            Intent(
                this,
                CartActivity::class.java
            )
        )
    }

    // =====================================
    // 9. CẬP NHẬT SỐ LƯỢNG GIỎ HÀNG
    // =====================================

    private fun updateCartCount() {

        binding.txtCart.text =
            "🛒 Giỏ hàng: ${CartManager.totalQuantity()}"
    }

    // =====================================
    // 10. CHUYỂN DP
    // =====================================

    private fun dp(value: Int): Int {

        return (
                value * resources.displayMetrics.density
                ).toInt()
    }
}
