package com.currencycloud.provider.ripple.events.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "queued_submissions")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(QueuedPaymentId.class)
public class QueuedPayment implements Serializable {
  @Id
  @Column(name = "provider")
  private String provider;
  @Id
  @Column(name = "payment_short_ref")
  private String paymentShortReference;
  @Column(name = "timestamp")
  private Date timestamp;
  @Column(name = "required_fields")
  private String requiredFields;
}
