package com.vanlang.smartshop.adapter

import android.R
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vanlang.smartshop.ui.product.ShopProduct

class ShopProductAdapter(
    private val onClick: (ShopProduct) -> Unit
) : RecyclerView.Adapter<ShopProductAdapter.ProductViewHolder>() {

    private val products = mutableListOf<ShopProduct>()

    class ProductViewHolder(
        val card: LinearLayout,
        val image: ImageView,
        val name: TextView,
        val category: TextView,
        val price: TextView,
        val detail: TextView
    ) : RecyclerView.ViewHolder(card)

    fun submitList(list: List<ShopProduct>) {
        products.clear()
        products.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val context = parent.context

        val card = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = roundedBackground(context, Color.WHITE, 16)
            elevation = dp(context, 3).toFloat()
            clipToOutline = true

            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(
                    dp(context, 6),
                    dp(context, 7),
                    dp(context, 6),
                    dp(context, 7)
                )
            }
        }

        val image = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(context, 145)
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
            setPadding(dp(context, 8), dp(context, 8),
                dp(context, 8), dp(context, 8))
            setBackgroundColor(Color.WHITE)
        }

        val category = TextView(context).apply {
            textSize = 11f
            setTextColor(Color.GRAY)
            setPadding(dp(context, 11), dp(context, 9),
                dp(context, 11), 0)
        }

        val name = TextView(context).apply {
            textSize = 13f
            setTextColor(Color.rgb(35, 45, 40))
            setTypeface(null, Typeface.BOLD)
            maxLines = 2
            minLines = 2
            setPadding(
                dp(context, 11), dp(context, 5),
                dp(context, 11), 0
            )
        }

        val price = TextView(context).apply {
            textSize = 15f
            setTextColor(Color.rgb(21, 128, 61))
            setTypeface(null, Typeface.BOLD)
            setPadding(
                dp(context, 11), dp(context, 9),
                dp(context, 11), 0
            )
        }

        val detail = TextView(context).apply {
            text = "Xem chi tiết  ›"
            textSize = 12f
            setTextColor(Color.rgb(21, 128, 61))
            setPadding(
                dp(context, 11), dp(context, 10),
                dp(context, 11), dp(context, 12)
            )
        }

        card.addView(image)
        card.addView(category)
        card.addView(name)
        card.addView(price)
        card.addView(detail)

        return ProductViewHolder(
            card, image, name, category, price, detail
        )
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val product = products[position]

        holder.name.text = product.name
        holder.category.text = product.category
        holder.price.text = product.formattedPrice()

        Glide.with(holder.image.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_menu_gallery)
            .error(R.drawable.ic_menu_report_image)
            .centerInside()
            .into(holder.image)

        holder.card.setOnClickListener {
            onClick(product)
        }
    }

    override fun getItemCount(): Int = products.size

    companion object {
        private fun dp(context: Context, value: Int): Int {
            return (value * context.resources.displayMetrics.density)
                .toInt()
        }

        private fun roundedBackground(
            context: Context,
            color: Int,
            radius: Int
        ): GradientDrawable {
            return GradientDrawable().apply {
                setColor(color)
                cornerRadius = dp(context, radius).toFloat()
            }
        }
    }
}