package org.npathai;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.npathai.controller.UrlShortnerControllerAcceptanceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UrlShortnerControllerAcceptanceTest.class
})
public class AcceptanceTests {

    public static final String BASEURL = "http://localhost:" + Main.PORT;
    static Main main;

    @BeforeClass
    public static void createMain() {
        main = new Main();
        main.start();
        main.awaitInitialization();
    }

    @AfterClass
    public static void stop() {
        main.stop();
    }
}
