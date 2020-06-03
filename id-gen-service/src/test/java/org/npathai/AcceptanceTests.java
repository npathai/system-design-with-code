package org.npathai;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.npathai.api.IdGeneratorAPIAcceptanceTest;

import java.io.IOException;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        IdGeneratorAPIAcceptanceTest.class
})
public class AcceptanceTests {

    public static final String BASEURL = "http://localhost:" + IdGenServiceLauncher.PORT;
    static IdGenServiceLauncher idGenServiceLauncher;

    @BeforeClass
    public static void createLauncher() throws Exception {
        idGenServiceLauncher = new IdGenServiceLauncher();
        idGenServiceLauncher.start();
        idGenServiceLauncher.awaitInitialization();
    }

    @AfterClass
    public static void stop() throws InterruptedException, IOException {
        idGenServiceLauncher.stop();
    }
}
