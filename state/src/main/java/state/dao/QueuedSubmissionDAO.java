package state.dao;

import org.springframework.data.cassandra.repository.CassandraRepository;
import state.model.QueuedPayment;

public interface QueuedSubmissionDAO extends CassandraRepository<QueuedPayment, String> {
}
