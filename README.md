# E-store Java Demo

## ℹ️ Overview

**E-store** is a legacy Java-based e-commerce application developed to demonstrate **service-oriented architecture (SOA)**, layered backend design, and full-stack web application concepts.

The project includes both:

- A server-side API
- A traditional MVC web client

While the technology stack reflects the era in which it was built, the project demonstrates foundational concepts that remain relevant today, including separation of concerns, facade patterns, data access layers, and testability.

## 📦 Architectural Overview

The application follows a classic layered architecture:

```
MVC Client
   ↓
API Controllers (Spring MVC)
   ↓
Service Facade Layer
   ↓
DAO Layer
   ↓
MySQL Database
```

### Key Architectural Concepts

- **Service-Oriented Architecture (SOA)**
- **Facade Pattern** for encapsulating business logic
- **DAO Pattern** for persistence abstraction
- **Dependency Injection** for testability
- Clear separation between client, API, services, and persistence layers

## 📁 Project Structure

| Package / Folder | Description |
|------------------|-------------|
| `db` | SQL scripts for creating MySQL schemas and tables |
| `com.estore.entity` | JPA entity classes mapped via Hibernate |
| `com.estore.dto` | DTOs extending Spring `RepresentationModel` for HATEOAS support |
| `com.estore.dao` | DAO classes using Hibernate `SessionFactory`; includes a generic base DAO |
| `com.estore.service` | Service facade layer encapsulating business logic and workflows |
| `com.estore.controller.api` | REST-style API controllers using Spring MVC |
| `com.estore.controller.client` | MVC controllers for the server-rendered client |
| `resources` | Client-side JS/CSS assets and Thymeleaf templates |
| `test/java` | Unit and integration tests |

## 🛠 Technology Stack

### Server-Side

- Java
- Spring MVC (non–Spring Boot)
- Spring Security
- Hibernate (JPA implementation)
- Thymeleaf (server-side templating)
- JUnit
- Mockito

### Client-Side

- npm
- Webpack
- JavaScript (ES5/ES6)
- jQuery
- Axios
- Bootstrap + Bootswatch
- Babel
- ESLint

### Infrastructure

- AWS Elastic Beanstalk
- AWS RDS (MySQL)

## 🔄 Testing Strategy

### Unit Tests

- Focused on application and service-layer logic
- DAO dependencies mocked using Mockito
- Dependency injection via `javax.inject` to enable isolation

### Integration Tests

- Validate CRUD operations against the database
- Ensure correct interaction between DAOs and persistence layer

### Manual Testing

- API endpoints tested using Postman

## ⚙️ Caching

Server-side caching is implemented using Spring's caching abstraction with `@Cacheable`.

Benefits include:

- Reduced database load
- Improved response times
- Flexibility to adopt advanced cache providers (e.g., Redis)

## ❗ Exception Handling

Custom exception classes (e.g. `ProductNotFoundException`) are used to:

- Provide descriptive error responses
- Include contextual data such as entity IDs
- Centralize error handling and logging

## 📄 HATEOAS

HATEOAS support is implemented using Spring's APIs.

- Link generation occurs in DTO classes
- Keeps controllers and services focused on behavior
- Encourages discoverable APIs

## 🔒 Security

- OAuth2-based authentication
- Social login providers (e.g., Google)
- Auth0 for authentication and authorization
- Spring Security for request handling and access control

## 🚩 Legacy Context

This project reflects:

- Pre–Spring Boot conventions
- Server-rendered MVC patterns
- Earlier frontend tooling choices

It is retained as a **historical and educational artifact** demonstrating:

- Full-stack system design
- Enterprise-style Java architecture
- Early exposure to security, caching, and testing practices

## 📄 License

MIT
