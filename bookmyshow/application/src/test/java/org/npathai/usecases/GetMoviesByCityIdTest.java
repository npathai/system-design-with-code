package org.npathai.usecases;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.npathai.city.CityId;
import org.npathai.movie.Movie;
import org.npathai.movie.MovieId;
import org.npathai.movie.MovieRepository;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GetMoviesByCityIdTest {

    private static final CityId CITY_ID = new CityId(1L);
    private static final Movie HARRY_POTTER_MOVIE = createHarryPotterMovie();
    private static final Movie INTERSTELLAR_MOVIE = createInterstellarMovie();

    @Mock
    MovieRepository movieRepository;

    @Test
    public void returnsAllMoviesRunningInCity() {
        GetMoviesByCityId useCase = new GetMoviesByCityId(movieRepository, CITY_ID);
        Mockito.when(movieRepository.getByCityId(CITY_ID)).thenReturn(List.of(HARRY_POTTER_MOVIE, INTERSTELLAR_MOVIE));
        List<Movie> movies = useCase.execute();
        assertThat(movies).containsExactlyElementsOf(List.of(HARRY_POTTER_MOVIE, INTERSTELLAR_MOVIE));
    }

    private static Movie createHarryPotterMovie() {
        Movie movie = new Movie();
        movie.setId(new MovieId(1L));
        movie.setDuration(Duration.ofMinutes(200));
        movie.setName("Harry Potter and the Sorcerers stone");
        return movie;
    }

    private static Movie createInterstellarMovie() {
        Movie movie = new Movie();
        movie.setId(new MovieId(2L));
        movie.setDuration(Duration.ofMinutes(240));
        movie.setName("Interstellar");
        return movie;
    }
}
