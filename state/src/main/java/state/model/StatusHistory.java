package state.model;

import java.util.Date;

/**
 * @author davidgammon
 *
 */
public class StatusHistory {
  private String status;
  private Date date;
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
  /**
   * Simple getter for {@link #date}.
   * @return The value of {@link #date}. 
   */
  public Date getDate() {
    return date;
  }
  /**
   * Set the value of {@link #t}.
   * @param date The value to set {@link #date}.
   */
  public void setDate(final Date date) {
    this.date = date;
  }
  
  
}
