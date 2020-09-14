package org.npathai.usecases;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.npathai.cinemahall.CinemaHallDao;
import org.npathai.cinemahall.show.ShowDao;
import org.npathai.city.CityId;
import org.npathai.movie.MovieId;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class GetShowsByMovieTest {
    private static final CityId CURRENT_CITY_ID = new CityId(1L);
    public static final MovieId ANY_MOVIE_ID = new MovieId(1L);

    private GetShowsByMovie getShowsByMovie;
    private CinemaHallDao cinemaHallDao;
    private ShowDao showDao;

    @BeforeEach
    public void setUp() {
        cinemaHallDao = Mockito.mock(CinemaHallDao.class);
        showDao = Mockito.mock(ShowDao.class);
        getShowsByMovie = new GetShowsByMovie(cinemaHallDao, showDao);
    }

    @Test
    public void returnsNoShowsWhenNoCinemaHallIsAssociatedForACity() {
        when(cinemaHallDao.getCinemaHalls(CURRENT_CITY_ID)).thenReturn(Collections.emptyList());

        Shows shows = getShowsByMovie.execute(ANY_MOVIE_ID, CURRENT_CITY_ID);

        assertThat(shows.getAll()).hasSize(0);
    }
}