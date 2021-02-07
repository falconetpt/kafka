CREATE KEYSPACE
    state_manager
WITH
    replication = {'class':'SimpleStrategy', 'replication_factor' : 3};

-- Event sourcing table
CREATE TABLE state_manager.events (
  provider text,
  payment_short_ref text,
  timestamp timestamp,
  event_id text,
  event_type text,
  data text,
  PRIMARY KEY  (provider, payment_short_ref, timestamp, event_id)
);

-- Queued for submission table
CREATE TABLE state_manager.queued_submissions (
  provider text,
  payment_short_ref text,
  timestamp timestamp,
  PRIMARY KEY  (provider, payment_short_ref, timestamp)
);
