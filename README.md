# Saga Orchestration (Spring Boot + Kafka, Java 19) 

This is a demo multi-module Maven project implementing a Saga **Orchestration** pattern
using Spring Boot and Apache Kafka.

## Modules
- `kafka-common` — shared event classes & topic names
- `orchestrator-service` — exposes REST `/api/orders` to start a saga and coordinates via Kafka
- `order-service` — tracks order status (in-memory)
- `inventory-service` — simple in-memory stock, reserves/releases
- `payment-service` — mock payment
- `shipping-service` — mock shipping

## Run
1. Start Kafka locally on `localhost:9092` (e.g., Confluent Platform or Docker).
2. Build: `mvn -q -DskipTests package`
3. In separate terminals, run each service:
   - `mvn -pl orchestrator-service spring-boot:run`
   - `mvn -pl order-service spring-boot:run`
   - `mvn -pl inventory-service spring-boot:run`
   - `mvn -pl payment-service spring-boot:run`
   - `mvn -pl shipping-service spring-boot:run`

## Create an order
```bash
curl -X POST "http://localhost:8080/api/orders?userId=u1&itemName=item&quantity=1&amount=100"
```

Check order statuses:
```bash
curl "http://localhost:8081/orders"
```

Check inventory snapshot:
```bash
curl "http://localhost:8082/inventory"
```
