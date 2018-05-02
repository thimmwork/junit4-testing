package net.thimmwork.testing.junit4;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(Suite.class)
@Suite.SuiteClasses({FlexiScopeSuiteTest.FlexiScopeTestClass.class, FlexiScopeSuiteTest.FlexiScopeTestClass.class})
public class FlexiScopeSuiteTest {

    @ClassRule public static FlexiScopedSemaphore SEMAPHORE = new FlexiScopedSemaphore();

    public static class FlexiScopeTestClass {

        @ClassRule @Rule
        public static FlexiScopedSemaphore SEMAPHORE = FlexiScopeSuiteTest.SEMAPHORE;

        @Test
        public void setUp_is_called_only_once_in_test_suite() {
            assertThat("semaphore was not acquired", SEMAPHORE.isDrained(), equalTo(true));
        }
    }

}
