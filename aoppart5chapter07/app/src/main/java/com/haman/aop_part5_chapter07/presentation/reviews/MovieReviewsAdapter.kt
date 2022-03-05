package com.haman.aop_part5_chapter07.presentation.reviews

import android.annotation.SuppressLint
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.haman.aop_part5_chapter07.databinding.ItemMovieInformationBinding
import com.haman.aop_part5_chapter07.databinding.ItemMyReviewBinding
import com.haman.aop_part5_chapter07.databinding.ItemReviewBinding
import com.haman.aop_part5_chapter07.databinding.ItemReviewFormBinding
import com.haman.aop_part5_chapter07.domain.model.Movie
import com.haman.aop_part5_chapter07.domain.model.Review
import com.haman.aop_part5_chapter07.extension.toAbbreviatedString
import com.haman.aop_part5_chapter07.extension.toDecimalFormatString

class MovieReviewsAdapter(
    private val movie: Movie
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var myReview: Review? = null // 내가 작성한 댓글
    var reviews: List<Review> = emptyList() // 다른 사용자가 작성한 댓글

    // 댓글 작성
    var onReviewSubmitButtonClickListener: ((content: String, score: Float) -> Unit)? = null

    // 댓글 삭제
    var onReviewDeleteButtonClickListener: ((Review) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> { // 영화 정보
                MovieInformationViewHolder(
                    ItemMovieInformationBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            ITEM_VIEW_TYPE_ITEM -> { // 다른 사용자가 작성한 댓글
                ReviewViewHolder(parent)
            }
            ITEM_VIEW_TYPE_REVIEW_FORM -> { // 댓글 작성 폼
                ReviewFormViewHolder(
                    ItemReviewFormBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            ITEM_VIEW_TYPE_MY_REVIEW -> { // 내가 작성한 댓글
                MyReviewViewHolder(
                    ItemMyReviewBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> throw RuntimeException("알 수 없는 ViewType 입니다.")
        }

    override fun getItemCount(): Int = 2 + reviews.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieInformationViewHolder -> {
                holder.bind(movie)
            }
            is ReviewViewHolder -> {
                // adapter position 에서는 2번째이므로 -2 필요
                holder.bind(reviews[position - 2])
            }
            is MyReviewViewHolder -> {
                myReview ?: return
                holder.bind(myReview!!)
            }
            is ReviewFormViewHolder -> Unit
            else -> throw RuntimeException("알 수 없는 ViewHolder 입니다.")
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (position) {
            0 -> ITEM_VIEW_TYPE_HEADER
            1 -> {
                if (myReview == null) {
                    ITEM_VIEW_TYPE_REVIEW_FORM
                } else {
                    ITEM_VIEW_TYPE_MY_REVIEW
                }
            }
            else -> ITEM_VIEW_TYPE_ITEM
        }

    /**
     * 영화 정보
     */
    class MovieInformationViewHolder(
        private val binding: ItemMovieInformationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Movie) {
            // 영화 이미지
            Glide.with(binding.root)
                .load(item.posterUrl)
                .into(binding.posterImageView)

            item.let {
                binding.averageScoreTextView.text =
                    "평점 ${it.averageScore?.toDecimalFormatString("0.0")} (${it.numberOfScore?.toAbbreviatedString()})"
                binding.titleTextView.text = it.title
                binding.additionalInformationTextView.text = "${it.releaseYear}·${it.country}"
                binding.relationsTextView.text = "감독: ${it.director}\n출연진: ${it.actors}"
                binding.genreChipGroup.removeAllViews()
                // 장르는 chip 으로 표현
                it.genre?.split(" ")?.forEach { genre ->
                    binding.genreChipGroup.addView(
                        Chip(binding.root.context).apply {
                            isClickable = false
                            text = genre
                        }
                    )
                }
            }
        }
    }

    /**
     * 리뷰 정보
     */
    inner class ReviewViewHolder(
        parent: ViewGroup,
        private val binding: ItemReviewBinding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: Review) {
            item.let {
                binding.authorIdTextView.text = "${it.userId?.take(3)}***"
                binding.scoreTextView.text = it.score?.toDecimalFormatString("0.0")
                binding.contentsTextView.text = "\"${it.content}\""
            }
        }
    }

    /**
     * 리뷰 작성 form
     */
    @SuppressLint("SetTextI18n")
    inner class ReviewFormViewHolder(private val binding: ItemReviewFormBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // 댓글 작성 버튼
            binding.submitButton.setOnClickListener {
                onReviewSubmitButtonClickListener?.invoke(
                    binding.reviewFieldEditText.text.toString(),
                    binding.ratingBar.rating
                )
            }
            binding.reviewFieldEditText.addTextChangedListener { editable ->
                binding.contentLimitTextView.text = "(${editable?.length ?: 0}/50)"
                binding.submitButton.isEnabled = (editable?.length ?: 0) >= 5
            }
        }
    }

    /**
     * 내가 작성한 댓글
     */
    inner class MyReviewViewHolder(private val binding: ItemMyReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteButton.setOnClickListener {
                onReviewDeleteButtonClickListener?.invoke(myReview!!)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: Review) {
            item.let {
                binding.scoreTextView.text = it.score?.toDecimalFormatString("0.0")
                binding.contentsTextView.text = "\"${it.content}\""
            }
        }
    }

    companion object {
        const val ITEM_VIEW_TYPE_HEADER = 0
        const val ITEM_VIEW_TYPE_ITEM = 1
        const val ITEM_VIEW_TYPE_REVIEW_FORM = 2
        const val ITEM_VIEW_TYPE_MY_REVIEW = 3
    }
}