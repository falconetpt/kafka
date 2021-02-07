package state.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Event {
  private String provider;
  private String paymentShortReference;
  private String eventId;
  private String eventType;
  private Date timestamp;
  private String data;
}
