package org.npathai.cinemahall.show;

import org.npathai.cinemahall.seat.SeatId;
import org.npathai.cinemahall.seat.SeatNo;
import org.npathai.cinemahall.seat.SeatType;

import java.math.BigDecimal;

public class ShowingSeat {
    private long id;
    private SeatType seatType;
    private BigDecimal price;
    private SeatNo seatNo;

    public ShowingSeat(long id, SeatType seatType, BigDecimal price, SeatNo seatNo) {
        this.id = id;
        this.seatType = seatType;
        this.price = price;
        this.seatNo = seatNo;
    }

    public SeatId getId() {
        return new SeatId(id);
    }
}
