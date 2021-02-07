package provider1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import provider1.publisher.EventPublisher;
import state.model.Event;
import state.model.EventDto;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "events")
public class EventsController {

  @Autowired
  private EventPublisher<Event> eventEventPublisher;

  private ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping("/new")
  public ResponseEntity<Event> save(@RequestBody final EventDto eventDto) throws JsonProcessingException {
    final var event = new Event(
            eventDto.getProvider(),
            eventDto.getPaymentShortReference(),
            UUID.randomUUID().toString(),
            eventDto.getType(),
            new Date(),
            objectMapper.writeValueAsString(eventDto.getPayload())
    );

    eventEventPublisher.send(event);

    return ResponseEntity.ok(event);
  }
}
