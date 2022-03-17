package com.haman.aop_part6_chapter01.widget.adapter.listener.order

import com.haman.aop_part6_chapter01.model.order.OrderModel
import com.haman.aop_part6_chapter01.widget.adapter.listener.AdapterListener

/**
 * 주문내역 아이템 클릭 리스너
 */
interface OrderListListener: AdapterListener {
    fun onClickItem(orderModel: OrderModel)
}