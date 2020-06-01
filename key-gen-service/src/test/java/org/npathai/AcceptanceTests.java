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

    public static final String BASEURL = "http://localhost:" + KeyGenServiceLauncher.PORT;
    static KeyGenServiceLauncher keyGenServiceLauncher;

    @BeforeClass
    public static void createLauncher() throws Exception {
        keyGenServiceLauncher = new KeyGenServiceLauncher();
        keyGenServiceLauncher.start();
        keyGenServiceLauncher.awaitInitialization();
    }

    @AfterClass
    public static void stop() throws InterruptedException {
        keyGenServiceLauncher.stop();
    }
}
