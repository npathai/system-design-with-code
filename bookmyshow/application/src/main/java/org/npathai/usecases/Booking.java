package org.npathai.usecases;

import org.npathai.cinemahall.seat.Seat;
import org.npathai.cinemahall.show.Show;
import org.npathai.movie.Movie;

import java.math.BigDecimal;

public class Booking {
    private long id;
    private Seat seat;
    private Movie movie;
    private Show show;
    private BigDecimal totalAmount;
    private BookingStatus status;

}
