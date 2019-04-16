package com.anton.mercaritest.presentation.timeline.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anton.mercaritest.R
import com.anton.mercaritest.data.entity.PRODUCT_STATUS_SOLD_OUT
import com.anton.mercaritest.data.entity.Product
import com.anton.mercaritest.extensions.loadImage
import kotlinx.android.synthetic.main.item_product.view.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.properties.Delegates

class ProductsAdapter(private val onDetailsClick: (product: Product) -> Unit) :
    RecyclerView.Adapter<ProductsAdapter.ProductHolder>() {

    init {
        setHasStableIds(true)
    }

    var products: List<Product> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    private val priceFormatter = DecimalFormat().apply {
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = ','
        groupingSize = 3
        decimalFormatSymbols = symbols
    }


    override fun getItemId(position: Int): Long {
        return products[position].id.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(products[position])
    }

    inner class ProductHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onDetailsClick(products[pos])
                }
            }
        }

        fun bind(product: Product) {
            itemView.apply {
                productImage.loadImage(product.photo, R.drawable.product_img_placeholder)
                productName.text = product.name
                productNumComments.text = product.numComments.toString()
                productNumLikes.text = product.numLikes.toString()
                productPrice.text = "$ ${priceFormatter.format(product.price)}"

                productLabel.visibility = when (product.status) {
                    PRODUCT_STATUS_SOLD_OUT -> View.VISIBLE
                    else -> View.GONE
                }
            }
        }
    }
}