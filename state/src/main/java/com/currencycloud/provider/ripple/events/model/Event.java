package com.currencycloud.provider.ripple.events.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "events")
@IdClass(EventId.class)
public class Event implements Serializable {
  @Id
  @Column(name = "provider")
  private String provider;
  @Id
  @Column(name = "payment_short_ref")
  private String paymentShortReference;
  @Id
  @Column(name = "timestamp")
  private Date timestamp;
  @Id
  @Column(name = "event_id")
  private String eventId;
  @Column(name = "event_type")
  private String eventType;
  @Column(name = "data")
  private String data;
}
