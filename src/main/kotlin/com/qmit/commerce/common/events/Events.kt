package com.qmit.commerce.common.events

import com.fasterxml.jackson.annotation.Nulls
import java.time.Instant
import java.util.UUID


data class EventMetadata(
    val eventId: UUID = UUID.randomUUID(),
    val occurredAt: Instant = Instant.now(),
    val traceId: String? = null,
    val schemaName: String,
    val schemaType: String = "JSON",
    val schemaVersion: Int = 1,
)

sealed interface DomainEvent { val meta: EventMetadata }

data class OrderItem(val sku: String, val quantity: Int, val unitPrice: Long)

data class OrderPlaced(
    override val meta: EventMetadata = EventMetadata(schemaName = "OrderPlaced"),
    val orderId: String,
    val userId: String,
    val items: List<OrderItem>,
    val totalAmount: Long,
): DomainEvent

data class InventoryReserved(
    override val meta: EventMetadata = EventMetadata(schemaName = "InventoryReserved"),
    val orderId: String,
    val reservedAt: String,
): DomainEvent

data class PaymentAuthorized(
    override val meta: EventMetadata = EventMetadata(schemaName = "PaymentAuthorized"),
    val orderId: String,
    val paymentId: String,
    val amount: Long
): DomainEvent

data class DeliveryAssigned(
    override val meta: EventMetadata = EventMetadata(schemaName = "DeliveryAssigned"),
    val orderId: String,
    val deliveryId: String,
    val etaMinutes: Int
): DomainEvent