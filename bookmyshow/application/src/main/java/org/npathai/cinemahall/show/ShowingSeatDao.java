package org.npathai.cinemahall.show;

import org.npathai.cinemahall.seat.Seat;
import org.npathai.cinemahall.seat.SeatId;
import org.npathai.cinemahall.seat.SeatNo;
import org.npathai.cinemahall.seat.SeatType;

import java.math.BigDecimal;

public class ShowingSeatDao {

    public ShowingSeat reserveSeat(SeatId seatId) {
        ShowingSeat seat = new ShowingSeat(seatId.getValue(), SeatType.DELUX, BigDecimal.TEN, new SeatNo());
        return seat;
    }
}
