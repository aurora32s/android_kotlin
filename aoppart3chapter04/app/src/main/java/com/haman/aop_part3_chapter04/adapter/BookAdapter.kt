package com.haman.aop_part3_chapter04.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haman.aop_part3_chapter04.databinding.ItemBookBinding
import com.haman.aop_part3_chapter04.model.Book

class BookAdapter(
    private val itemClickedListener : (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil) {
    // item_book <-> ItemBook + Binding
    inner class BookItemViewHolder(
        private val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.titleTextView.text = book.title
            binding.descriptionTextView.text = book.description
            Glide.with(binding.coverImageView)
                .load(book.coverSmallUrl)
                .into(binding.coverImageView)
            binding.root.setOnClickListener {
                itemClickedListener(book)
            }
        }
    }

    // viewHolder : recycler view 가 생성해둔 리스트 아이템
    // 해당 viewHolder 가 생성되어 있지 않은 경우, 새로 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    // 데이터와 viewHolder 는 연결
    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        // 다른 내용이여서 새로운 viewHolder 를 생성해 주어야 함. 이때 다름의 척도를 정의
        val diffUtil = object : DiffUtil.ItemCallback<Book>() {
            // 같은 아이템인가
            override fun areItemsTheSame(oldItem: Book, newItem: Book) = oldItem == newItem

            // 같은 내용을 가지고 있는가
            override fun areContentsTheSame(oldItem: Book, newItem: Book) = oldItem.id == newItem.id

        }
    }
}