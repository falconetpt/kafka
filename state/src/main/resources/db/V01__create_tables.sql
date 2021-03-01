CREATE TABLE events (
                      provider varchar(255) NOT NULL,
                      payment_short_ref varchar(255) NOT NULL,
                      timestamp timestamp DEFAULT NOW() NOT NULL,
                      event_id varchar(255) NOT NULL,
                      event_type varchar(255) NOT NULL,
                      data JSON,
                      PRIMARY KEY  (provider, payment_short_ref, timestamp, event_id)
);

-- Payment submission projection table
CREATE TABLE payment_submission (
                                  provider varchar(255) NOT NULL,
                                  payment_short_ref varchar(255) NOT NULL,
                                  last_update timestamp NOT NULL,
                                  status varchar(255),
                                  acked_status varchar(255),
                                  PRIMARY KEY  (provider, payment_short_ref)
);

-- Queued for submission table
CREATE TABLE queued_submissions (
                                provider varchar(255) NOT NULL,
                                payment_short_ref varchar(255) NOT NULL,
                                timestamp timestamp,
                                PRIMARY KEY  (provider, payment_short_ref)
);
