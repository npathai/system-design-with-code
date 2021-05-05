package org.npathai.movie;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.npathai.cinemahall.CinemaHallDao;
import org.npathai.city.CityId;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRepositoryTest {
    public static final CityId CITY_ID = new CityId(1L);

    // This can come from a cache, because the list of movies will not change frequently
    // We can keep refreshing this cache after a day or a few hours

    @Mock(lenient = false)
    CinemaHallDao cinemaHallDao;

    @InjectMocks
    MovieRepository movieRepository;

    @Test
    public void returnsEmptyListWhenNoMovieCurrentlyPlayingInTheCity() {
        when(cinemaHallDao.getCinemaHalls(CITY_ID)).thenReturn(Collections.emptyList());

        List<Movie> moviesByCity = movieRepository.getByCityId(CITY_ID);

        assertThat(moviesByCity).isEmpty();
    }

    @Test
    public void returnsListOfReleaseMoviesCurrentlyPlayingInTheCity() {
        // FIXME add this test case
        Assertions.assertTrue(false);
    }
}