# Kafka Staking Events

A small **Apache Kafka** example (Java + `kafka-clients`) that publishes and consumes staking events, with an integration test that spins up a **real Kafka broker** via [Testcontainers](https://testcontainers.com/) and round-trips a message.

`StakingEventBus` wraps the producer and consumer: `publish(topic, key, value)` sends a keyed event and blocks for the ack, and `consumeOne(...)` reads from the beginning until an event arrives or a timeout elapses.

## Test

```bash
mvn test
```

The test starts a Kafka container (`apache/kafka:3.8.0`, KRaft mode), publishes `user:42 -> staked:1000`, consumes it back, and asserts the key and value round-trip. It runs in CI on every push (GitHub's runners provide Docker for Testcontainers).

## Files

```
src/main/java/com/liander/StakingEventBus.java        producer + consumer wrapper
src/test/java/com/liander/StakingEventBusTest.java     Testcontainers integration test
pom.xml                                                 Maven build
```

## Stack

- **Apache Kafka** (`kafka-clients`)
- **Testcontainers** (real broker in Docker), **JUnit 5**, **Maven**

## License

MIT
