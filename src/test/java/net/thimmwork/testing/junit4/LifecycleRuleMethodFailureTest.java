package net.thimmwork.testing.junit4;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LifecycleRuleMethodFailureTest {

    @Test
    public void when_a_method_fails_then_lifecycle_methods_including_onMethodFail_are_invoked_in_correct_order() throws InitializationError {
        BlockJUnit4ClassRunner runner = new BlockJUnit4ClassRunner(TestSubject.class);
        RunNotifier notifier = new RunNotifier();
        runner.run(notifier);
        int expectedLifecycleInvocations = InvocationOrderVerifier.Invocation.values().length;
        assertThat(InvocationOrderVerifier.counter.get(), equalTo(expectedLifecycleInvocations));
    }

    public static class TestSubject {
        @ClassRule @Rule public static LifecycleRule ON_FAILURE_VERIFIER = new InvocationOrderVerifier();

        @Test
        public void a_failing_test_method() {
            InvocationOrderVerifier.verifyInvocation(InvocationOrderVerifier.Invocation.METHOD_INVOCATION);
            fail();
        }
    }

    private static class InvocationOrderVerifier implements LifecycleRule {
        private static AtomicInteger counter = new AtomicInteger();

        private enum Invocation {
            BEFORE_CLASS,
            BEFORE,
            METHOD_INVOCATION,
            METHOD_FAILURE,
            AFTER,
            AFTER_CLASS
        }

        static void verifyInvocation(Enum<?> invocation) {
            Assert.assertThat(counter.getAndIncrement(), equalTo(invocation.ordinal()));
        }

        @Override
        public void beforeClass(Description description) {
            verifyInvocation(Invocation.BEFORE_CLASS);
        }

        @Override
        public void before(Description description) {
            verifyInvocation(Invocation.BEFORE);
        }

        @Override
        public void onMethodFailure(Description description, Throwable t) {
            verifyInvocation(Invocation.METHOD_FAILURE);
        }

        @Override
        public void after(Description description) {
            verifyInvocation(Invocation.AFTER);
        }

        @Override
        public void afterClass(Description description) {
            verifyInvocation(Invocation.AFTER_CLASS);
        }
    }
}
