package org.npathai.movie;

import org.npathai.cinemahall.show.Show;
import org.npathai.city.CityId;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Movie {
    long id;
    LocalDate releaseDate;
    String name;
    Genre genre;
    String language;
    Duration duration;

    public List<Show> getShows(CityId cityId) {
        return Collections.emptyList();
    }
}
