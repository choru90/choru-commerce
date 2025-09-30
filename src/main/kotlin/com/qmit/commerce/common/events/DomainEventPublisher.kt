package com.qmit.commerce.common.events

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class DomainEventPublisher(
    private val template: KafkaTemplate<String, ByteArray>,
) {

    private val mapper: ObjectMapper = jacksonObjectMapper()
    @Value("\${topics.order:commerce.order.events}") lateinit var orderTopic: String
    @Value("\${topics.inventory:commerce.inventory.events}") lateinit var inventoryTopic: String
    @Value("\${topics.payment:commerce.payment.events}") lateinit var paymentTopic: String
    @Value("\${topics.delivery:commerce.delivery.events}") lateinit var deliveryTopic: String


    fun publish(event: DomainEvent) {
        val (topic, key) = when(event) {
            is OrderPlaced -> orderTopic to event.orderId
            is InventoryReserved -> inventoryTopic to event.orderId
            is PaymentAuthorized -> paymentTopic to event.orderId
            is DeliveryAssigned -> deliveryTopic to event.orderId
        }
        template.send(topic, key, mapper.writeValueAsBytes(event))
    }
}