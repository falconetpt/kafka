package state.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentId implements Serializable {
  private String provider;
  private String paymentShortReference;
}
