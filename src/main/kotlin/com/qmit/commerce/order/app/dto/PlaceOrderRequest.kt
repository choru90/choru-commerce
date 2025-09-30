package com.qmit.commerce.order.app.dto

import com.qmit.commerce.common.events.OrderItem

data class PlaceOrderRequest(
    val userId: String,
    val items: List<OrderItem>
)
