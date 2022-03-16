package com.haman.aop_part6_chapter01.widget.adapter.listener.food

import com.haman.aop_part6_chapter01.model.food.FoodModel
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener

interface FoodMenuListListener: AdapterListener {
    fun onClickItem(model: FoodModel)
}