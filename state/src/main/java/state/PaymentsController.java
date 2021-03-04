package state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import state.dao.PaymentDAO;
import state.dao.QueuedSubmissionDAO;
import state.model.Event;
import state.model.Payment;
import state.service.StateManagerService;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * @author davidgammon
 *
 */
@RestController
@RequestMapping(value = "payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentsController {
  @Autowired
  private QueuedSubmissionDAO dao;
  @Autowired
  private EventServiceImpl service;
  @Autowired
  private StateManagerService stateManagerService;

  @GetMapping("/{provider}/{id}")
  public @ResponseBody Payment getPayment(final @PathVariable String provider,
                                          final @PathVariable String id) {
    return stateManagerService.project(provider, id);
  }
  
  @PostMapping  ("/submit/{provider}")
  public @ResponseBody String getPayment(final @PathVariable String provider) {
    final var payments = dao.findAllByProvider(provider);

    StreamSupport.stream(payments.spliterator(), false)
            .peek(e -> {
              final var payment = stateManagerService.project(e.getProvider(), e.getPaymentShortReference());
              service.invoke(e.getPaymentShortReference(), payment, "submitting");
            })
            .peek(e -> {
              var event = new Event();
              event.setEventType("submitted");
              event.setProvider(provider);
              event.setEventId(UUID.randomUUID().toString());
              event.setTimestamp(new Date());
              event.setData(e.getRequiredFields());
              event.setPaymentShortReference(e.getPaymentShortReference());
              stateManagerService.execute(event);
            })
            .forEach(dao::delete);

    return "ok";
  }
}
