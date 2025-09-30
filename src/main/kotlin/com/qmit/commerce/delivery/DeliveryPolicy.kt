package com.qmit.commerce.delivery

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.qmit.commerce.common.events.PaymentAuthorized
import com.qmit.commerce.order.domain.OrderStatus
import com.qmit.commerce.order.infra.OrderRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DeliveryPolicy(
    private val repository: OrderRepository,
) {
    private val mapper = jacksonObjectMapper()

    @KafkaListener(topics = ["\${topics.payment:commerce.payment.events}"], groupId = "delivery")
    @Transactional
    fun onPaymentAuthorized(@Payload bytes: ByteArray) {
        val event: PaymentAuthorized = mapper.readValue(bytes)
        repository.findById(event.orderId).ifPresent {
            it.status = OrderStatus.PAYMENT_AUTHORIZED
            repository.save(it)
            it.status = OrderStatus.DELIVERY_ASSIGNED
            repository.save(it)
        }
    }
}