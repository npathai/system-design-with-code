package org.npathai.movie;

import org.npathai.cinemahall.show.Show;
import org.npathai.city.CityId;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class Movie {
    MovieId movieId;
    String name;
    Genre genre;
    String language;
    private Duration runDuration;
    private ZonedDateTime releaseDate;

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

    public void setReleaseDate(ZonedDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ZonedDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }
}
