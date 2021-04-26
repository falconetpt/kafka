package com.currencycloud.provider.ripple.events.model;

import lombok.Data;

@Data
public class ReleasePaymentData {
  private String paymentAmount;
  private String paymentCurrency;
}
