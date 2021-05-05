package org.npathai.movie;

import org.npathai.cinemahall.CinemaHall;
import org.npathai.cinemahall.CinemaHallDao;
import org.npathai.city.CityId;

import java.util.Collections;
import java.util.List;

public class MovieRepository {

    private final CinemaHallDao cinemaHallDao;

    public MovieRepository(CinemaHallDao cinemaHallDao) {
        this.cinemaHallDao = cinemaHallDao;
    }

    public List<Movie> getByCityId(CityId cityId) {
        List<CinemaHall> cinemaHalls = cinemaHallDao.getCinemaHalls(cityId);
        return Collections.emptyList();
    }
}
