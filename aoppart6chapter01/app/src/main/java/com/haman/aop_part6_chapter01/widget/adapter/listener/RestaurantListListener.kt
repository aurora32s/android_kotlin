package com.haman.aop_part6_chapter01.widget.adapter.listener

import com.haman.aop_part6_chapter01.model.restaurant.RestaurantModel

/**
 *  restaurant 클릭 listener
 */
interface RestaurantListListener: AdapterListener {
    fun onClickItem(model: RestaurantModel)
}