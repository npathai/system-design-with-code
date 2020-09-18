package org.npathai.usecases;

import org.npathai.cinemahall.seat.SeatDao;
import org.npathai.cinemahall.seat.SeatId;

import java.util.concurrent.CompletableFuture;

public class BookSeat {

    private SeatDao seatDao;

    public BookSeat(SeatDao seatDao) {
        this.seatDao = seatDao;
    }

    public CompletableFuture<Booking> execute(SeatId seatId) {
        // Reserve seat if available
        // Make payment
        // Mark as confirmed if payment succeeds
        // If payment fails Seat remains reserved for few minutes and freed afterwards

        return null;
    }
}
