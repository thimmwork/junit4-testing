/**
 * Copyright 2018 thimmwork
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.thimmwork.testing.junit4;

import org.junit.*;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Suite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(Suite.class)
@Suite.SuiteClasses({LifecycleRuleSuiteTest.TheTest.class})
public class LifecycleRuleSuiteTest {

    @ClassRule public static LifecycleRule VERIFIER = new InvocationOrderVerifier();

    private static final AtomicInteger counter = new AtomicInteger();

    public static class TheTest {
        @ClassRule @Rule public static LifecycleRule VERIFIER = LifecycleRuleSuiteTest.VERIFIER;

        @Test
        public void lifecycle_methods_are_calledin_the_appropriate_order() {
            verifyInvocation(Invocation.METHOD_INVOCATION);
        }
    }

    enum Invocation {
        BEFORE_SUITE,
        BEFORE_CLASS,
        BEFORE,
        METHOD_INVOCATION,
        ON_CLASS_SUCCESS,
        AFTER,
        AFTER_CLASS,
        AFTER_SUITE
    }

    private static void verifyInvocation(Enum<?> invocation) {
        Assert.assertThat(counter.getAndIncrement(), equalTo(invocation.ordinal()));
    }

    private static class InvocationOrderVerifier implements LifecycleRule {

        @Override
        public void beforeSuite(Description description) throws Throwable {
            verifyInvocation(Invocation.BEFORE_SUITE);
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
        public void onMethodSuccess(Description description) {
            verifyInvocation(Invocation.ON_CLASS_SUCCESS);
        }

        @Override
        public void after(Description description) {
            verifyInvocation(Invocation.AFTER);
        }

        @Override
        public void afterClass(Description description) {
            verifyInvocation(Invocation.AFTER_CLASS);
        }

        @Override
        public void afterSuite(Description description) throws Throwable {
            verifyInvocation(Invocation.AFTER_SUITE);
        }
    }
}
