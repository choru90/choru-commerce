package com.qmit.commerce.order.api

import com.qmit.commerce.order.app.OrderApplicationService
import com.qmit.commerce.order.app.dto.PlaceOrderRequest
import com.qmit.commerce.order.infra.OrderRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(
    private val app: OrderApplicationService,
    private val repository: OrderRepository
) {

    @PostMapping
    fun place(@RequestBody req: PlaceOrderRequest) =
        ResponseEntity.ok(mapOf("orderId" to app.place(req)))

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) =
        repository.findById(id).map { ResponseEntity.ok(it) }.orElse(ResponseEntity.notFound().build())
}