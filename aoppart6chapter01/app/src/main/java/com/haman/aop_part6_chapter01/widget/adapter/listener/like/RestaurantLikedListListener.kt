package com.haman.aop_part6_chapter01.widget.adapter.listener.like

import com.haman.aop_part6_chapter01.model.restaurant.RestaurantModel
import com.haman.aop_part6_chapter01.widget.adapter.listener.restaurant.RestaurantListListener

interface RestaurantLikedListListener: RestaurantListListener {

    fun onDislikeItem(model: RestaurantModel)

}