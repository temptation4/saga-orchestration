package com.example.saga.paymentservice.listeners;

import com.example.saga.paymentservice.entity.Payment;
import com.example.saga.paymentservice.repository.PaymentRepository;
import com.example.saga.common.events.PaymentEvents;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentListeners {

    private final PaymentRepository repo;
    private final KafkaTemplate<String, Object> kafka;

    public PaymentListeners(PaymentRepository repo, KafkaTemplate<String, Object> kafka) {
        this.repo = repo;
        this.kafka = kafka;
    }

    @KafkaListener(topics = "payment-process", groupId = "payment-service")
    public void handlePayment(PaymentEvents.ProcessPayment event) {
        Payment payment = new Payment();
        payment.setOrderId(event.orderId());
        payment.setAmount(Double.parseDouble(event.amount().toString()));

        // simulate success if amount < 1000
        boolean success = event.amount().intValue() < 1000;
        payment.setStatus("success");

        repo.save(payment);

        if (success) {
            kafka.send("payment-processed", new PaymentEvents.PaymentProcessed(event.orderId()));
            System.out.println("✅ Payment success for order: " + event.orderId());
        } else {
            kafka.send("payment-failed", new PaymentEvents.PaymentFailed(event.orderId(),event.name(),event.quantity(),"❌ Payment failed for order: " + event.orderId()));
            System.out.println("❌ Payment failed for order: " + event.orderId());
        }
    }
}
