package state.dto;

import lombok.Data;

@Data
public class EventDto {
  private String provider;
  private String paymentShortReference;
  private String type;
  private Object payload;
}
