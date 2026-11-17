package com.vanlang.smartshop.product

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanlang.smartshop.databinding.ItemProductBinding
import com.vanlang.smartshop.model.Product
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onAddCart: (Product) -> Unit,
    private val isFavorite: (Product) -> Boolean,
    private val onToggleFavorite: (Product) -> Unit,
    private val onProductClick: (Product) -> Unit = {}
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val products = mutableListOf<Product>()

    private val currencyFormat =
        NumberFormat.getNumberInstance(Locale("vi", "VN"))

    fun submitList(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(
        val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {

        val product = products[position]
        val binding = holder.binding

        // Hiển thị thông tin sản phẩm
        binding.txtName.text = product.name
        binding.txtCategory.text = product.category

        binding.txtPrice.text =
            "${currencyFormat.format(product.price)} đ"

        // Hiển thị ảnh sản phẩm
        if (product.imageUrl.isNotBlank()) {

            binding.imgProduct.visibility = View.VISIBLE
            binding.txtEmoji.visibility = View.GONE

            Glide.with(binding.imgProduct)
                .load(product.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .centerInside()
                .into(binding.imgProduct)

        } else {

            Glide.with(binding.imgProduct).clear(
                binding.imgProduct
            )

            binding.imgProduct.visibility = View.GONE
            binding.txtEmoji.visibility = View.VISIBLE
            binding.txtEmoji.text = product.emoji
        }

        // Thêm giỏ hàng
        binding.btnAddCart.setOnClickListener {
            onAddCart(product)
        }

        // Cập nhật trạng thái yêu thích
        fun updateFavoriteButton() {

            val favorite = isFavorite(product)

            binding.btnFavorite.text =
                if (favorite) "♥" else "♡"

            binding.btnFavorite.setTextColor(
                if (favorite) Color.RED
                else Color.rgb(21, 128, 61)
            )

            binding.btnFavorite.contentDescription =
                if (favorite) {
                    "Bỏ yêu thích ${product.name}"
                } else {
                    "Yêu thích ${product.name}"
                }
        }

        updateFavoriteButton()

        // Bật/tắt yêu thích
        binding.btnFavorite.setOnClickListener {

            onToggleFavorite(product)
            updateFavoriteButton()
        }

        // Nhấn thẻ sản phẩm để xem chi tiết
        binding.root.setOnClickListener {
            onProductClick(product)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }
}
