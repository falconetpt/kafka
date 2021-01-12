package provider1;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import state.model.Payment;


/**
 * @author davidgammon
 *
 */
@Component
public class EventConsumer {
  @Autowired
  private EventServiceImpl service;
  
  // CREATED -> READY
  @KafkaListener(groupId = "${payment.group}", topics = "${payment.topic}", containerFactory = "created")
  public void created(final @Payload Payment message) {
    sanatise(message);
    
    validate(message);
    
    message.setStatus("ready");
    service.invoke(message.getId(), message, "update");
  }
  
  // UPDATE -> UPDATED
  @KafkaListener(groupId = "${payment.group}2", topics = "${payment.topic}", containerFactory = "submitting")
  public void submitting(final @Payload Payment message) {
    sendPayment(message);
    
    message.setStatus("submitted");
    service.invoke(message.getId(), message, "update_status");
  }

  /**
   * @param message
   * @return 
   */
  private void validate(final Payment message) {
    // TODO Auto-generated method stub
  }

  /**
   * @param message
   */
  private void sanatise(final Payment message) {
    final Map<String, Object> values = message.getValues();
    
    values.entrySet().stream()
      .filter(e -> e.getValue() instanceof String)
      .forEach(e -> values.put(e.getKey(), ((String) e.getValue()).substring(2)));
    
  }
  
  /**
   * @param message
   */
  private void sendPayment(final Payment message) {
    // TODO Auto-generated method stub
  }
}
