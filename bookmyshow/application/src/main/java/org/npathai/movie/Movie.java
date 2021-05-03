package org.npathai.movie;

import org.npathai.cinemahall.show.Show;
import org.npathai.city.CityId;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Movie {
    MovieId movieId;
    LocalDate releaseDate;
    String name;
    Genre genre;
    String language;
    private Duration runDuration;

    public List<Show> getShows(CityId cityId) {
        return Collections.emptyList();
    }

    public MovieId getMovieId() {
        return movieId;
    }

    public void setId(MovieId movieId) {
        this.movieId = movieId;
    }

    public Duration getRunDuration() {
        return runDuration;
    }

    public void setDuration(Duration runDuration) {
        this.runDuration = runDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
