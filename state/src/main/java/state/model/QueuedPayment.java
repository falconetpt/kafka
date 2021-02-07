package state.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Data
@Table(value = "queued_submissions")
@AllArgsConstructor
@NoArgsConstructor
public class QueuedPayment {
  @PrimaryKeyColumn(name = "provider", type = PrimaryKeyType.PARTITIONED)
  private String provider;
  @PrimaryKeyColumn(name = "payment_short_ref")
  private String paymentShortRef;
  @PrimaryKeyColumn(name = "timestamp")
  private Date timestamp;
}
