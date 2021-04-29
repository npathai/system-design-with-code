package org.npathai.usecases;

import org.npathai.city.City;
import org.npathai.city.CityDao;

import java.util.List;

public class GetCities {
    private final CityDao cityDao;

    public GetCities(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    public List<City> execute() {
        return cityDao.getCities();
    }
}
