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

public interface LifecycleRule extends TestRule {
    /**
     * wraps the executed statement into a new statement that will call the appropriate lifecycle methods
     */
    default Statement apply(Statement baseStatement, Description description) {
        if (description.isSuite()) {
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

    /**
     * override this method to add code that should be called before each test class or suite
     */
    default void beforeClass(Description description) {}

    /**
     * override this method to add code that should be called before each test method
     */
    default void before(Description description) {}

    /**
     * override this method to add code that should be called after each successful completion of a test method
     */
    default void onMethodSuccess(Description description) {}

    /**
     * override this method to add code that should be called after each failed test method
     */
    default void onMethodFailure(Description description, Throwable t) {}

    /**
     * override this method to add code that should be called after each test method
     */
    default void after(Description description) {}

    /**
     * override this method to add code that should be called before each test class or suite
     */
    default void afterClass(Description description) {}
}
