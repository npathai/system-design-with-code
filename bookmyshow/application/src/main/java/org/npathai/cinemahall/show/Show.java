package org.npathai.cinemahall.show;

import org.npathai.cinemahall.auditorium.AuditoriumId;
import org.npathai.cinemahall.seat.Seat;
import org.npathai.movie.MovieId;

import java.time.ZonedDateTime;
import java.util.List;

public class Show {
    private long id;
    private AuditoriumId auditoriumId;
    private List<Seat> seats;
    private MovieId movieId;
    private ZonedDateTime startTime;


}
