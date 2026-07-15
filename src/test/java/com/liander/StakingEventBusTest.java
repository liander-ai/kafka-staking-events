package com.liander;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

/** Round-trips a staking event through a real Kafka broker started by Testcontainers. */
@Testcontainers
class StakingEventBusTest {

    @Container
    static final KafkaContainer KAFKA =
            new KafkaContainer(DockerImageName.parse("apache/kafka:3.8.0"));

    @Test
    void publishesAndConsumesAStakingEvent() throws Exception {
        StakingEventBus bus = new StakingEventBus(KAFKA.getBootstrapServers());

        bus.publish("staking-events", "user:42", "staked:1000");

        ConsumerRecord<String, String> record =
                bus.consumeOne("staking-events", "test-group", Duration.ofSeconds(20));

        assertNotNull(record, "expected to consume the published event");
        assertEquals("user:42", record.key());
        assertEquals("staked:1000", record.value());
    }
}
