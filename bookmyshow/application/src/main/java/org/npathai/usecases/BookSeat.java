package org.npathai.usecases;

import org.npathai.cinemahall.show.ShowingSeatDao;
import org.npathai.cinemahall.seat.SeatId;

import java.util.concurrent.CompletableFuture;

public class BookSeat {

    private ShowingSeatDao showingSeatDao;

    public BookSeat(ShowingSeatDao showingSeatDao) {
        this.showingSeatDao = showingSeatDao;
    }

    public CompletableFuture<Booking> execute(SeatId seatId) {
        showingSeatDao.reserveSeat(seatId);
        // Reserve seat if available
        // Make payment
        // Mark as confirmed if payment succeeds
        // If payment fails Seat remains reserved for few minutes and freed afterwards

        return null;
    }
}
