package org.npathai.cinemahall.auditorium;

public class Auditorium {
    private long id;
    private String name;
    private int seatCount;

    public Auditorium(long id, String name, int seatCount) {
        this.id = id;
        this.name = name;
        this.seatCount = seatCount;
    }

    public AuditoriumId getId() {
        return new AuditoriumId(id);
    }
}
