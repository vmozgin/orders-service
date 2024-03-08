package com.example.orderservice;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import javax.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import order.OrderEvent;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@UtilityClass
public class KafkaConsumerUtils {

	public KafkaMessageListenerContainer<String, OrderEvent> setUpKafkaConsumer(
			@Nonnull final BlockingQueue<ConsumerRecord<String, OrderEvent>> consumerRecords,
			String...topics) {
		final var containerProperties = new ContainerProperties(topics);
		final Map<String, Object> consumerProperties = KafkaTestUtils.consumerProps(KafkaInitializer.getBootstrapSevers(), "sender", "false");
		consumerProperties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "PLAINTEXT");
		consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		final var consumer = new DefaultKafkaConsumerFactory<>(consumerProperties, new StringDeserializer(), new JsonDeserializer<>(OrderEvent.class));
		final var container = new KafkaMessageListenerContainer<>(consumer, containerProperties);
		container.setupMessageListener((MessageListener<String, OrderEvent>) consumerRecords::add);
		container.start();
		ContainerTestUtils.waitForAssignment(container, 1);
		return container;
	}
}
