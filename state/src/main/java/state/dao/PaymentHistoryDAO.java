package state.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import state.model.Event;

import java.util.Date;
import java.util.List;

public interface PaymentHistoryDAO extends CassandraRepository<Event, String> {
  List<Event> findByProviderEqualsAndPaymentShortReferenceEqualsAndTimestampAfter(final String provider,
                                                                                  final String shortRef,
                                                                                  final Date timestamp);
}
