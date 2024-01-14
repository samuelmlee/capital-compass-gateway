# Capital-Compass - API Gateway

## Description

The API Gateway is part of the Capital Compass application. It acts as an aggregator of data between the
User Microservice and the Stock Microservice. Utilizing Keycloak for secure authentication.

## Installation

**Prerequisites:**

- JDK 11
- Gradle 8.4
- Keycloak server setup for authentication
- Access to AWS Parameter Store and AWS Secrets Manager
- Access to all microservices (User and Stock services)
- Running instance of Redis exposing port 6379

**Steps:**

1. Clone the repository: `git clone git@github.com:samuelmlee/capital-compass-gateway.git`
2. Navigate to the project directory: `cd capital-compass-gateway`
3. Build the project: `./gradlew build`
4. Run the application: `./gradlew bootRun`

## Technologies

- **Spring Cloud Gateway**: For routing and API gateway functionalities
- **Java**: Version 11
- **Gradle**: For dependency management and project building
- **Keycloak**: For OAuth2 and OpenID Connect based authentication
- **Redis**: For storing OAuth2 Access Tokens

## Dependencies / Libraries

- **Spring Cloud Gateway**
- **Spring Boot OAuth2 Client **: For integrating with Keycloak as client application
- **Spring Boot OAuth2 Resource Server **: For integrating with Keycloak as resource server

Dependencies are managed through Gradle.

## Configuration

Ensure Keycloak is properly configured with realms, clients, and roles corresponding to the services being accessed.

## Usage

Once the API Gateway is running, it will route requests to the appropriate microservices, providing a unified entry
point for the application.

**API Endpoints:**

Endpoints for the gateway will be listed at `https://localhost:8082/swagger-ui.html`. These
include:

- `GET v1/gateway/watchlists`: Retrieves user watch lists with the latest ticker prices

## Authentication

Most requests to the API Gateway require authentication via Keycloak.

The Spring Cloud Gateway is configured as Spring OAuth2 client for Oauth2 Login, it also acts as Token Relay to the
other micro services.

The Gateway stores OAuth 2 Access Tokens for each user and set the Session Id as Http Only cookie in the browser.

To start the Oauth2 authorization flow, the Frontend should route to {gateway url}/oauth2/authorization/keycloak

Ensure the Frontend is sending credentials before making requests requiring authentication.

## Release History

- **0.0.1** - Initial release: Basic functionality for routing and securing requests between User and Stock services.

