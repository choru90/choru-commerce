package com.qmit.commerce.order.app

import com.qmit.commerce.common.events.DomainEventPublisher
import com.qmit.commerce.common.events.OrderPlaced
import com.qmit.commerce.order.app.dto.PlaceOrderRequest
import com.qmit.commerce.order.domain.Order
import com.qmit.commerce.order.infra.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class OrderApplicationService(
    private val repository: OrderRepository,
    private val publisher: DomainEventPublisher
) {

    @Transactional
    fun place(req: PlaceOrderRequest): String {
        val orderId = UUID.randomUUID().toString()
        val total = req.items.sumOf { it.unitPrice * it.quantity }
        repository.save(Order(id = orderId, userId = req.userId, totalAmount = total))
        publisher.publish(OrderPlaced(
            orderId = orderId, userId = req.userId, items = req.items, totalAmount = total,
        ))
        return orderId
    }

}