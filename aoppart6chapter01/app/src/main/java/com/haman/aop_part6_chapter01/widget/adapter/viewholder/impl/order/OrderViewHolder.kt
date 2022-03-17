package com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.order

import com.haman.aop_part6_chapter01.R
import com.haman.aop_part6_chapter01.databinding.ViewholderOrderBinding
import com.haman.aop_part6_chapter01.model.order.OrderModel
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener
import com.haman.aop_part6_chapter01.widget.adapter.listener.order.OrderListListener
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderModel>(binding, viewModel, resourcesProvider) {
    override fun reset() {
    }

    override fun bindData(model: OrderModel) {
        super.bindData(model)
        with(binding) {
            orderTitleText.text =
                resourceProvider.getString(R.string.order_history_title, model.orderId)
            val foodMenuList = model.foodMenuList

            var orderDataStr = ""
            foodMenuList
                .groupBy { it.title }
                .entries.forEach { (title, menuList) ->
                        orderDataStr += "메뉴 : $title | 가격 : ${menuList.first().price}원 X ${menuList.size}\n"
                }
            orderContentText.text = orderDataStr

            orderTotalPriceText.text =
                resourceProvider.getString(
                    R.string.price,
                    foodMenuList.map { it.price }.reduce { total, price -> total + price }
                )
        }
    }

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) {
        if (adapterListener is OrderListListener) {
            binding.root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }
}