package com.haman.aop_part3_chapter06.chatdetail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part3_chapter06.chatlist.ChatListItem
import com.haman.aop_part3_chapter06.databinding.ItemChatBinding
import com.haman.aop_part3_chapter06.databinding.ItemChatListBinding

class ChatItemAdapter : ListAdapter<ChatItem, ChatItemAdapter.ChatItemViewHolder>(diffUtil) {

    inner class ChatItemViewHolder(
        private val binding: ItemChatBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(chatItem: ChatItem) {
            binding.senderTextView.text = chatItem.senderId
            binding.messageTextView.text = chatItem.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        return ChatItemViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem)
                    = oldItem == newItem

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem)
                    = oldItem == newItem // data class 의 equals 는 생성자 프로퍼티 비교
        }
    }
}