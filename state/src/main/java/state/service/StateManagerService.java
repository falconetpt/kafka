package state.service;

import state.model.Event;
import state.model.Payment;

public interface StateManagerService {
  void execute(final Event message);
  Payment project(final String provider, final String shortRef);
}
