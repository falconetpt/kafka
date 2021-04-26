package com.currencycloud.provider.provider1.events.model;

import com.currencycloud.provider.provider1.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Event {
  private String provider;
  private String paymentSubmissionId;
  private EventType eventType;
  private String status;
  private String payload;
}
