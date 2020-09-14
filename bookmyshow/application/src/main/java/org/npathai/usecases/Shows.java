package org.npathai.usecases;

import org.npathai.cinemahall.CinemaHall;
import org.npathai.cinemahall.show.Show;

import java.util.List;
import java.util.Map;

public class Shows {

    public static Shows empty() {
        return new Shows();
    }


    public void add(CinemaHall cinemaHall, List<Show> shows) {
        throw new UnsupportedOperationException();
    }

    public Map<CinemaHall, List<Show>> getAll() {
        return null;
    }
}
