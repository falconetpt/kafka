package state.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;

@Data
@Table(value = "events")
public class Event {
  @PrimaryKeyColumn(name = "provider", type = PrimaryKeyType.PARTITIONED)
  private String provider;
  @PrimaryKeyColumn(name = "payment_short_ref")
  private String paymentShortReference;
  @PrimaryKeyColumn(name = "event_id")
  private String eventId;
  @PrimaryKeyColumn(name = "timestamp")
  private Date timestamp;
  @Column(value = "event_type")
  private String eventType;
  @Column(value = "data")
  private String data;
}
