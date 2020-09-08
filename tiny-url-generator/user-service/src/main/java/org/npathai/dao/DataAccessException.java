package org.npathai.dao;

public class DataAccessException extends Exception {

    public DataAccessException(Exception ex) {
        super(ex);
    }
}
