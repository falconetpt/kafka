package state.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author davidgammon
 *
 */
@Data
@Table(value = "payment_submission")
public class Payment {
  @PrimaryKeyColumn(name = "provider", type = PrimaryKeyType.PARTITIONED)
  private String provider;
  @PrimaryKeyColumn(name = "payment_short_ref")
  private String paymentShortReference;
  @PrimaryKeyColumn(name = "status")
  private String status;
  @Column(value = "acked_status")
  private String ackedStatus;
  @Column(value = "last_update")
  private Date lastUpdate = new Date(0);
}
