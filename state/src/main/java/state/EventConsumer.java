package state;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import state.dao.PaymentDAO;
import state.model.Payment;
import state.model.StatusHistory;


/**
 * @author davidgammon
 *
 */
@Component
public class EventConsumer {
  @Autowired
  private EventServiceImpl service;
  @Autowired
  private PaymentDAO dao;
  
  // CREATE -> CREATED
  @KafkaListener(groupId = "${payment.group}", topics = "${payment.topic}", containerFactory = "create")
  public void create(final @Payload Payment message) {
    message.setStatus("new");
    final StatusHistory statusHistory = new StatusHistory();
    statusHistory.setStatus(message.getStatus());
    statusHistory.setDate(new Date());
    
    message.getStatusHistory().add(statusHistory);
    
    final Payment saved = dao.save(message);
    
    service.invoke(message.getId(), saved, "created");
  }
  
  // UPDATE -> UPDATED
  @KafkaListener(groupId = "${payment.group}2", topics = "${payment.topic}", containerFactory = "update")
  public void update(final @Payload Payment message) {
    final Payment original = dao.findById(message.getId()).orElseThrow();
    
    message.setStatusHistory(original.getStatusHistory());
    
    final StatusHistory statusHistory = new StatusHistory();
    statusHistory.setStatus(message.getStatus());
    statusHistory.setDate(new Date());
    
    message.getStatusHistory().add(statusHistory);
    
    final Payment saved = dao.save(message);
    
    service.invoke(message.getId(), saved, "updated");
  }
  
  // UPDATE_STATUS -> UPDATED
  @KafkaListener(groupId = "${payment.group}3", topics = "${payment.topic}", containerFactory = "updateStatus")
  public void updateStatus(final @Payload Payment message) {
    final Payment original = dao.findById(message.getId()).orElseThrow();
    
    final StatusHistory statusHistory = new StatusHistory();
    statusHistory.setStatus(message.getStatus());
    statusHistory.setDate(new Date());
    
    original.getStatusHistory().add(statusHistory);
    original.setStatus(message.getStatus());
    
    final Payment saved = dao.save(original);
    
    service.invoke(message.getId(), saved, "updated");
  }
}
