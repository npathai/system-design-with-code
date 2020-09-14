package org.npathai.usecases;

import org.npathai.cinemahall.Address;
import org.npathai.cinemahall.CinemaHall;
import org.npathai.cinemahall.Rating;
import org.npathai.cinemahall.auditorium.Auditorium;

import java.security.cert.CertPathBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CinemaHallBuilder {
    private static final Random random = new Random();
    private final long id;
    private String name;
    private Address address;
    private Rating rating;
    private List<Auditorium> audis = new ArrayList<>();

    public CinemaHallBuilder(int id) {
        this.id = id;
        name = "CinemaHall-" + id;
    }

    public static CinemaHallBuilder aCinemaHall() {
        CinemaHallBuilder cinemaHallBuilder = new CinemaHallBuilder(random.ints(1, 1000000)
                .findFirst().getAsInt());
        return cinemaHallBuilder;
    }

    public AuditoriumBuilder anAuditorium() {
        return new AuditoriumBuilder(random.ints(1, 1000000)
                .findFirst().getAsInt());
    }

    public CinemaHall build() {
        return new CinemaHall(id, name, address, rating, audis);
    }

    public class AuditoriumBuilder {
        private long id;
        private String name;
        private int seatCount;

        public AuditoriumBuilder(int id) {
            name = "Audi-" + id;
        }

        public CinemaHallBuilder build() {
            audis.add(new Auditorium(id, name, seatCount));
            return CinemaHallBuilder.this;
        }
    }
}
