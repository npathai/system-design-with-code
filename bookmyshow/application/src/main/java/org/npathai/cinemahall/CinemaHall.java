package org.npathai.cinemahall;

import org.npathai.cinemahall.auditorium.Auditorium;

import java.util.ArrayList;
import java.util.List;

public class CinemaHall {
    private long id;
    private String name;
    private Address address;
    private Rating rating;
    private List<Auditorium> audis = new ArrayList<>();

    public List<Auditorium> getAudis() {
        return audis;
    }
}
