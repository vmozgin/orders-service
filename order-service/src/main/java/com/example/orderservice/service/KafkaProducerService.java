package com.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import order.OrderEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	@Value("${app.kafka.orderTopic}")
	private String orderTopic;

	private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

	public void sendMessage(OrderEvent event) {
		kafkaTemplate.send(orderTopic, event);
	}
}
