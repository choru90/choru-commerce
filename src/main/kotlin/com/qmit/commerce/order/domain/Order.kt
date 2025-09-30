package com.qmit.commerce.order.domain

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    @Id val id: String,
    val userId: String,
    var totalAmount: Long,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.CREATED,
)

enum class OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    PAYMENT_AUTHORIZED,
    DELIVERY_ASSIGNED,
    COMPLETED,
    CANCELLED
}