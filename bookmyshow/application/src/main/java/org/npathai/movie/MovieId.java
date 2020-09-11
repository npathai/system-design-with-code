package org.npathai.movie;

import org.npathai.common.ValueObject;

public class MovieId extends ValueObject<Long>  {

    public MovieId(Long value) {
        super(value);
    }
}
