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

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This class is designed as a base class for test resources that provides multiple lifecycle hooks
 * that combine JUnit's <code>before</code>/<code>beforeClass</code> and <code>after</code>/
 * <code>afterClass</code> by leveraging its <code>@ClassRule</code>/<code>@Rule</code> mechanism.
 *
 * <p>Since this class does not hold any state, it is implemented as an interface with default methods.
 *
 * <p>When using <code>beforeSuite</code>/<code>afterSuite</code>, make sure you use the same instance of
 * your <code>LifecycleRule</code> subclass in both your suite and test classes.
 */
public interface LifecycleRule extends TestRule {
    /**
     * wraps the executed statement into a new statement that will call the appropriate lifecycle methods
     */
    default Statement apply(Statement baseStatement, Description description) {
        if (isSuite(description)) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    beforeSuite(description);
                    try {
                        baseStatement.evaluate();
                    } finally {
                        afterSuite(description);
                    }
                }
            };
        }
        if (isClass(description)) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    beforeClass(description);
                    try {
                        baseStatement.evaluate();
                    } finally {
                        afterClass(description);
                    }
                }
            };
        }
        if (description.isTest()) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    before(description);
                    try {
                        baseStatement.evaluate();
                        onMethodSuccess(description);
                    } catch (Throwable throwable) {
                        onMethodFailure(description, throwable);
                        throw throwable;
                    } finally {
                        after(description);
                    }
                }
            };
        }
        return baseStatement;
    }

    default boolean isClass(Description description) {
        return !description.getChildren().isEmpty()
                && description.getChildren().get(0).getChildren().isEmpty();
    }

    default boolean isSuite(Description description) {
        if (description.getChildren().isEmpty()) {
            return false;
        }
        Description child = description.getChildren().get(0);
        if (child.getChildren().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * override this method to add code that should be called before each test class or suite
     */
    default void beforeSuite(Description description) throws Throwable {}

    /**
     * override this method to add code that should be called before each test class or suite
     */
    default void beforeClass(Description description) throws Throwable {}

    /**
     * override this method to add code that should be called before each test method
     */
    default void before(Description description) throws Throwable {}

    /**
     * override this method to add code that should be called after each successful completion of a test method
     */
    default void onMethodSuccess(Description description) throws Throwable {}

    /**
     * override this method to add code that should be called after each failed test method
     */
    default void onMethodFailure(Description description, Throwable t) throws Throwable {}

    /**
     * override this method to add code that should be called after each test method
     */
    default void after(Description description) throws Throwable {}

    /**
     * override this method to add code that should be called before each test class or suite
     */
    default void afterClass(Description description) throws Throwable {}

    /**
     * override this method to add code that should be called before each test class or suite
     */
    default void afterSuite(Description description) throws Throwable {}
}
