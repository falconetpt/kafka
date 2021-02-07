package state;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import state.dao.PaymentDAO;
import state.dao.PaymentHistoryDAO;
import state.dto.EventDto;
import state.model.Event;
import state.model.Payment;
import state.service.StateManagerService;

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
  @Autowired
  private StateManagerService stateManagerService;

  @GetMapping("/{provider}/{id}")
  public @ResponseBody Payment getPayment(final @PathVariable String provider,
                                          final @PathVariable String id) {
    return stateManagerService.project(provider, id);
  }
  
  @GetMapping("/submit")
  public @ResponseBody String getPayment() {
    final List<Payment> payments = dao.findByStatus("ready");
    
    payments.forEach(e -> service.invoke(e.getId(), e, "submitting"));
    
    return "ok";
  }
}
