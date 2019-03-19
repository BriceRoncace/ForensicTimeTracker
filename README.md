# Forensic Time Tracker

## Version 1.0 (March 2019)

### Quick Start

1. Download or clone source code

2. Maven build 
```
ForensicTimeTracker-1.0> mvn clean install
...
```

3. CD to target directory and execute the .war file
```
ForensicTimeTracker-2.0\target>java -jar ForensicTimeTracker-1.0.war

 
```

4. Browse to http://localhost:8080/ForensicTimeTracker

5. Log in with default in-memory user credentials (username and password "admin")

6. Add (database) users


### Build Profiles

The application is Maven based.  By default the **dev** profile is active.  When you run Maven goals without a profile explicitly defined, **dev** will be used:

```
> mvn clean install
```

The application contains a **prod** profile but this profile must be explicitly specified like so:

```
> mvn -Pprod clean install
```

When the prod profile is used, the **/src/main/resources/application-prod.properties** will be referenced and will override any property found in the base properties file, **/src/main/resources/application.properties**.  In this way, for example, the application can be configured to, for example, disable the **mail.test.mode** property in production.


### Database Configuration

Using the default dev Maven profile, only the **/src/main/resources/application.properties** Spring Boot configuration file will be used.  This file specifies an H2 database:

```
# H2
spring.h2.console.enabled = true
spring.datasource.url = jdbc:h2:file:~/FTT;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name = org.h2.Driver
spring.datasource.username = sa
spring.datasource.password = 
spring.jpa.hibernate.ddl-auto = update
spring.jpa.show-sql = false
```

The above configuration will attempt to create a new FTT H2 database in the home directory of the user running the application.  The database is file based and will therefore persist data between application restarts.

This setup requires that the H2 dependencies are available which is the case based on the Maven pom.xml:

```
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```

To change the database used, for example to MariaDB, change the Maven dependencies:

```
<dependency>
  <groupId>org.mariadb.jdbc</groupId>
  <artifactId>mariadb-java-client</artifactId>
  <scope>runtime</scope>
</dependency>

<!-- H2 dependency no longer needed; can be commented out or removed completely
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
-->
```

And now the application.properties file can be updated to contain the connection information for MariaDB:

```
# MariaDB
spring.jpa.database-platform=org.hibernate.dialect.MariaDB102Dialect
spring.datasource.url=jdbc:mariadb://localhost:3306/ForensicTimeTracker
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.username = dev_user
spring.datasource.password = test
spring.jpa.generate-ddl = true
spring.jpa.show-sql = false
```
