package com.haman.aop_part3_chapter06.chatlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haman.aop_part3_chapter06.databinding.ItemArticleBinding
import com.haman.aop_part3_chapter06.databinding.ItemChatListBinding
import java.text.SimpleDateFormat
import java.util.*

class ChatListAdapter (
    val onItemClicked : (ChatListItem) -> Unit
) : ListAdapter<ChatListItem, ChatListAdapter.ChatListViewHolder>(diffUtil) {

    inner class ChatListViewHolder(
        private val binding: ItemChatListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(chatListItem: ChatListItem) {
            binding.chatRoomTitleTextView.text = chatListItem.itemsTitle
            binding.root.setOnClickListener {
                onItemClicked(chatListItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatListItem>() {
            override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem)
                = oldItem.key == newItem.key

            override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem)
                = oldItem == newItem // data class 의 equals 는 생성자 프로퍼티 비교
        }
    }
}