package state.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import state.model.Event;

public interface PaymentHistoryDAO extends CassandraRepository<Event, String> {
  Iterable<Event> findByProviderEquals(final String provider);
  Iterable<Event> findByProviderEqualsAndPaymentShortReferenceEquals(final String provider,
                                                                     final String shortRef);
}
