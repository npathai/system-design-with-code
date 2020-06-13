package org.npathai;

import io.micronaut.runtime.Micronaut;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {
    private static final Logger LOG = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }

}
