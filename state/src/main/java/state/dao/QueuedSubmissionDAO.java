package state.dao;

import org.springframework.data.repository.CrudRepository;
import state.model.QueuedPayment;

public interface QueuedSubmissionDAO extends CrudRepository<QueuedPayment, String> {
}
