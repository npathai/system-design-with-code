package org.npathai;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.npathai.api.UrlShortenerAPIAcceptanceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UrlShortenerAPIAcceptanceTest.class
})
public class AcceptanceTests {

    public static final String BASEURL = "http://localhost:" + ShortUrlGeneratorLauncher.PORT;
    static ShortUrlGeneratorLauncher shortUrlGeneratorLauncher;

    @BeforeClass
    public static void createMain() {
        shortUrlGeneratorLauncher = new ShortUrlGeneratorLauncher();
        shortUrlGeneratorLauncher.start();
        shortUrlGeneratorLauncher.awaitInitialization();
    }

    @AfterClass
    public static void stop() {
        shortUrlGeneratorLauncher.stop();
    }
}
