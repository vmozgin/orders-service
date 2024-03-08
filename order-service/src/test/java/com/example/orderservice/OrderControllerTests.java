package com.example.orderservice;

import com.example.orderservice.model.OrderRequest;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import order.OrderEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {KafkaInitializer.class})
class OrderControllerTests {

	private KafkaMessageListenerContainer<String, OrderEvent> container;
	private final BlockingQueue<ConsumerRecord<String, OrderEvent>> consumerRecords = new LinkedBlockingQueue<>();

	@Autowired
	private TestRestTemplate restTemplate;

	@Value("${app.kafka.orderTopic}")
	private String topicName;

	@BeforeAll
	void setUpKafkaConsumer() {
		container = KafkaConsumerUtils.setUpKafkaConsumer(consumerRecords, topicName);
	}

	@AfterAll
	void tearDownKafkaConsumer() {
		if (container != null) {
			container.stop();
			container = null;
		}
	}

	@SneakyThrows
	@Test
	public void whenOrderSend_thenMessageCorrectlyRecordedInOrderTopic() {
		OrderRequest request = new OrderRequest();
		request.setProduct("test_product");
		request.setQuantity(1);

		ResponseEntity<Void> response = restTemplate.postForEntity("/api/order/send", request, Void.class);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

		ConsumerRecord<String, OrderEvent> record = consumerRecords.poll(10, TimeUnit.SECONDS);

		Assertions.assertThat(record).isNotNull();
		Assertions.assertThat(record.value().getProduct()).isEqualTo(request.getProduct());
		Assertions.assertThat(record.value().getQuantity()).isEqualTo(request.getQuantity());
	}
}
