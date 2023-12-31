# List-manager

## About
List Manager is a versatile platform that allows users to create, edit, and share various types of lists, built using SpringBoot + React + Typescript. Uses Spring Security & Cookie authorization.

## Requirements

- Java 19 or higher
- Node.js
- PostgreSQL
- Yarn or npm
- Maven
- Docker

## Environment Variables
The following environment variables need to be set in the '.env' file:
```properties
# The URL of your PostgreSQL database.
SPRING_DATASOURCE_URL= 

# The username for your PostgreSQL database.
SPRING_DATASOURCE_USERNAME=

# The password for your PostgreSQL database.
SPRING_DATASOURCE_PASSWORD=

# The username for your PostgreSQL service.
POSTGRES_USER=

# The password for your PostgreSQL service.
POSTGRES_PASSWORD=

# The URL of your API (locates in /frontend)
VITE_API_URL= 
```
## Installation

1. Clone the repository
2. Install the dependences using Maven and Yarn/npm
3. Set the environment variables in the '.env' files.
4. Generate a .jar file: ```mvn install -DskipTests```

#### Docker Compose
1. Build the Docker images: ```docker-compose build```
2. Run the Docker containers: ```docker-compose up```

Tip: If you encounter an error `Connection to localhost:5432 refused` , change `localhost` to `host.docker.internal` in .`.env` file
## API Documentation
This project uses Swagger UI for API documentation. Once the application is running, you can access the Swagger UI at `http://localhost:8080/swagger-ui.html` (replace `localhost:8080` with your server's address and port if necessary).
## Frontend Setup

To setup the frontend, navigate to the `/frontend` directory and run the following commands:

```bash
cd /frontend
yarn
yarn run dev
```
This will install the necessary dependencies and start the development server.

## Tests

This project uses JUnit and Mockito for unit testing and integration testing.

To run the tests, use the following command:

```bash
mvn test
```
The tests cover all the service and controller layers, ensuring the correct behavior of the application.

We strive to maintain a high level of code coverage and regularly check this using coverage from IntelliJ IDEA.

The current code coverage is 80%.
## License

[MIT License](LICENSE)