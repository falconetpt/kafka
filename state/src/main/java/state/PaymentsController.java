package state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import state.dao.PaymentDAO;
import state.dao.QueuedSubmissionDAO;
import state.model.Payment;
import state.service.StateManagerService;

import java.util.List;

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
  
  @GetMapping("/submit")
  public @ResponseBody String getPayment() {
    final var payments = dao.findAll();

    payments.stream()
            .peek(e -> {
              final var payment = stateManagerService.project(e.getProvider(), e.getPaymentShortRef());
              service.invoke(e.getPaymentShortRef(), payment, "submitting");
            })
            .forEach(dao::delete);

    return "ok";
  }
}
