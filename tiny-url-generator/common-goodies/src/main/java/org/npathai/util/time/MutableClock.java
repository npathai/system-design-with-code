package org.npathai.util.time;

import org.npathai.annotations.TestingUtil;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;

@TestingUtil
public class MutableClock extends Clock {

    private Instant instant;
    private final ZoneId zoneId;

    public MutableClock() {
        this.instant = Instant.now();
        this.zoneId = ZoneId.systemDefault();
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public Clock withZone(ZoneId zoneId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant instant() {
        return instant;
    }

    /*
    Mutator methods
     */
    public void advanceBy(TemporalAmount amount) {
        instant = instant.plus(amount);
    }
}
