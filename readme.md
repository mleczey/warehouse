### How to compile the application?
- install java 15 (can be installed using sdkman)
- run ./mvnw install
- fat jar can be found in /target/warehouse-0.0.1-SNAPSHOT-runner.jar

- to start MySQL database use docker-compose (file located at the root of the project)
- in order to convert data from USA date format use date_converter.py located in /src/main/python

### What to do next?
- instead of strings in criteria api use generated name of fields from entity - JPA model generator
- different user for liquibase, different user (read only) for application
- add tags for integration, database test and fire it with failsafe maven plugin
- jooq do not generate classes every time
- jooq do not store database properties inside pom.xml (outside) or generate metamodel based on entity classes
- headers - caching (Etag)
- add more integration tests