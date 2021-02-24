package state.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

import state.model.Event;
import state.model.Payment;

/**
 * @author davidgammon
 *
 */

public interface PaymentDAO extends CassandraRepository<Payment, String> {
  Optional<Payment> findByProviderEqualsAndPaymentShortReferenceEquals(final String provider,
                                                                     final String shortRef);
}
