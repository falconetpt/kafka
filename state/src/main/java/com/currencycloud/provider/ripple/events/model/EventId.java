package com.currencycloud.provider.ripple.events.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
public class EventId implements Serializable {
  private String provider;
  private String paymentShortReference;
  private Date timestamp;
  private String eventId;
}
