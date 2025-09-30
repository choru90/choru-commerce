package com.qmit.commerce.payment

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.qmit.commerce.common.events.DomainEventPublisher
import com.qmit.commerce.common.events.InventoryReserved
import com.qmit.commerce.common.events.PaymentAuthorized
import com.qmit.commerce.order.domain.OrderStatus
import com.qmit.commerce.order.infra.OrderRepository
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PaymentPolicy(
    private val publisher: DomainEventPublisher,
    private val repository: OrderRepository
) {

    private val mapper = jacksonObjectMapper()

    fun onInventoryReserved(@Payload bytes: ByteArray) {
        val event: InventoryReserved = mapper.readValue(bytes)
        repository.findById(event.orderId).ifPresent {
            it.status = OrderStatus.INVENTORY_RESERVED
            repository.save(it)
            publisher.publish(
                PaymentAuthorized(
                    orderId = event.orderId,
                    paymentId = "pay-" + UUID.randomUUID(),
                    amount = it.totalAmount
                )
            )
        }
    }
}