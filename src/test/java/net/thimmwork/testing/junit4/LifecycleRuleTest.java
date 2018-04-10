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

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.IsEqual.equalTo;

public class LifecycleRuleTest {

    @ClassRule @Rule public static LifecycleRule VERIFIER = new InvocationOrderVerifier();

    private static final AtomicInteger counter = new AtomicInteger();

    @Test
    public void  lifecycle_methods_are_calledin_the_appropriate_order() {
        verifyInvocation(Invocation.METHOD_INVOCATION);
    }

    enum Invocation {
        BEFORE_CLASS,
        BEFORE,
        METHOD_INVOCATION,
        ON_METHOD_SUCCESS,
        AFTER,
        AFTER_CLASS
    }

    private static void verifyInvocation(Enum<?> invocation) {
        Assert.assertThat(counter.getAndIncrement(), equalTo(invocation.ordinal()));
    }

    private static class InvocationOrderVerifier implements LifecycleRule {

        @Override
        public void before(Description description) {
            verifyInvocation(Invocation.BEFORE);
        }

        @Override
        public void onMethodSuccess(Description description) {
            verifyInvocation(Invocation.ON_METHOD_SUCCESS);
        }

        @Override
        public void after(Description description) {
            verifyInvocation(Invocation.AFTER);
        }

        @Override
        public void beforeClass(Description description) {
            verifyInvocation(Invocation.BEFORE_CLASS);
        }

        @Override
        public void afterClass(Description description) {
            verifyInvocation(Invocation.AFTER_CLASS);
        }
    }
}
