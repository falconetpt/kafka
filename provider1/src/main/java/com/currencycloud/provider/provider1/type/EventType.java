package com.currencycloud.provider.provider1.type;


import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.NonNull;

/**
 * Class that represents the event types known by the application.
 */
public enum EventType {
  /** Created event type. */
  CREATED,
  /** Ready event type. */
  SUBMITTING,
  /** Status Updated event type. */
  STATUS_UPDATED,
  /** Updated event type. */
  UPDATED,
  /** Submitted event type. */
  @JsonEnumDefaultValue
  UNKNOWN;

  /**
   * Method responsible for looking up a event type in the avaliable event types.
   * @param eventType event type as string
   * @return a {@link EventType}.
   */
  public static EventType findByName(@NonNull final String eventType) {
    try {
      return EventType.valueOf(eventType.toUpperCase());
    } catch (final IllegalArgumentException e) {
      return UNKNOWN;
    }
  }
}
