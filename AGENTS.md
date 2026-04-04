# AGENTS.md

## Cursor Cloud specific instructions

This is a Java SDK library (not a runnable application) for the NEWPOS STORE platform. It builds with Maven and targets Java 8 bytecode.

### Environment
- **JDK 11** is required (not JDK 21; the older maven-compiler-plugin 3.6.2 + Lombok 1.18.0 hit `ExceptionInInitializerError` on JDK 17+).
- `JAVA_HOME` is set to `/usr/lib/jvm/java-11-openjdk-amd64` in `~/.bashrc`.
- Maven is installed via `apt` (`/usr/bin/mvn`).

### Build / Test / Package
- `mvn clean compile` — compile sources
- `mvn test` — run JUnit 4 integration tests (2 tests hit the external NEWPOS STORE dev API)
- `mvn clean package` — compile + test + produce JAR at `target/newstore-openapi-sdk-1.0.0.jar`
- `mvn install` — install JAR into local Maven repo

### Gotchas
- Tests make live HTTPS calls to `https://idcenter2.newposp.com:8099` using hardcoded test credentials in `TestApiConstants.java`. The first test returns `"api key sign error"` and the second returns `"invalid terminal"`, but both tests *pass* (no assertions on API response success). Network access is required.
- There is no lint tool configured; the project has no checkstyle/spotbugs/PMD setup.
- This is a library, not a server — there are no services to start.
