package state.service;

import com.currencycloud.provider.ripple.events.model.Event;
import com.currencycloud.provider.ripple.events.model.Payment;

public interface StateManagerService {
  void execute(final Event message);
  Payment project(final String provider, final String shortRef);
}
