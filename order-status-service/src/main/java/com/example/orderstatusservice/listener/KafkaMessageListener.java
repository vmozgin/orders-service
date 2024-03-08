package com.example.orderstatusservice.listener;

import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import order.OrderEvent;
import order.OrderStatusEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageListener {

	@Value("${app.kafka.orderStatusTopic}")
	private String orderStatusTopic;

	private final KafkaTemplate<String, OrderStatusEvent> kafkaTemplate;

	@KafkaListener(topics = "${app.kafka.orderTopic}",
			groupId = "${app.kafka.kafkaMessageGroupId}",
			containerFactory = "kafkaMessageKafkaListenerContainerFactory")
	public void listen(@Payload OrderEvent message,
					   @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key,
					   @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
					   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
					   @Header(KafkaHeaders.RECEIVED_TIMESTAMP) Long timestamp) {
		log.info("Received message: {}", message);
		log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestamp);

		OrderStatusEvent orderStatusEvent = new OrderStatusEvent("CREATED", Instant.now());
		kafkaTemplate.send(orderStatusTopic, orderStatusEvent);
	}
}
