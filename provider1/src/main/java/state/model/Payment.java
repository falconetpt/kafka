package state.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

/**
 * @author davidgammon
 *
 */
public class Payment {
  private String id;
  
  private String status;
  
  private Map<String, Object> values = new HashMap<String, Object>();
  
  /**
   * Simple getter for {@link #status}.
   * @return The value of {@link #status}. 
   */
  public String getStatus() {
    return status;
  }

  /**
   * Set the value of {@link #t}.
   * @param status The value to set {@link #status}.
   */
  public void setStatus(final String status) {
    this.status = status;
  }
  
  
  
  
  @JsonAnySetter
  public void add(final String key, final String value) {
    values.put(key, value);
  }


  /**
   * Simple getter for {@link #values}.
   * @return The value of {@link #values}. 
   */
  @JsonAnyGetter
  public Map<String, Object> getValues() {
    return values;
  }

  
  
  /**
   * Set the value of {@link #t}.
   * @param values The value to set {@link #values}.
   */
  public void setValues(final Map<String, Object> values) {
    this.values = values;
  }

  /**
   * Simple getter for {@link #id}.
   * @return The value of {@link #id}. 
   */
  public String getId() {
    return id;
  }

  /**
   * Set the value of {@link #t}.
   * @param id The value to set {@link #id}.
   */
  public void setId(final String id) {
    this.id = id;
  }
}
