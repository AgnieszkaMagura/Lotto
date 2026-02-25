# Lotto Game Service 🎰

A robust, production-ready backend system for a lottery service built with **Java 17** and **Spring Boot 2.7.8**. This project demonstrates Clean Architecture, Domain-Driven Design (DDD) principles, and an advanced automated testing suite.

## 🚀 Key Features

* **Number Receiver:** Accepts user numbers, validates them (6 numbers in range 1-99), and issues a unique Ticket ID.
* **Number Generator:** Fetches winning numbers from an external HTTP server (simulated via WireMock in tests).
* **Result Checker:** Automatically matches user tickets against winning numbers for specific draw dates every Saturday at 12pm.
* **Result Announcer:** Provides a clear "win/loss" status based on specific Ticket IDs.
* **Scheduled Draws:** Automated logic for determining the next draw date.

## 🏗️ Architecture

The project follows **Hexagonal Architecture** and **Clean Architecture** principles, ensuring a strict separation between the core domain logic and external infrastructure.

![Architecture Diagram](architecture/lotto-architecture-security-v2.png)
<img width="4200" height="3537" alt="lotto-architecture-security-v2" src="https://github.com/user-attachments/assets/16f993ca-fb47-4e9a-9d1a-24386b238c12" />


### 🔐 Security Implementation (JWT)
The application uses **Spring Security** combined with **JSON Web Tokens (JWT)** for robust authentication:
* **Registration**: Password hashing is handled via **BCrypt** through the `LoginAndRegisterFacade`.
* **Authentication**: The `/token` endpoint issues a JWT for authorized access.
* **Authorization**: The `JwtAuthTokenFilter` validates tokens and establishes the security context for protected resources.

### 🌐 API Endpoints

| Module | Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- | :--- |
| **Security** | `POST` | `/register` | **Public** |  Create a new user account. |
| **Security** | `POST` | `/token` | **Public** | Authenticate and retrieve JWT. |
| **Number Receiver** | `POST` | `/inputNumbers` | **Authenticated** | Submit user lottery numbers. |
| **Result Announcer** | `GET` | `/results/{id}` | **Authenticated** | Check draw result by ticket ID. |

### 🧩 Domain Highlights
* **Number Receiver**: Acts as the central "Source of Truth" for ticket draw dates.
* **Winning Numbers Generator**: Fetches **6 unique numbers** from a remote service via `RestTemplate`.
* **Result Checker**: Automatically validates tickets against winning numbers after the draw.

## 🛠️ Technologies & Skills
* * **Testcontainers (MongoDB):** Integration tests run against a real MongoDB instance in a Docker container, providing a production-like environment.
* **WireMock:** Used to mock the external "Remote HTTP Server" for number generation, allowing for reliable testing of external API integrations.
* **Awaitility:** Handles testing of asynchronous processes and schedulers.
* **AssertJ & Mockito:** For expressive, readable assertions and clean unit mocks.

## 🧪 Testing Excellence
This project follows a **Test-Driven Development (TDD)** approach, utilizing a sophisticated testing stack to ensure 100% reliability:

### Core
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7.8-brightgreen?style=for-the-badge&logo=spring-boot)
![Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

### Testing
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-grey?style=for-the-badge)
![Testcontainers](https://img.shields.io/badge/Testcontainers-664B95?style=for-the-badge)
![WireMock](https://img.shields.io/badge/WireMock-orange?style=for-the-badge)
![Awaitility](https://img.shields.io/badge/Awaitility-blue?style=for-the-badge)

* **Core Details:** Java 17, Spring Boot 2.7.8 (Web, Security + JWT, Validation, Data MongoDB, Scheduler)
* **Databases:** MongoDB + MongoExpress, Redis & Jedis (with Redis-Commander)
* **Testing Details:** * **Unit Tests:** JUnit 5, Mockito, AssertJ
    * **Integration Tests:** Testcontainers, WireMock, Awaitility
    * **API Testing:** MockMvc, RestTemplate


## 🧪 Quality Assurance
The application was built with a strong emphasis on code quality:
* **Asynchrony:** Testing scheduled tasks using **Awaitility**.
* **Mocking:** Simulating external job servers with **WireMock**.
* **Real Environment:** Using **Testcontainers** to run integration tests on actual MongoDB instances.

## 📦 Getting Started
1. Clone the repository:
   git clone [https://github.com/AgnieszkaMagura/Lotto.git](https://github.com/AgnieszkaMagura/Lotto.git)

2. Spin up the infrastructure: docker-compose up (requires Docker Desktop).

3. Build and run the app: ./mvnw spring-boot:run or via your IDE.

4. API Documentation is available at: http://localhost:8000/swagger-ui/index.html (port depends on your local configuration).
5. Once the application is running, you can access the interactive Swagger UI at:
👉 http://localhost:8080/swagger-ui/index.html

## 🤝 Contact
**Author:** Agnieszka Magura  
**LinkedIn:** [Agnieszka Magura](https://www.linkedin.com/in/agnieszka-magura-0714241a8/)

If you like this project, please consider giving it a ⭐!
