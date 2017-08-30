# DEBITUM assets service

### Prerequisites

* JDK 8 (**Make sure _JAVA_HOME_ environmental variable points to _JDK8_.**)

### Getting Started

#### Building

In command line:
* `./gradlew clean build` to build the project.

#### Testing

If you only want to run tests use `./gradlew clean test`. Tests will be available in `build/reports/tests/index.html`.

#### Running


To pack a jar and run with custom profile:
   + `./gradlew clean build`
   + `java -jar build/libs/assets.jar -Dspring.profiles.active=custom`
 OR
   + `SPRING_PROFILES_ACTIVE=custom ./gradlew clean bootRun`

