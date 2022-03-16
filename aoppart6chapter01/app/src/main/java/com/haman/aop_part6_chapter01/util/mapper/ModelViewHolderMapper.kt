package com.haman.aop_part6_chapter01.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.haman.aop_part6_chapter01.databinding.*
import com.haman.aop_part6_chapter01.model.CellType
import com.haman.aop_part6_chapter01.model.Model
import com.haman.aop_part6_chapter01.screen.base.BaseViewModel
import com.haman.aop_part6_chapter01.screen.order.OrderMenuListViewModel
import com.haman.aop_part6_chapter01.util.provider.ResourcesProvider
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.ModelViewHolder
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.EmptyViewHolder
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.food.FoodMenuViewHolder
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.like.RestaurantLikedListViewHolder
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.order.OrderMenuViewHolder
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.order.OrderViewHolder
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.restaurant.RestaurantViewHolder
import com.haman.aop_part6_chapter01.widget.adapter.viewholder.impl.review.ReviewViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.REVIEW_CELL -> ReviewViewHolder(
                ViewholderReviewBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.LIKED_RESTAURANT_CELL ->  RestaurantLikedListViewHolder(
                ViewholderRestaurantLikedBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_FOOD_CELL -> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
            CellType.ORDER_CELL -> OrderViewHolder(
                ViewholderOrderBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }

}