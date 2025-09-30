package com.qmit.commerce.inventory

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.qmit.commerce.common.events.DomainEventPublisher
import com.qmit.commerce.common.events.InventoryReserved
import com.qmit.commerce.common.events.OrderPlaced
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class InventoryPolicy(
    private val publisher: DomainEventPublisher
) {
    private val mapper = jacksonObjectMapper()

    @KafkaListener(topics = ["\${topics.order:commerce.order.events"], groupId = "inventory")
    fun onOrderPlaced(@Payload bytes: ByteArray) {
        val event: OrderPlaced = mapper.readValue(bytes)
        publisher.publish(
            InventoryReserved(
                orderId = event.orderId,
                reservedAt = Instant.now().toString(),
            )
        )
    }
}