package org.npathai.movie;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Random;

public class MovieBuilder {
    private final Movie movie;

    public MovieBuilder() {
        movie = new Movie();
    }

    public MovieBuilder withId(long id) {
        movie.setId(new MovieId(id));
        return this;
    }

    public MovieBuilder withName(@NotNull String name) {
        movie.setName(name);
        return this;
    }

    public MovieBuilder withReleaseDate(ZonedDateTime releaseDateTime) {
        movie.setReleaseDate(releaseDateTime);
        return this;
    }

    public MovieBuilder withDuration(Duration duration) {
        movie.setDuration(duration);
        return this;
    }

    private MovieBuilder withGenre(Genre genre) {
        movie.setGenre(genre);
        return this;
    }

    public Movie create() {
        return movie;
    }

    public static MovieBuilder aMovieNamed(String name) {
        final Random random = new Random();
        return new MovieBuilder()
                .withId(random.nextLong())
                .withName(name)
                .withReleaseDate(ZonedDateTime.now().minusDays(random.nextInt()))
                .withGenre(Genre.values()[random.nextInt(Genre.values().length)])
                .withDuration(Duration.ofHours(random.nextInt(5)));
    }
}
