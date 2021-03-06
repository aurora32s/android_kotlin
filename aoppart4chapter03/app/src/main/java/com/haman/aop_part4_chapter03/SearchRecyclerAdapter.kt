package com.haman.aop_part4_chapter03

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.haman.aop_part4_chapter03.databinding.ViewholderSearchResultItemBinding
import com.haman.aop_part4_chapter03.model.SearchResultEntity

class SearchRecyclerAdapter(
    private val searchResultClickListener: (SearchResultEntity) -> Unit
) : RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultItemViewHolder>() {

    private var searchResultLists: List<SearchResultEntity> = emptyList()

    inner class SearchResultItemViewHolder(
        private val view: ViewholderSearchResultItemBinding
    ) : RecyclerView.ViewHolder(view.root) {

        fun bind(searchResultEntity: SearchResultEntity) = with(view) {
            titleTextView.text = searchResultEntity.name
            subTitleTextView.text = searchResultEntity.fullAddress

            view.root.setOnClickListener {
                searchResultClickListener(searchResultEntity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultItemViewHolder {
        return SearchResultItemViewHolder(
            ViewholderSearchResultItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int) {
        holder.bind(searchResultLists[position])
    }

    override fun getItemCount() = searchResultLists.size

    @SuppressLint("NotifyDataSetChanged")
    fun setSearchResultList(results: List<SearchResultEntity>) {
        this.searchResultLists = results
        this.notifyDataSetChanged()
    }
}