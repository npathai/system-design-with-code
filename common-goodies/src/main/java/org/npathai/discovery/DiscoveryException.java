package org.npathai.discovery;

public class DiscoveryException extends Exception {
    public DiscoveryException(Exception ex) {
        super(ex);
    }

    public DiscoveryException(String message) {
        super(message);
    }
}
