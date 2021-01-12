package state.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import state.model.Payment;

/**
 * @author davidgammon
 *
 */

public interface PaymentDAO extends MongoRepository<Payment, String> {
  List<Payment> findByStatus(String status);
}
