package org.npathai.cinemahall.seat;

import org.npathai.common.ValueObject;

public class SeatId extends ValueObject<Long> {
    public SeatId(long id) {
        super(id);
    }
}
