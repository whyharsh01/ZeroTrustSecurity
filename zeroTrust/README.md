# Zero-Trust Security Framework for Enterprise REST APIs

A secure REST API framework built with Spring Boot that follows Zero-Trust security principles by continuously verifying user identity, permissions, and resource ownership before allowing protected operations.

## Features

* JWT-based stateless authentication
* Spring Security authentication and authorization
* Role-Based Access Control (RBAC)
* Fine-grained permission-based authorization
* Resource-level and ownership-based access control
* BCrypt password hashing
* Redis-based rate limiting
* Database-backed audit logging
* Global exception handling
* Request validation using DTOs
* Swagger/OpenAPI documentation
* Dockerized application with MySQL and Redis

## Architecture

```text
Client
  |
  v
REST API
  |
  v
JWT Validation
  |
  v
Authentication
  |
  v
Role & Permission Check
  |
  v
Resource / Ownership Authorization
  |
  +-----> DENIED -----> Audit Log
  |
  v
Business Logic
  |
  +-----> Redis Rate Limiting
  |
  v
MySQL
```

## Tech Stack

* Java 21
* Spring Boot
* Spring Security
* JWT
* OAuth 2.0 / Resource Server
* Spring Data JPA / Hibernate
* MySQL
* Redis
* Swagger / OpenAPI
* Docker
* JUnit / Mockito

## Security Model

The application follows the principle of **least privilege**.

Every protected request is evaluated through multiple security layers:

1. JWT token validation
2. User identity verification
3. Role and permission verification
4. Resource-level authorization
5. Ownership verification where required
6. Rate-limit verification
7. Audit logging for security-sensitive actions

A valid login alone does not automatically provide access to every resource.

## Authentication

Users authenticate using username and password.

Passwords are never stored in plain text. BCrypt is used for password hashing.

After successful authentication, the server generates a JWT containing information such as:

* Subject / username
* Issuer
* Issued-at time
* Expiration time
* User authorities

The application uses stateless authentication, so server-side HTTP sessions are not required for API authentication.

## Authorization

The application supports fine-grained permissions such as:

```text
USER_READ
USER_CREATE
USER_DELETE
AUDIT_READ
```

Roles are mapped to permissions.

For example:

```text
Role_Admin
    |
    +-- USER_READ
    +-- USER_CREATE
    +-- USER_DELETE
    +-- AUDIT_READ

Role_User
    |
    +-- USER_READ
```

This separates authentication from authorization and allows access to be controlled at the permission level.

## Resource-Level Authorization

The application also verifies whether the authenticated user is allowed to access a specific resource.

For example:

```text
GET /api/users/{id}
```

A user cannot access another user's resource merely because they possess a valid JWT.

If the requested resource does not belong to the authenticated user, the request is rejected with:

```text
403 Forbidden
```

The denied operation is also recorded in the audit log.

## Rate Limiting

Redis is used to implement API rate limiting.

Current configuration:

```text
5 requests / minute / authenticated username
```

Requests exceeding the configured limit return:

```text
429 Too Many Requests
```

Redis provides a fast in-memory counter without requiring every request to query MySQL.

## Audit Logging

Security-sensitive operations are stored in the database.

Each audit record contains information such as:

```text
Username
Action
Resource
Result
Timestamp
```

Example:

```text
username: harshydv2
action: GET_USER
resource: USER:8
result: DENIED
timestamp: ...
```

Both successful and denied resource-access attempts can therefore be traced.

## API Endpoints

### Authentication

```text
POST /auth/login
```

Authenticates a user and returns a JWT.

### User Registration

```text
POST /api/users/register
```

Registers a user with a securely hashed password.

### Protected User Endpoint

```text
GET /api/users/hello
```

Requires a valid JWT and appropriate permission.

### User Resource

```text
GET /api/users/{id}
```

Requires authentication and ownership-based authorization.

### Audit Logs

```text
GET /api/audit
```

Requires the appropriate audit permission.

## Swagger

Interactive API documentation is available through Swagger UI.

When running locally:

```text
http://localhost:8081/swagger-ui/index.html
```

JWT authentication can be tested directly from Swagger using the **Authorize** button.

## Docker

The project uses Docker Compose to run:

```text
Application
MySQL
Redis
```

Start the complete environment:

```bash
docker compose up --build
```

Application:

```text
http://localhost:8081
```

MySQL:

```text
localhost:3307
```

Redis:

```text
localhost:6379
```

Stop the environment:

```bash
docker compose down
```

## Running Without Docker

Configure MySQL and Redis locally and provide the required application properties/environment variables.

Then build the project:

```bash
mvn clean package -DskipTests
```

Run:

```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

## Example Security Flow

```text
Login
  ↓
Username + Password
  ↓
AuthenticationManager
  ↓
UserDetailsService
  ↓
BCrypt Password Verification
  ↓
JWT Generation
  ↓
Client sends JWT
  ↓
JWT Validation
  ↓
Authorities extracted
  ↓
Permission Check
  ↓
Resource Ownership Check
  ↓
Rate Limit Check
  ↓
Allow / Deny
  ↓
Audit Log
```

## Security Principles Demonstrated

This project demonstrates practical implementation of:

* Zero Trust
* Least privilege
* Stateless authentication
* Defense in depth
* RBAC
* Fine-grained authorization
* Resource ownership validation
* Secure password storage
* Rate limiting
* Security auditing

## Testing

The project includes tests for important authentication and JWT components.

Manual integration testing covers:

* User registration
* Login
* JWT authentication
* Protected endpoints
* Unauthorized requests
* Resource ownership checks
* Audit logging
* Redis rate limiting
* Swagger API testing

## Future Improvements

Possible extensions include:

* OAuth2 / OpenID Connect identity provider integration
* Refresh-token rotation
* More granular policy evaluation
* Distributed rate limiting improvements
* Centralized security event processing
* CI/CD pipeline
* Container orchestration
* Additional integration and security tests
