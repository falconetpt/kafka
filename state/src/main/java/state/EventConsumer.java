package state;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import state.dao.PaymentDAO;
import state.dao.PaymentHistoryDAO;
import state.model.Event;
import state.model.Payment;
import state.model.StatusHistory;
import state.service.StateManagerService;


/**
 * @author davidgammon
 *
 */
@Component
public class EventConsumer {
  @Autowired
  private PaymentHistoryDAO dao;

  @Autowired
  private StateManagerService stateManagerService;



  @KafkaListener(groupId = "${payment.group}", topics = "${payment.topic}", containerFactory = "lhv")
  public void storeEvent(final @Payload Event message) {
    dao.save(message);
    stateManagerService.execute(message);
  }
}
