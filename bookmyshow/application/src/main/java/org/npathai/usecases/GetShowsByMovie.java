package org.npathai.usecases;

import org.npathai.cinemahall.CinemaHall;
import org.npathai.cinemahall.CinemaHallDao;
import org.npathai.cinemahall.auditorium.Auditorium;
import org.npathai.cinemahall.show.Show;
import org.npathai.cinemahall.show.ShowDao;
import org.npathai.city.CityId;
import org.npathai.movie.MovieId;

import java.util.List;

public class GetShowsByMovie {

    private final CinemaHallDao cinemaHallDao;
    private final ShowDao showDao;

    public GetShowsByMovie(CinemaHallDao cinemaHallDao, ShowDao showDao) {
        this.cinemaHallDao = cinemaHallDao;
        this.showDao = showDao;
    }

    public CinemaHallShows execute(MovieId movieId, CityId cityId) {
        List<CinemaHall> cinemaHallList = cinemaHallDao.getCinemaHalls(cityId);
        if (cinemaHallList.isEmpty()) {
            return CinemaHallShows.empty();
        }

        CinemaHallShows cinemaHallShows = new CinemaHallShows();
        for (CinemaHall cinemaHall : cinemaHallList) {
            for (Auditorium auditorium : cinemaHall.getAudis()) {
                List<Show> shows = showDao.getShows(auditorium.getId(), movieId);
                if (shows.isEmpty()) {
                    continue;
                }
                cinemaHallShows.add(cinemaHall, shows);
            }
        }

        return cinemaHallShows;
    }
}