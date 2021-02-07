package state;

import java.util.Optional;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import state.model.Event;
import state.model.Payment;

/**
 * @author davidgammon
 *
 */
public class EventFilteringStrategy implements RecordFilterStrategy<String, Event> {
  private final String provider;
  
  /**
   * @param provider
   */
  public EventFilteringStrategy(final String provider) {
    this.provider = provider;
  }



  /**
   * {@inheritDoc}
   */
  @Override
  public boolean filter(final ConsumerRecord<String, Event> consumerRecord) {
    final Optional<Header> header = Optional.ofNullable(consumerRecord.headers().lastHeader("provider"));
    
    return header.map(this::shouldFilter)
                 .orElse(true);
  }
  
  private boolean shouldFilter(final Header header) {
    final String headerValue = new String(header.value());
    
    return !provider.equals(headerValue);
  }
}

