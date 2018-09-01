# junit4-testing
a lightweight collection of utilities based on JUnit 4

[![Build Status](https://travis-ci.org/thimmwork/junit4-testing.svg?branch=master)](https://travis-ci.org/thimmwork/junit4-testing)

## Maven coordinates
Just add this to your dependencies:
```xml
<dependency>
    <groupId>net.thimmwork</groupId>
    <artifactId>junit4-testing</artifactId>
    <version>2.0</version>
</dependency>
```
available at maven central.

### LifecycleRule
The LifecycleRule interface allows to wrap ```@BeforeClass```, ```@Before```, ```@After```, ```@AfterClass``` behavior and more in a single class.

This can be useful when setting up the test environment is expensive and it's easier to reset a subset of the environment between test methods instead of shutting down and setting up the whole environment.

#### Usage Example
Imagine a test database you want to initialize once (create schema).
Additionally, before each test you want to reset your initial data.
And after each test, you want to truncate a few tables with dynamic data. 

With LifecycleRule, you can write a class like this:

```java
public class TestDatabase implements LifecycleRule {
    @Override
    public void beforeClass(Description description) {
        createSchema();
    }

    @Override
    public void before(Description description) {
        resetInitialData();
    }

    @Override
    public void after(Description description) {
        clearDynamicData();
    }

    @Override
    public void afterClass(Description description) {
        dropSchema();
    }
}
```
You can bind this ```TestDatabase``` to the lifecycle of your test class by this line:
```java
@ClassRule @Rule public static LifecycleRule DATABASE = new TestDatabase();
```
So if you want to use your ```TestDatabase``` in several test classes, you only need a single line of code in each class.

Note that the field has to be static and annotated with both ```@ClassRule``` and ```@Rule``` annotations due to the JUnit4 mechanisms used under the hood.

Also note that - thanks to composition - your test classes do not need to inherit a super class. This way you can write integration tests that combine multiple environments.

#### additional lifecycle methods
In addition to the above before/after methods, you can use the methods ```onMethodSuccess``` and ```onMethodFailure``` to execute code dependent on the test result:
```java
@Override
public void onMethodSuccess(Description) {
    clearEventLog();
}

@Override
public void onMethodFailure(Description) {
    exportSnapshotForErrorAnalysis();
}
```

#### before/after Suite
To call methods before/after a Suite, add a static field to your suite and annotate it with ```@ClassRule``` like so:
```java
@RunWith(Suite.class)
@SuiteClasses({MyTestCase1.class, MyTestCase2.class})
public class TestSuite {
    @ClassRule public static LifecycleRule RULE = new MySuiteLifecycle();
}
```
In your test classes you can access the same instance of your Lifecycle by referencing the static field:
```java
public class MyTestCase1 {
    @ClassRule @Rule public static LifecycleRule RULE = TestSuite.RULE;
}
```
LifecycleRule looks at child descriptions to detect suite hooks from class hooks. Usually you have a structure like
```
Suite
\- Class
   \- Method
```
However, if you have nested tests, this detection may not work for you.
In this case, you may have to override the detection mechanism in ```LifecycleRule#isSuite()``` and/or ```LifecycleRule#isClass()```.
