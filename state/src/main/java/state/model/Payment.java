package state.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author davidgammon
 *
 */
@Data
@Entity
@Table(name = "payment_submission")
@IdClass(PaymentId.class)
public class Payment implements Serializable {
  @Id
  @Column(name = "provider")
  private String provider;
  @Id
  @Column(name = "payment_short_ref")
  private String paymentShortReference;
  @Column(name = "status")
  private String status;
  @Column(name = "acked_status")
  private String ackedStatus;
  @Column(name = "payment_amount")
  private String paymentAmount;
  @Column(name = "payment_currency")
  private String paymentCurrency;
  @Column(name = "last_update")
  private Date lastUpdate = new Date(0);
}
