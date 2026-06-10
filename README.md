# Enterprise UI and API Automation Framework

Maven/TestNG framework for Selenium UI automation and RestAssured API automation.

## Stack

- Java 21
- Selenium WebDriver
- Maven
- TestNG
- Page Object Model
- Thread-safe parallel execution with `ThreadLocal<WebDriver>`
- Allure reporting with failure screenshots
- TestNG listeners and retry hooks
- RestAssured API layer
- Jackson serialization/deserialization
- Token handling
- Centralized exception handling and utilities

## Run

```bash
mvn clean test
mvn allure:serve
```

Run the full UI and API suite explicitly:

```bash
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/testng.xml"
```

Override configuration from the command line:

```bash
mvn clean test -Dbrowser=firefox -Dheadless=true -Dui.base.url=https://your-app.example.com -Dapi.base.url=https://api.example.com
```

## Project Layout

```text
src/main/java/com/enterprise/automation
  config       Configuration loading
  constants    Framework constants
  driver       Browser creation and ThreadLocal WebDriver lifecycle
  exceptions   Framework-specific runtime exception
  reports      Allure attachment helpers
  utils        Wait, JSON, screenshot, and shared utilities

src/test/java/com/enterprise/automation
  api          API clients, models, and tests
  base         Test base classes
  listeners    TestNG listeners and retry analyzer
  pages        Page Object Model classes
  ui           UI tests
```

## Notes

- Update `src/test/resources/config/config.properties` with your application URLs and auth details.
- Keep page locators inside page classes only.
- Keep API endpoints inside API client classes only.
- Add new suites under `src/test/resources/suites` for smoke, regression, API-only, or UI-only runs.
