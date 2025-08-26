package com.example.saga.orchestratorservice.saga;

import com.example.saga.common.Topics;
import com.example.saga.common.events.InventoryEvents;
import com.example.saga.common.events.PaymentEvents;
import com.example.saga.common.events.ShippingEvents;
import com.example.saga.common.events.OrderEvents;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrchestratorListeners {

    private final KafkaTemplate<String, Object> kafka;

    public OrchestratorListeners(KafkaTemplate<String,Object> kafka){
        this.kafka = kafka;
    }


    /** Step 1: Order created */
    @KafkaListener(topics = Topics.ORDER_CREATED, groupId = "orchestrator")
    public void handleOrderCreated(OrderEvents.OrderCreated event) {
        System.out.println("🟢 Orchestrator: OrderCreated " + event.orderId());

        // Trigger Inventory
        kafka.send(Topics.INVENTORY_RESERVE,
                new InventoryEvents.ReserveInventory(event.orderId(), event.itemName(), event.quantity(),event.amount()));
    }

    /** Step 2: Inventory reserved */
    @KafkaListener(topics = Topics.INVENTORY_RESERVED, groupId = "orchestrator")
    public void handleInventoryReserved(InventoryEvents.InventoryReserved event) {
        System.out.println("🟢 Orchestrator: InventoryReserved " + event.orderId());

        // Trigger Payment
        kafka.send(Topics.PAYMENT_PROCESS,
                new PaymentEvents.ProcessPayment(event.orderId(), "demo-user" ,event.amount(),event.quantity()));
    }

    /** Step 2b: Inventory failed */
    @KafkaListener(topics = Topics.INVENTORY_FAILED, groupId = "orchestrator")
    public void handleInventoryFailed(InventoryEvents.InventoryFailed event) {
        System.out.println("❌ Orchestrator: InventoryFailed " + event.orderId());

        // Compensating action: cancel order
        kafka.send(Topics.ORDER_CANCELLED,
                new OrderEvents.OrderCancelled(event.orderId(), event.reason()));
    }
    /** Step 3: Payment successful */
    @KafkaListener(topics = Topics.PAYMENT_PROCESSED, groupId = "orchestrator")
    public void handlePaymentSuccess(PaymentEvents.ProcessPayment event) {
        System.out.println("🟢 Orchestrator: PaymentSuccess " + event.orderId());

        // Trigger Shipping
        kafka.send(Topics.SHIPPING_SCHEDULE,
                new ShippingEvents.ScheduleShipping(event.orderId(), "demo-user", event.quantity()));
    }

    /** Step 3b: Payment failed */
    @KafkaListener(topics = Topics.PAYMENT_FAILED, groupId = "orchestrator")
    public void handlePaymentFailed(PaymentEvents.PaymentFailed event) {
        System.out.println("❌ Orchestrator: PaymentFailed " + event.orderId());

        // Compensating actions:
        // 1. Cancel shipping (if already triggered)
        // 2. Release inventory
        kafka.send(Topics.INVENTORY_RELEASE,
                new InventoryEvents.ReleaseInventory(event.orderId(), event.name(), event.quantity()));

        // Cancel order
        kafka.send(Topics.ORDER_CANCELLED,
                new OrderEvents.OrderCancelled(event.orderId(), event.reason()));
    }

    /** Step 4: Shipping success */
    @KafkaListener(topics = Topics.SHIPPING_SCHEDULED, groupId = "orchestrator")
    public void handleShippingSuccess(ShippingEvents.ShippingScheduled event) {
        System.out.println("🟢 Orchestrator: ShippingSuccess " + event.orderId());

        // Complete the order
        kafka.send(Topics.ORDER_COMPLETED,
                new OrderEvents.OrderCompleted(event.orderId(), "Order completed successfully"));
    }

    /** Step 4b: Shipping failed */
    @KafkaListener(topics = Topics.SHIPPING_FAILED, groupId = "orchestrator")
    public void handleShippingFailed(ShippingEvents.ShippingFailed event) {
        System.out.println("❌ Orchestrator: ShippingFailed " + event.orderId());

        // Compensating actions:
        // 1. Refund payment
        kafka.send(Topics.PAYMENT_REFUND,
                new PaymentEvents.RefundPayment(event.orderId()));

        // 2. Release inventory
        kafka.send(Topics.INVENTORY_RELEASE,
                new InventoryEvents.ReleaseInventory(event.orderId(), event.itemName(), event.quantity()));

        // 3. Cancel order
        kafka.send(Topics.ORDER_CANCELLED,
                new OrderEvents.OrderCancelled(event.orderId(), "Shipping failed, order cancelled"));
    }
}
