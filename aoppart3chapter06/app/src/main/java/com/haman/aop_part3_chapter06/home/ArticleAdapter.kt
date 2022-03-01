package com.haman.aop_part3_chapter06.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haman.aop_part3_chapter06.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter (
    val onItemClicked : (ArticleModel) -> Unit
) : ListAdapter<ArticleModel, ArticleAdapter.ArticleViewHolder>(diffUtil) {

    inner class ArticleViewHolder(
        private val binding: ItemArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(articleModel: ArticleModel) {
            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(articleModel.createdAt)

            binding.titleTextView.text = articleModel.title
            binding.dateTextView.text = format.format(date).toString()
            binding.priceTextView.text = articleModel.content

            if (articleModel.imageUrlList.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(articleModel.imageUrlList.first())
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener {
                onItemClicked(articleModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel)
                = oldItem.createdAt == newItem.createdAt

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel)
                = oldItem == newItem // data class 의 equals 는 생성자 프로퍼티 비교
        }
    }
}