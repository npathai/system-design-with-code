package org.npathai.usecases;

import org.npathai.cinemahall.CinemaHall;
import org.npathai.cinemahall.show.Show;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shows {

    private final Map<CinemaHall, List<Show>> showsByCinemaHall = new HashMap<>();

    public static Shows empty() {
        return new Shows();
    }

    public void add(CinemaHall cinemaHall, List<Show> shows) {
        showsByCinemaHall.computeIfAbsent(cinemaHall, key -> new ArrayList<>());
        showsByCinemaHall.get(cinemaHall).addAll(shows);
    }

    public Map<CinemaHall, List<Show>> getAll() {
        return showsByCinemaHall;
    }
}
