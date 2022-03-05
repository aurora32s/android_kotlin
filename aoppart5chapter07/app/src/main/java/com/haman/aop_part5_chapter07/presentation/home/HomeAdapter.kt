package com.haman.aop_part5_chapter07.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haman.aop_part5_chapter07.databinding.ItemFeaturedMovieBinding
import com.haman.aop_part5_chapter07.databinding.ItemMovieBinding
import com.haman.aop_part5_chapter07.domain.model.FeaturedMovie
import com.haman.aop_part5_chapter07.domain.model.Movie
import com.haman.aop_part5_chapter07.extension.dip
import com.haman.aop_part5_chapter07.extension.toAbbreviatedString
import com.haman.aop_part5_chapter07.extension.toDecimalFormatString

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<DataItem> = emptyList() // 영화 리스트
    var onMovieClickListener: ((Movie) -> Unit)? = null // 영화 클릭 이벤트

    /**
     * item type 에 맞게 view 생성
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_SECTION_HEADER -> { // header
                TitleItemViewHolder(parent.context)
            }
            ITEM_VIEW_TYPE_FEATURED -> { // 추천 영화
                FeaturedMovieItemViewHolder(
                    ItemFeaturedMovieBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            ITEM_VIEW_TYPE_ITEM -> { // 일반 영화
                MovieItemViewHolder(
                    ItemMovieBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> throw RuntimeException("알 수 없는 ViewType 입니다.")
        }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemValue = data[position].value
        when {
            // holder 와 value type 도 함께 검사
            holder is TitleItemViewHolder && itemValue is String -> {
                holder.bind(itemValue)
            }
            holder is FeaturedMovieItemViewHolder && itemValue is FeaturedMovie -> {
                holder.bind(itemValue)
            }
            holder is MovieItemViewHolder && itemValue is Movie -> {
                holder.bind(itemValue)
            }
            else -> throw RuntimeException("알 수 없는 ViewHolder 입니다.")
        }
    }

    override fun getItemViewType(position: Int): Int = when (data[position].value) {
        is String -> { // 문자인경우
            ITEM_VIEW_TYPE_SECTION_HEADER
        }
        is FeaturedMovie -> { // 추천 영화 객체인 경우
            ITEM_VIEW_TYPE_FEATURED
        }
        else -> { // 영화 객체인 경우
            ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addData(featuredMovie: FeaturedMovie?, movies: List<Movie>) {
        val newData = mutableListOf<DataItem>()

        // 추천 영화
        featuredMovie?.let {
            newData += DataItem("🔥 요즘 핫한 영화")
            newData += DataItem(it)
        }
        // 일반 영화
        newData += DataItem("🍿 이 영화들은 보셨나요?")
        newData += movies.map { DataItem(it) }

        data = newData
    }

    /**
     * 화면 상단 title
     */
    inner class TitleItemViewHolder(context: Context) : RecyclerView.ViewHolder(
        TextView(context).apply {
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.BLACK)
            setPadding(dip(12f), dip(6f), dip(12f), dip(6f))
        }
    ) {
        fun bind(item: String) {
            (itemView as? TextView)?.text = item
        }
    }

    /**
     * 추천 영화 view holder
     */
    inner class FeaturedMovieItemViewHolder(private val binding: ItemFeaturedMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                // FeaturedMovie 인지 방어 코드 추가
                (data[adapterPosition].value as? FeaturedMovie)?.movie?.let {
                    onMovieClickListener?.invoke(it)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: FeaturedMovie) {
            Glide.with(binding.root)
                .load(item.movie.posterUrl)
                .into(binding.posterImageView)

            binding.scoreCountTextView.text = item.movie.numberOfScore?.toAbbreviatedString()
            binding.averageScoreTextView.text = item.movie.averageScore?.toDecimalFormatString("0.0")

            item.latestReview?.let { review ->
                binding.latestReviewLabelTextView.text =
                    if (review.userId.isNullOrBlank()) {
                        "🌟 따끈따끈한 후기"
                    } else {
                        "- ${review.userId.take(3)}*** -"
                    }

                binding.latestReviewTextView.text = "\"${review.content}\""
            }
        }
    }

    /**
     * 일반 영화 view holder
     */
    inner class MovieItemViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                (data[adapterPosition].value as? Movie)?.let {
                    onMovieClickListener?.invoke(it)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            Glide.with(binding.root)
                .load(movie.posterUrl)
                .into(binding.posterImageView)

            movie.let {
                binding.titleTextView.text = it.title
                binding.additionalInformationTextView.text = "${it.releaseYear}·${it.country}"
            }
        }
    }

    data class DataItem(val value: Any)

    companion object {
        const val ITEM_VIEW_TYPE_SECTION_HEADER = 0
        const val ITEM_VIEW_TYPE_FEATURED = 1
        const val ITEM_VIEW_TYPE_ITEM = 2
    }
}