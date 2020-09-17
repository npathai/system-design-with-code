package org.npathai.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.npathai.cinemahall.CinemaHall;
import org.npathai.cinemahall.CinemaHallBuilder;
import org.npathai.cinemahall.CinemaHallDao;
import org.npathai.cinemahall.auditorium.Auditorium;
import org.npathai.cinemahall.auditorium.AuditoriumId;
import org.npathai.cinemahall.show.Show;
import org.npathai.cinemahall.show.ShowBuilder;
import org.npathai.cinemahall.show.ShowDao;
import org.npathai.city.CityId;
import org.npathai.movie.MovieId;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetShowsByMovieTest {
    private static final CityId CURRENT_CITY_ID = new CityId(1L);
    public static final MovieId ANY_MOVIE_ID = new MovieId(1L);
    public static final MovieId HARRY_POTTER_MOVIE_ID = new MovieId(2L);

    private static final CinemaHall CINEMA_HALL_WITH_2_AUDITORIUMS
            = CinemaHallBuilder.aCinemaHall()
                .anAuditorium().build()
                .anAuditorium().build()
            .build();

    private static final Show HARRY_POTTER_SHOW_1 = ShowBuilder.aShow()
            .withAuditoriumId(CINEMA_HALL_WITH_2_AUDITORIUMS.getAudis().get(0).getId())
            .withMovieId(HARRY_POTTER_MOVIE_ID)
            .build();
    private static final Show HARRY_POTTER_SHOW_2 = ShowBuilder.aShow()
            .withAuditoriumId(CINEMA_HALL_WITH_2_AUDITORIUMS.getAudis().get(1).getId())
            .withMovieId(HARRY_POTTER_MOVIE_ID)
            .build();;

    private GetShowsByMovie getShowsByMovie;
    @Mock
    private CinemaHallDao cinemaHallDao;
    @Mock
    private ShowDao showDao;
    @Captor
    private ArgumentCaptor<AuditoriumId> auditoriumIdArgumentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        getShowsByMovie = new GetShowsByMovie(cinemaHallDao, showDao);
    }

    @Test
    public void returnsNoShowsWhenNoCinemaHallIsAssociatedForACity() {
        when(cinemaHallDao.getCinemaHalls(CURRENT_CITY_ID)).thenReturn(Collections.emptyList());

        Shows shows = getShowsByMovie.execute(ANY_MOVIE_ID, CURRENT_CITY_ID);

        assertThat(shows.getAll()).hasSize(0);
    }

    @Test
    public void skipsCinemaHallsWhenNoneOfTheAuditoriumsAreNotShowingTheMovie() {
        when(cinemaHallDao.getCinemaHalls(CURRENT_CITY_ID)).thenReturn(List.of(CINEMA_HALL_WITH_2_AUDITORIUMS));
        when(showDao.getShows(CINEMA_HALL_WITH_2_AUDITORIUMS.getAudis().get(0).getId(), HARRY_POTTER_MOVIE_ID))
                .thenReturn(Collections.emptyList());
        when(showDao.getShows(CINEMA_HALL_WITH_2_AUDITORIUMS.getAudis().get(1).getId(), HARRY_POTTER_MOVIE_ID))
                .thenReturn(Collections.emptyList());

        Shows shows = getShowsByMovie.execute(HARRY_POTTER_MOVIE_ID, CURRENT_CITY_ID);

        assertThat(shows.getAll()).hasSize(0);
    }

    @Test
    public void returnsShowsOfAllCinemaHallsInCurrentCityShowingMovie() {
        when(cinemaHallDao.getCinemaHalls(CURRENT_CITY_ID)).thenReturn(List.of(CINEMA_HALL_WITH_2_AUDITORIUMS));
        when(showDao.getShows(any(AuditoriumId.class), any(MovieId.class)))
                .thenReturn(List.of(HARRY_POTTER_SHOW_1))
                .thenReturn(List.of(HARRY_POTTER_SHOW_2));

        Shows shows = getShowsByMovie.execute(HARRY_POTTER_MOVIE_ID, CURRENT_CITY_ID);

        Map<CinemaHall, List<Show>> expected = Map.of(CINEMA_HALL_WITH_2_AUDITORIUMS, List.of(HARRY_POTTER_SHOW_1, HARRY_POTTER_SHOW_2));
        assertThat(shows.getAll()).isEqualTo(expected);
        verify(showDao, Mockito.times(2)).getShows(auditoriumIdArgumentCaptor.capture(), eq(HARRY_POTTER_MOVIE_ID));

        assertThat(auditoriumIdArgumentCaptor.getAllValues()).isEqualTo(List.of(
                CINEMA_HALL_WITH_2_AUDITORIUMS.getAudis().get(0).getId(),
                CINEMA_HALL_WITH_2_AUDITORIUMS.getAudis().get(1).getId()
        ));
    }
}