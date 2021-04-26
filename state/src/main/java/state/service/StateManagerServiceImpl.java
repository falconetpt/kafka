package state.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import state.dao.PaymentDAO;
import state.dao.PaymentHistoryDAO;
import state.dao.QueuedSubmissionDAO;
import com.currencycloud.provider.ripple.events.model.Event;
import com.currencycloud.provider.ripple.events.model.Payment;
import com.currencycloud.provider.ripple.events.model.ReleasePaymentData;
import com.currencycloud.provider.ripple.events.model.QueuedPayment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@Service

public class StateManagerServiceImpl implements StateManagerService {
  @Autowired
  private QueuedSubmissionDAO queuedSubmissionDAO;
  @Autowired
  private PaymentHistoryDAO paymentHistoryDAO;
  @Autowired
  private PaymentDAO paymentDAO;

  private final Map<String, BiConsumer<Payment, Event>> consumerMap = new HashMap<>() {{
    put("released", (p, e) -> {
      ReleasePaymentData releasePaymentData = null;
      try {
        releasePaymentData = new ObjectMapper().readValue(e.getData(), ReleasePaymentData.class);
      } catch (JsonProcessingException jsonProcessingException) {
        jsonProcessingException.printStackTrace();
      }
      p.setStatus("RELEASED");
      p.setPaymentCurrency(releasePaymentData.getPaymentCurrency());
      p.setPaymentAmount(releasePaymentData.getPaymentAmount());
    });
    put("ready", (p, e) -> p.setStatus("READY"));
    put("submitted", (p, e) -> p.setStatus("SUBMITED"));
    put("acked", (p, e) -> p.setAckedStatus("SUBMITED"));
    put("completed", (p, e) -> p.setStatus("COMPLETE"));
    put("complete_notification_sent", (p, e) -> p.setPaymentInflight(false));
  }};

  private static final BiConsumer<Payment, Event> updateTimeStamp = (p, e) -> p.setLastUpdate(e.getTimestamp());

  @Override
  public void execute(Event event) {
    switch (event.getEventType()) {
      case "released":
        queuedSubmissionDAO.save(new QueuedPayment(
                event.getProvider(),
                event.getPaymentShortReference(),
                event.getTimestamp(),
                event.getData()
        ));
        break;
      case "submitted":
        paymentHistoryDAO.save(event);
        // send to kafka the msg
        break;
      case "acked":
      case "completed":
      case "complete_notification_sent":
        project(event.getProvider(), event.getPaymentShortReference());
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

    paymentHistoryDAO
            .findByProviderEqualsAndPaymentShortReferenceEqualsAndTimestampAfter(provider, shortRef, payment.getLastUpdate())
            .forEach(e -> consumerMap.get(e.getEventType())
              .andThen(updateTimeStamp)
              .accept(payment, e)
            );

    handlePayment(payment);

    return paymentDAO.save(payment);
  }

  private void handlePayment(final Payment payment) {
    if ("SUBMITED".equalsIgnoreCase(payment.getAckedStatus())
          && "COMPLETE".equalsIgnoreCase(payment.getStatus())
          && payment.isPaymentInflight()) {
      // send to PE the complete msg
      var event = new Event();
      event.setEventType("complete_notification_sent");
      event.setEventId(UUID.randomUUID().toString());
      event.setProvider(payment.getProvider());
      event.setPaymentShortReference(payment.getPaymentShortReference());
      event.setTimestamp(new Date());

      paymentHistoryDAO.save(event);
      payment.setPaymentInflight(false);
      paymentDAO.save(payment);
    } // handle failure as well
  }

  private Payment newPayment(String provider, String shortRef) {
    var payment = new Payment();
    payment.setProvider(provider);
    payment.setPaymentShortReference(shortRef);

    return payment;
  }
}
