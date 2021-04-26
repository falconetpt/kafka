package state.dao;

import org.springframework.data.repository.CrudRepository;
import com.currencycloud.provider.ripple.events.model.QueuedPayment;

import java.util.List;

public interface QueuedSubmissionDAO extends CrudRepository<QueuedPayment, String> {
  List<QueuedPayment> findAllByProvider(final String provider);
}
