package com.haman.aop_part3_chapter02

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * ViewPager2 가 RecyclerView 기반으로 구현되어 있으므로
 */
class QuotePagerAdapter (
    private val quotes : List<Quote>,
    private val isNameRevealed : Boolean
): RecyclerView.Adapter<QuotePagerAdapter.QuoterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote, parent, false)
        )

    override fun onBindViewHolder(holder: QuoterViewHolder, position: Int) {
        holder.bind(quotes[position % quotes.size], isNameRevealed)
    }

    override fun getItemCount() = Int.MAX_VALUE

    // 중첩 class
    class QuoterViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val quoteTextView : TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView : TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n") // 번역과 관련된 처리
        fun bind (quote : Quote, isNameRevealed: Boolean) {
            quoteTextView.text = "\"${quote.quote}\""

            if (isNameRevealed) {
                nameTextView.text = "-${quote.name}"
                nameTextView.visibility = View.VISIBLE
            } else {
                nameTextView.visibility = View.GONE
            }
        }
    }
}