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

import org.junit.runner.Description;

/**
 * This class is designed as a base class for test resources that need to be initialized/teared down
 * once and are required to be executable in both Suite and single test classes, but perform operations
 * that are too expensive to execute before/after every test class.
 * Examples are databases, container orchestration or insertion of large datasets.
 *
 * <p>FlexiScope will detect the scope (suite/class/method) it runs in and call the setUp()/tearDown() methods
 * only once in the corresponding scope.
 *
 * <p>Make sure you use the same instance of your FlexiScope subclass in both your suite and test classes.
 */
public abstract class FlexiScope implements LifecycleRule {
    private Scope scope;

    public abstract void setUp(Description description);
    public abstract void tearDown(Description description);

    @Override
    public void beforeSuite(Description description) {
        scope = Scope.SUITE;
        setUp(description);
    }

    @Override
    public void beforeClass(Description description) {
        if (scope == null) {
            scope = Scope.CLASS;
            setUp(description);
        }
    }

    @Override
    public void before(Description description) {
        if (scope == null) {
            scope = Scope.METHOD;
            setUp(description);
        }
    }

    @Override
    public void after(Description description) {
        if (scope == Scope.METHOD) {
            tearDown(description);
        }
    }

    @Override
    public void afterClass(Description description) {
        if (scope == Scope.CLASS) {
            tearDown(description);
        }
    }

    @Override
    public void afterSuite(Description description) {
        if (scope == Scope.SUITE) {
            tearDown(description);
        }
    }

    private enum Scope {
        SUITE, CLASS, METHOD
    }
}
