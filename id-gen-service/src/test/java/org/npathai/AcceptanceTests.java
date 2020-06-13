package org.npathai;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.npathai.api.IdGeneratorAPIAcceptanceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        IdGeneratorAPIAcceptanceTest.class
})
public class AcceptanceTests {

//    public static final String BASEURL = "http://localhost:" + Application.PORT;
    static Application application;

    @BeforeClass
    public static void createLauncher() throws Exception {
//        application = new Application();
//        application.start();
//        application.awaitInitialization();
    }

    @AfterClass
    public static void stop() throws Exception {
//        application.stop();
    }
}
