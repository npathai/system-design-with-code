package org.npathai.cinemahall;

import org.npathai.city.CityId;

import java.util.List;

public interface CinemaHallDao {
    List<CinemaHall> getCinemaHalls(CityId cityId);
}
