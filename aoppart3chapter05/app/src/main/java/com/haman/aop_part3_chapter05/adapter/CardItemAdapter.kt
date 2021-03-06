package com.haman.aop_part3_chapter05.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part3_chapter05.R
import com.haman.aop_part3_chapter05.model.CardItem

class CardItemAdapter : ListAdapter<CardItem, CardItemAdapter.CardItemViewHolder>(diffUtil) {

    inner class CardItemViewHolder(
        private val view : View
    ) : RecyclerView.ViewHolder(view) {

        fun bind (cardItem : CardItem) {
            view.findViewById<TextView>(R.id.nameTextView).text = cardItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CardItemViewHolder(
            inflater.inflate(R.layout.item_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CardItem>() {
            override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem) = oldItem.userId == newItem.userId
            override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem) = oldItem == newItem
        }
    }
}