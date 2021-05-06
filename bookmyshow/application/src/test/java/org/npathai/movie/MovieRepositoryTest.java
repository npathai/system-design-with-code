package org.npathai.movie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.npathai.cinemahall.CinemaHallDao;
import org.npathai.cinemahall.show.ShowDao;
import org.npathai.city.CityId;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRepositoryTest {
    public static final CityId CITY_ID = new CityId(1L);

    // This can come from a cache, because the list of movies will not change frequently
    // We can keep refreshing this cache after a day or a few hours

    @Mock
    CinemaHallDao cinemaHallDao;

//    @Mock
//    ShowDao showDao;

    @Mock
    MovieDao movieDao;

    Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        movieRepository = new MovieRepository(cinemaHallDao, showDao, movieDao, fixedClock);
    }

    @Test
    public void returnsEmptyListWhenNoMovieCurrentlyPlayingInTheCity() {
        when(cinemaHallDao.getCinemaHalls(CITY_ID)).thenReturn(Collections.emptyList());

        List<Movie> moviesByCity = movieRepository.getByCityId(CITY_ID);

        assertThat(moviesByCity).isEmpty();
    }

    @Test
    public void returnsListOfReleasedMoviesCurrentlyPlayingInTheCity() {
//        CinemaHallBuilder.aCinemaHall()
//                .showing()


        Movie harryPotterMovie = MovieBuilder.aMovieNamed("Harry Potter")
                .withReleaseDate(today().minus(Duration.ofDays(1)).atZone(ZoneId.systemDefault()))
                .create();

        Movie interstellarMovie = MovieBuilder.aMovieNamed("Interstellar")
                .withReleaseDate(today().atZone(ZoneId.systemDefault()))
                .create();

        Movie wonderWoman = MovieBuilder.aMovieNamed("Wonder Woman")
                .withReleaseDate(today().plus(Duration.ofDays(1)).atZone(ZoneId.systemDefault()))
                .create();

        // FIXME add this test case
        Assertions.assertTrue(false);
    }

    private Instant today() {
        return fixedClock.instant();
    }
}