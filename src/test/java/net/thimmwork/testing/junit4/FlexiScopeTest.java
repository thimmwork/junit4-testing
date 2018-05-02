package net.thimmwork.testing.junit4;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class FlexiScopeTest {

    @ClassRule @Rule public static FlexiScopedSemaphore SEMAPHORE = new FlexiScopedSemaphore();

    @Test
    public void setUp_is_called_only_once_in_test_class() {
        assertThat("semaphore was not acquired", SEMAPHORE.isDrained(), equalTo(true));
    }

    @Test
    public void setUp_is_called_only_once_in_test_class_with_two_methods() {
        assertThat("semaphore was not acquired", SEMAPHORE.isDrained(), equalTo(true));
    }
}
