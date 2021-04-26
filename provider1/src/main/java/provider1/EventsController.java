package provider1;

import com.currencycloud.provider.provider1.type.EventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import provider1.publisher.EventPublisher;
import com.currencycloud.provider.provider1.events.model.Event;
import com.currencycloud.provider.provider1.events.model.EventDto;

@RestController
@RequestMapping(value = "events")
public class EventsController {

  @Autowired
  private EventPublisher<Event> eventEventPublisher;

  private ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping("/new")
  public ResponseEntity<Event> save(@RequestBody @NonNull final EventDto eventDto) throws JsonProcessingException {
    final var event = new Event(
            eventDto.getProvider(),
            eventDto.getPaymentShortReference(),
            EventType.findByName(eventDto.getType()),
            eventDto.getType(),
            objectMapper.writeValueAsString(eventDto.getPayload())
    );

    eventEventPublisher.send(event);

    return ResponseEntity.ok(event);
  }
}
