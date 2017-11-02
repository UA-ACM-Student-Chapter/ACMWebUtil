# ACM Web Utility

- Helps us automate some of the more mundane tasks.

[![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)

## Running the Web Utility

### Through the IDE 

Import the project into IntelliJ IDEA (default settings should all be correct), click Run with the default Run Configuration

### From the command line

 1. Install [Apache Maven](https://maven.apache.org/install.html). This is the project and dependency management tool used by the project
 2. Run `mvn spring-boot:run` [^1] in the directory where you cloned the  repository. 

[^1]: `mvn` is the command used to work with Maven. `spring-boot:run` is the target defined by the Spring plugin for maven that compiles the project and starts it using a development server
