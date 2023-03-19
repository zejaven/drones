## Drones app launch instructions

---

### Build & Run with Docker & Docker Compose

1. To launch the application, Docker and Docker Compose must be installed on the system and corresponding system environment variables must be set.
2. Use **start-docker.bat** to launch the application on Windows or **start-docker.sh** to launch it on Linux.
3. Once the application is deployed, the Swagger API can be accessed using the following link: http://localhost:8084/swagger-ui/index.html. Open the link to view and test all available API methods and their descriptions.

> The environment where the application was launched successfully:
> 
> Windows:
> - Docker (20.10.11, build dea9396)
> - Docker Compose (1.29.2, build 5becea4c)
> 
> Linux:
> - Docker (23.0.1, build a5ee5b1)
> - Docker Compose (v2.16.0)

---

### Alternative Build & Run with Java & Maven

1. To launch the application, Java 17 and Maven must be installed on the system and corresponding system environment variables must be set.
2. Install PostgreSQL on your system.
3. Create a database with the name **drones**, and create a database schema with the same name.
4. Create a file named **hidden.yml** in the root directory of the project, and include your PostgreSQL username and password in the following format:
```
psql:
  username: {your_username}
  password: {your_password}
```
5. Use **start-local.bat** to launch the application on Windows or **start-local.sh** to launch it on Linux.
6. Once the application is deployed, the Swagger API can be accessed using the following link: http://localhost:8084/swagger-ui/index.html. Open the link to view and test all available API methods and their descriptions.

Note: If you want to check the second profile with name **test**, you can add the '-Dspring.profiles.active=test' parameter to the Java command inside the 'start-local.bat' or 'start-local.sh' files. The **test** profile uses Hibernate auto-ddl to generate database tables, whereas the main profile uses Liquibase.

> The environment where the application was launched successfully:
>
> Windows:
> - Java (17.0.3.1 2022-04-22 LTS)
> - Maven (3.6.3 (cecedd343002696d0abb50b32b541b8a6ba2883f))
> - PostgreSQL (12.4)
>
> Linux:
> - Java (17.0.2 2022-01-18)
> - Maven (3.8.6 (84538c9988a25aec085021c365c560670ad80f63))
> - PostgreSQL (10.17)

---

### Tests

1. To run the tests, Java 17 and Maven must be installed on the system and corresponding system environment variables must be set.
2. Open a console and navigate to the working folder of the project.
3. Type **mvn test** to run the tests.

---
