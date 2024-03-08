package com.example.orderservice.controller;

import com.example.orderservice.model.OrderRequest;
import com.example.orderservice.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import order.OrderEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final KafkaProducerService kafkaProducerService;

	@PostMapping("/send")
	public ResponseEntity<Void> sendToKafka(@RequestBody OrderRequest request) {
		OrderEvent event = new OrderEvent();
		event.setProduct(request.getProduct());
		event.setQuantity(request.getQuantity());

		kafkaProducerService.sendMessage(event);

		return ResponseEntity.ok().build();
	}
}
