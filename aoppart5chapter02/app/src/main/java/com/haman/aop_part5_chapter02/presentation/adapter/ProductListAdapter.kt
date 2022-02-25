package com.haman.aop_part5_chapter02.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part5_chapter02.data.entity.product.ProductEntity
import com.haman.aop_part5_chapter02.databinding.ItemProductBinding
import com.haman.aop_part5_chapter02.extensions.loadCenterCrop

class ProductListAdapter: RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    private var productList: List<ProductEntity> = emptyList()
    private var productItemClickListener: (ProductEntity) -> Unit = {}

    inner class ProductViewHolder(
        val binding: ItemProductBinding
    ): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(productEntity: ProductEntity) = with(binding) {
            productNameTextView.text = productEntity.productName
            productPriceTextView.text = "${productEntity.productPrice}원"
            productImageView.loadCenterCrop(
                url = productEntity.productIntroductionImage, 8f
            )

            root.setOnClickListener {
                productItemClickListener(productEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(productList: List<ProductEntity>, productItemClickListener: (ProductEntity)->Unit = {}) {
        this.productList = productList
        this.productItemClickListener = productItemClickListener
        notifyDataSetChanged()
    }
}