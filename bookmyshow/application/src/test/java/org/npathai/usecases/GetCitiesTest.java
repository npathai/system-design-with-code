package org.npathai.usecases;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.npathai.city.City;
import org.npathai.city.CityDao;
import org.npathai.city.CityId;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCitiesTest {

    @Mock
    CityDao cityDao;

    @Test
    public void returnsAllRegisteredCities() {
        List<City> cities = List.of(city(1, "city1"), city(2, "city2"));
        when(cityDao.getCities()).thenReturn(cities);
        GetCities getCities = new GetCities(cityDao);
        List<City> answer = getCities.execute();
        assertThat(answer).containsExactlyElementsOf(cities);
    }

    private City city(long id, String name) {
        return new City(new CityId(id), name);
    }
}
