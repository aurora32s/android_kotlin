package com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.order

import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.databinding.ViewholderOrderMenuBinding
import com.haman.aop_part6_chapter01.extension.clear
import com.haman.aop_part6_chapter01.extension.load
import com.haman.aop_part6_chapter01.model.food.FoodModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import com.haman.aop_part6_chapter01.widget.adapter.listener.order.OrderMenuListListener
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.ModelViewHolder

class OrderMenuViewHolder(
    private val binding: ViewholderOrderMenuBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {
    override fun reset() {
        binding.foodImage.clear()
    }

    override fun bindData(model: FoodModel) {
        super.bindData(model)
        with(binding) {
            foodImage.load(model.imageUrl, 24f)
            foodTitleText.text = model.title
            foodDescriptionText.text = model.description
            priceText.text = resourceProvider.getString(R.string.price, model.price)
        }
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) {
        if (adapterListener is OrderMenuListListener) {
            binding.removeButton.setOnClickListener {
                adapterListener.onRemoveItem(model)
            }
        }
    }
}