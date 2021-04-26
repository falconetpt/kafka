package provider1.publisher;

import lombok.NonNull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.currencycloud.provider.provider1.events.model.Event;

import java.util.UUID;

@Component
public class KafkaEventPublisher implements EventPublisher<Event> {
  @Autowired
  private KafkaTemplate<String, Event> template;
  private @Value("${payment.topic}") String topic;

  @Override
  public void send(@NonNull Event element) {
    final var record = new ProducerRecord<>(topic, UUID.randomUUID().toString(), element);
    record.headers().add("provider", element.getProvider().getBytes());
    record.headers().add("event_type", element.getEventType().name().getBytes());
    template.send(record);
  }
}
