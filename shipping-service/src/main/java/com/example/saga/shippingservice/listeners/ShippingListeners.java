package com.example.saga.shippingservice.listeners;

import com.example.saga.common.Topics;
import com.example.saga.common.events.ShippingEvents;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ShippingListeners {
    private final KafkaTemplate<String,Object> kafka;
    public ShippingListeners(KafkaTemplate<String,Object> kafka){ this.kafka = kafka; }

    @KafkaListener(topics = Topics.SHIPPING_SCHEDULE, groupId = "shipping-service")
    public void onSchedule(ShippingEvents.ScheduleShipping evt){
        String tracking = UUID.randomUUID().toString();
        kafka.send(Topics.SHIPPING_SCHEDULED, new ShippingEvents.ShippingScheduled(evt.orderId(), tracking));
    }
}
