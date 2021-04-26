package state.dao;

import org.springframework.data.repository.CrudRepository;
import com.currencycloud.provider.ripple.events.model.Payment;

import java.util.Optional;

/**
 * @author davidgammon
 *
 */

public interface PaymentDAO extends CrudRepository<Payment, String> {
  Optional<Payment> findByProviderEqualsAndPaymentShortReferenceEquals(final String provider,
                                                                       final String shortRef);
}
