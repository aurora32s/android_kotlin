package com.haman.aop_part6_chapter01.widget.adapter.listener.order

import com.haman.aop_part6_chapter01.model.food.FoodModel
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener

/**
 * 장바구니 메뉴 클릭 리스너
 */
interface OrderMenuListListener: AdapterListener {
    fun onRemoveItem(foodModel: FoodModel)
}