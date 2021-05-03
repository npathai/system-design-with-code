package org.npathai.usecases;

import org.npathai.city.CityId;
import org.npathai.movie.Movie;
import org.npathai.movie.MovieRepository;

import java.util.List;

public class GetMoviesByCityId {
    private final MovieRepository movieRepository;
    private final CityId cityId;

    public GetMoviesByCityId(MovieRepository movieRepository, CityId cityId) {
        this.movieRepository = movieRepository;
        this.cityId = cityId;
    }

    public List<Movie> execute() {
        return movieRepository.getByCityId(cityId);
    }
}
