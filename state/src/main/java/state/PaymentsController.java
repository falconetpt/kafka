package state;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import state.dao.PaymentDAO;
import state.model.Payment;

/**
 * @author davidgammon
 *
 */
@RestController
@RequestMapping(value = "payments", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentsController {
  @Autowired
  private PaymentDAO dao;
  @Autowired
  private EventServiceImpl service;
  
  @GetMapping("/{id}")
  public @ResponseBody Payment getPayment(final @PathVariable String id) {
    return dao.findById(id).orElseThrow();
  }
  
  @GetMapping("/submit")
  public @ResponseBody String getPayment() {
    final List<Payment> payments = dao.findByStatus("ready");
    
    payments.forEach(e -> service.invoke(e.getId(), e, "submitting"));
    
    return "ok";
  }
}
