package org.npathai.cinemahall.show;

import org.npathai.cinemahall.auditorium.AuditoriumId;
import org.npathai.movie.MovieId;

import java.util.List;

public interface ShowDao {
    List<Show> getShows(AuditoriumId id, MovieId movieId);
}
