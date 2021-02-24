package state.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import state.model.Event;

import java.util.Date;

public interface PaymentHistoryDAO extends CassandraRepository<Event, String> {
  Iterable<Event> findByProviderEquals(final String provider);
  Iterable<Event> findByProviderEqualsAndPaymentShortReferenceEqualsAndTimestampAfter(final String provider,
                                                                                      final String shortRef,
                                                                                      final Date timestamp);
}
