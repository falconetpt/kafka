package state;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import state.model.Payment;

/**
 * @author davidgammon
 *
 */
@Service
public class EventServiceImpl {
  @Autowired
  private KafkaTemplate<String, Payment> template;

  private @Value("${payment.topic}") String topic;
  
  public void invoke(final String id, final Payment value, final String type) {
    final var record = new ProducerRecord<>(topic, id, value);
    record.headers().add("type", type.getBytes());
    
    template.send(record);
  }
}
