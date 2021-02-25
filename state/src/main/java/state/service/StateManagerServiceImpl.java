package state.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import state.dao.PaymentDAO;
import state.dao.PaymentHistoryDAO;
import state.dao.QueuedSubmissionDAO;
import state.model.Event;
import state.model.Payment;
import state.model.QueuedPayment;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;

@Service
public class StateManagerServiceImpl implements StateManagerService {
  @Autowired
  private QueuedSubmissionDAO queuedSubmissionDAO;

  @Autowired
  private PaymentHistoryDAO paymentHistoryDAO;

  @Autowired
  private PaymentDAO paymentDAO;

  private Map<String, BiConsumer<Payment, Event>> consumerMap = new HashMap<>() {{
    put("ready", (p, e) -> p.setStatus("READY"));
    put("submit", (p, e) -> p.setStatus("SUBMITED"));
    put("acked", (p, e) -> p.setAckedStatus("SUBMITED"));
    put("complete", (p, e) -> p.setStatus("COMPLETE"));
  }};

  private static BiConsumer<Payment, Event> updateTimeStamp = (p, e) -> p.setLastUpdate(e.getTimestamp());

  @Override
  public void execute(Event message) {
    switch (message.getEventType()) {
      case "ready":
        queuedSubmissionDAO.save(new QueuedPayment(
                message.getProvider(),
                message.getPaymentShortReference(),
                message.getTimestamp()
        ));
        break;
      default:
        break;
    }
  }

  @Override
  public Payment project(String provider, String shortRef) {
    var payment = paymentDAO
            .findByProviderEqualsAndPaymentShortReferenceEquals(provider, shortRef)
            .orElse(newPayment(provider, shortRef));

    StreamSupport.stream(
            paymentHistoryDAO
                    .findByProviderEqualsAndPaymentShortReferenceEqualsAndTimestampAfter(provider, shortRef, payment.getLastUpdate())
                    .spliterator(),
            false
    ).forEach(e -> consumerMap.get(e.getEventType())
            .andThen(updateTimeStamp)
            .accept(payment, e));

    return paymentDAO.save(payment);
  }

  private Payment newPayment(String provider, String shortRef) {
    var payment = new Payment();
    payment.setProvider(provider);
    payment.setPaymentShortReference(shortRef);

    return payment;
  }
}
