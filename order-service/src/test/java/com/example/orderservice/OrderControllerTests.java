package com.example.orderservice;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;


import com.example.orderservice.controller.OrderController;
import com.example.orderservice.model.OrderRequest;
import java.time.Duration;
import java.util.Collections;
import order.OrderEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/*@SpringBootTest
@Testcontainers
class OrderControllerTests {

	@Container
	static final KafkaContainer kafka = new KafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka:7.3.3")
	);

	@DynamicPropertySource
	static void registerKafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Autowired
	private OrderController orderController;
	@Autowired
	private ConsumerFactory<String, OrderEvent> consumerFactory;

	@Value("${app.kafka.orderTopic}")
	private String topicName;

	@Test
	public void whenOrderSend_thenMessageCorrectlyRecordedInOrderTopic() {
		Consumer<String, OrderEvent> consumer = consumerFactory.createConsumer("test-group-id", null);
		consumer.subscribe(Collections.singleton(topicName));

		OrderRequest request = new OrderRequest();
		request.setProduct("test_product");
		request.setQuantity(1);
		orderController.sendToKafka(request);

		ConsumerRecords<String, OrderEvent> records = consumer.poll(Duration.ofSeconds(10));

		Assertions.assertThat(records).isNotEmpty();
		ConsumerRecord<String, OrderEvent> record = records.iterator().next();
		Assertions.assertThat(record.value().getProduct()).isEqualTo(request.getProduct());
		Assertions.assertThat(record.value().getQuantity()).isEqualTo(request.getQuantity());
	}
}*/
