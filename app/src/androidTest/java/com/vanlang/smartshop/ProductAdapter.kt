package com.vanlang.smartshop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vanlang.smartshop.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onAddCart: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val products = mutableListOf<Product>()

    private val currencyFormat = NumberFormat.getNumberInstance(
        Locale("vi", "VN")
    )

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

        binding.txtEmoji.text = product.emoji
        binding.txtName.text = product.name
        binding.txtCategory.text = product.category
        binding.txtPrice.text =
            "${currencyFormat.format(product.price)} đ"

        binding.btnAddCart.setOnClickListener {
            onAddCart(product)
        }
    }

    override fun getItemCount(): Int = products.size
}
