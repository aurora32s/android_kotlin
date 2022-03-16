package com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.review

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.haman.aop_part6_chapter01.databinding.ViewholderReviewBinding
import com.haman.aop_part6_chapter01.extension.clear
import com.haman.aop_part6_chapter01.extension.load
import com.haman.aop_part6_chapter01.model.review.ReviewModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.ModelViewHolder

class ReviewViewHolder(
    private val binding: ViewholderReviewBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<ReviewModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        reviewThumbnailImage.clear()
        reviewThumbnailImage.isGone = true
    }

    override fun bindData(model: ReviewModel){
        super.bindData(model)
        with(binding) {
            reviewTitleText.text = model.title
            ratingBar.rating = model.grade.toFloat()
            reviewText.text = model.description

            if (model.thumbnailImageUri != null) {
                reviewThumbnailImage.isVisible = true
                reviewThumbnailImage.load(model.thumbnailImageUri.toString(), 24f)
            } else {
                reviewThumbnailImage.isGone = true
            }
        }
    }

    override fun bindViews(model: ReviewModel, adapterListener: AdapterListener) {

    }

}