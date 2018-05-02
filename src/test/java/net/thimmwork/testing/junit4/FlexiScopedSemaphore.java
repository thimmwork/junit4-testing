package net.thimmwork.testing.junit4;

import org.junit.runner.Description;

import java.util.concurrent.Semaphore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class FlexiScopedSemaphore extends FlexiScope {
    private Semaphore semaphore = new Semaphore(1);

    @Override
    public void setUp(Description description) {
        System.out.println("draining permits... " + description);
        int noOfPermits = semaphore.drainPermits();
        assertThat("no permits available. did this method get called multiple times?",
                noOfPermits, equalTo(1));
    }

    @Override
    public void tearDown(Description description) {
        System.out.println("releasing permits... " + description);
        semaphore.release();
        assertThat("more than one permit available. did this method get called multiple times?",
                semaphore.availablePermits(), equalTo(1));
    }

    public boolean isDrained() {
        System.out.println("available permits: " + semaphore.availablePermits());
        return semaphore.availablePermits() == 0;
    }
}
