package provider1;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import state.model.Payment;

/**
 * @author davidgammon
 *
 */
public class EventFilteringStrategy implements RecordFilterStrategy<String, Payment> {
  private final String type;
  
  /**
   * @param type
   */
  public EventFilteringStrategy(final String type) {
    this.type = type;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public boolean filter(final ConsumerRecord<String, Payment> consumerRecord) {
    final Optional<Header> header = Optional.ofNullable(consumerRecord.headers().lastHeader("type"));
    
    return header.map(this::shouldFilter)
                 .orElse(true);
  }
  
  private boolean shouldFilter(final Header header) {
    final String headerValue = new String(header.value());
    
    return !type.equals(headerValue);
  }
}

