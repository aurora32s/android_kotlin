package com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.restaurant

import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.databinding.ViewholderRestaurantBinding
import com.haman.aop_part6_chapter01.extension.clear
import com.haman.aop_part6_chapter01.extension.load
import com.haman.aop_part6_chapter01.model.restaurant.RestaurantModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import com.haman.aop_part6_chapter01.widget.adapter.listener.restaurant.RestaurantListListener
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourceProvider: ResourcesProvider
) : ModelViewHolder<RestaurantModel>(
    binding, viewModel, resourceProvider
) {
    override fun reset() = with(binding) {
        restaurantImage.clear()
    }

    override fun bindData(model: RestaurantModel) = with(binding) {
        super.bindData(model)
        restaurantImage.load(model.restaurantImageUrl, 24f)
        restaurantTitleText.text = model.restaurantTitle
        gradeText.text = resourceProvider.getString(R.string.grade_format, model.grade)
        reviewCountText.text = resourceProvider.getString(R.string.review_count, model.reviewCount)

        val (minTime, maxTime) = model.deliveryTimeRange
        deliveryTimeText.text = resourceProvider.getString(R.string.delivery_time, minTime, maxTime)

        val (minTip, maxTip) = model.deliveryTipRange
        deliveryTipText.text = resourceProvider.getString(R.string.delivery_tip, minTip, maxTip)
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) =
        with(binding) {
            if (adapterListener is RestaurantListListener) {
                root.setOnClickListener {
                    adapterListener.onClickItem(model)
                }
            }
        }
}