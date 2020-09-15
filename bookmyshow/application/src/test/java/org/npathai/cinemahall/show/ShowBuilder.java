package org.npathai.cinemahall.show;

import org.npathai.cinemahall.CinemaHallBuilder;
import org.npathai.cinemahall.auditorium.AuditoriumId;
import org.npathai.cinemahall.seat.Seat;
import org.npathai.cinemahall.seat.SeatNo;
import org.npathai.cinemahall.seat.SeatType;
import org.npathai.movie.MovieId;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class ShowBuilder {
    private long id;
    private AuditoriumId auditoriumId;
    private List<Seat> seats = List.of(
            new Seat(1L, SeatType.DELUX, BigDecimal.TEN, new SeatNo()),
            new Seat(2L, SeatType.ECONOMY, BigDecimal.ONE, new SeatNo())
    );
    private MovieId movieId;
    private ZonedDateTime startTime = ZonedDateTime.now();

    public static ShowBuilder aShow() {
        return new ShowBuilder();
    }

    public ShowBuilder withAuditoriumId(AuditoriumId auditoriumId) {
        this.auditoriumId = auditoriumId;
        return this;
    }

    public ShowBuilder withMovieId(MovieId movieId) {
        this.movieId = movieId;
        return this;
    }

    public Show build() {
        return new Show(id, auditoriumId, seats, movieId, startTime);
    }
}
