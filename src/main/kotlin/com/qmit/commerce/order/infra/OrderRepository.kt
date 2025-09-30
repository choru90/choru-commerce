package com.qmit.commerce.order.infra

import com.qmit.commerce.order.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, String>