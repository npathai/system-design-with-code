package org.npathai.cinemahall.seat;

import java.math.BigDecimal;

public class Seat {
    private long id;
    private SeatType seatType;
    private BigDecimal price;
    private SeatNo seatNo;

    public Seat(long id, SeatType seatType, BigDecimal price, SeatNo seatNo) {
        this.id = id;
        this.seatType = seatType;
        this.price = price;
        this.seatNo = seatNo;
    }
}
