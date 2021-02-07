package provider1.publisher;

import lombok.NonNull;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import state.model.Event;

@Component
public class KafkaEventPublisher implements EventPublisher<Event> {
  @Autowired
  private KafkaTemplate<String, Event> template;
  private @Value("${payment.topic}") String topic;

  @Override
  public void send(@NonNull Event element) {
    final var record = new ProducerRecord<>(topic, element.getEventId(), element);
    record.headers().add("provider", element.getProvider().getBytes());
    template.send(record);
  }
}
