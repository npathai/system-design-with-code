package org.npathai.movie;

import org.npathai.cinemahall.CinemaHall;
import org.npathai.cinemahall.CinemaHallDao;
import org.npathai.cinemahall.show.ShowDao;
import org.npathai.city.CityId;

import java.time.Clock;
import java.util.Collections;
import java.util.List;

public class MovieRepository {

    private final CinemaHallDao cinemaHallDao;

    public MovieRepository(CinemaHallDao cinemaHallDao, ShowDao showDao, MovieDao movieDao, Clock fixedClock) {
        this.cinemaHallDao = cinemaHallDao;
    }

    public List<Movie> getByCityId(CityId cityId) {
        List<CinemaHall> cinemaHalls = cinemaHallDao.getCinemaHalls(cityId);
//        cinemaHalls
//                .stream()
//                .map(cinemaHall -> showsDao.getShows(cinemaHall.getId()))

        return Collections.emptyList();
    }
}
