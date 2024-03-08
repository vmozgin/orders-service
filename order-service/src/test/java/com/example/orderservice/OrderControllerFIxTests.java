package com.example.orderservice;

import com.example.orderservice.controller.OrderController;
import com.example.orderservice.model.OrderRequest;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import order.OrderEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class OrderControllerFIxTests {

	@Container
	static final KafkaContainer kafka =
			new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.3"));

	@DynamicPropertySource
	static void registerKafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Autowired
	private OrderController orderController;

	@Value("${app.kafka.orderTopic}")
	private String topicName;

	@Test
	public void whenOrderSend_thenMessageCorrectlyRecordedInOrderTopic() {
		Properties consumerProps = new Properties();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		Consumer<String, OrderEvent> consumer = new KafkaConsumer<>(consumerProps);
		consumer.subscribe(Collections.singletonList(topicName));

		OrderRequest request = new OrderRequest();
		request.setProduct("test_product");
		request.setQuantity(1);
		orderController.sendToKafka(request);

		ConsumerRecord<String, OrderEvent> record = consumer.poll(Duration.ofSeconds(10)).iterator().next();

		Assertions.assertThat(record.value().getProduct()).isEqualTo(request.getProduct());
		Assertions.assertThat(record.value().getQuantity()).isEqualTo(request.getQuantity());

	}
}
