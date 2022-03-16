package com.haman.aop_part6_chapter01.widget.adapter.listener.restaurant

import com.haman.aop_part6_chapter01.model.restaurant.RestaurantModel
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener

/**
 *  restaurant 클릭 listener
 */
interface RestaurantListListener: AdapterListener {
    fun onClickItem(model: RestaurantModel)
}