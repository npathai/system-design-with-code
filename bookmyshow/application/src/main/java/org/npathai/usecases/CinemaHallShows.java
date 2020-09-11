package org.npathai.usecases;

import org.npathai.cinemahall.CinemaHall;
import org.npathai.cinemahall.show.Show;

import java.util.List;

public class CinemaHallShows {

    public static CinemaHallShows empty() {
        return new CinemaHallShows();
    }


    public void add(CinemaHall cinemaHall, List<Show> shows) {
        throw new UnsupportedOperationException();
    }
}
