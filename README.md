# Lotto Project 🎰
📝 Project Overview

A robust, production ready backend system for a lottery service built with **Java 17** and **Spring Boot 2.7.8**. This project demonstrates Clean Hexagonal Architecture, Domain-Driven Design (DDD) principles, and an advanced automated testing suite.


**Lotto Project** is a modern, microservice-ready application based on a **Coupon Draw System**. It allows users to participate in a lottery by submitting their numbers and checking results after a scheduled draw.

The system is designed with a focus on **security**, **scalability**, and **clean code**, ensuring that the entire process from ticket purchase to result announcement.It is fully automated and protected.

⚙️ How it works:
1. **Ticket Submission**: Users send their 6 numbers to the system. Each ticket is assigned a unique ID and saved in the database.

2. **Automated Draw**: Every Saturday at 12:00 PM (configurable), the **Spring Scheduler** triggers the draw process.

3. **External Integration**: The application fetches winning numbers from an external generator service using WireMock for reliable integration testing.

4. **Result Verification**: The system automatically compares user tickets with winning numbers using a dedicated **Result Checker** module.

5. **Security**: All sensitive operations and result checking are secured with **JWT tokens**, ensuring that only authorized users can access their data.
   
### Core
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7.8-brightgreen?style=for-the-badge&logo=spring-boot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-blue?style=for-the-badge)
![Maven](https://img.shields.io/badge/Apache_Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Scheduler](https://img.shields.io/badge/Scheduler-Spring%20Task-brightgreen?style=for-the-badge&logo=spring)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)

### Testing
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-grey?style=for-the-badge)
![Testcontainers](https://img.shields.io/badge/Testcontainers-664B95?style=for-the-badge)
![WireMock](https://img.shields.io/badge/WireMock-orange?style=for-the-badge)
![Awaitility](https://img.shields.io/badge/Awaitility-blue?style=for-the-badge)

### 🛠️ Core Technologies & Concepts

* **Spring Boot 2.7.8 & Java 17**: Core framework for building the microservice.
* **Spring Security & JWT**: Stateless authentication and authorization.
* **Hexagonal Architecture**: Ensuring high maintainability by separating domain logic from infrastructure.
* **Redis**: Used as a fast, in-memory cache to optimize performance and store temporary data.
* **Spring Task (Scheduler)**: 🕒 Automates winning numbers generation and result verification.
* **MongoDB**: NoSQL database chosen for its flexibility in storing lottery tickets and results.
* **Testcontainers**: Used for integration testing with a real database environment to ensure reliability.

🧪 Quality Assurance & Testing
* **Unit & Integration Tests**: Comprehensive test suite ensuring the reliability of business logic.
* **MockMvc**: Used for lightweight testing of REST controllers without starting a full HTTP server.
* **WireMock**: Simulates external APIs (Winning Numbers Provider) to test HTTP communication reliability.
* **Awaitility**: Used for testing asynchronous operations and schedulers to ensure tasks complete as expected.
* **RestTemplate**: Handles synchronous HTTP communication with external microservices.

## 🚀 Key Features

* **Number Receiver:** Accepts user numbers, validates them (6 numbers in range 1-99), and issues a unique Ticket ID.
* **Number Generator:** Fetches winning numbers from an external HTTP server (simulated via WireMock in tests).
* **Result Checker:** Automatically matches user tickets against winning numbers for specific draw dates every Saturday at 12:00.
* **Result Announcer:** Provides a clear "win/loss" status based on specific Ticket IDs.
* **Scheduled Draws:** Automated logic for determining the next draw date.

## 🏗️ Architecture

The project follows **Hexagonal Architecture** and **Clean Architecture** principles, ensuring a strict separation between the core domain logic and external infrastructure.

![Architecture Diagram](architecture/lotto-architecture-security-v2.png)

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
* **Testcontainers (MongoDB):** Integration tests run against a real MongoDB instance in a Docker container, providing a production-like environment.
* **WireMock:** Used to mock the external "Remote HTTP Server" for number generation, allowing for reliable testing of external API integrations.
* **Awaitility:** Handles testing of asynchronous processes and schedulers.
* **AssertJ & Mockito:** For expressive, readable assertions and clean unit mocks.

## 🧪 Testing Excellence and Quality Assurance 
This project follows a **Test-Driven Development (TDD)** approach, utilizing a sophisticated testing stack to ensure 100% reliability:
The application was built with a strong emphasis on code quality:
* **Asynchrony:** Testing scheduled tasks using **Awaitility**.
* **Mocking:** Simulating external job servers with **WireMock**.
* **Real Environment:** Using **Testcontainers** to run integration tests on actual MongoDB instances.

## 📦 Getting Started
1. Clone the repository:
   git clone [https://github.com/AgnieszkaMagura/Lotto.git](https://github.com/AgnieszkaMagura/Lotto.git)

2. Spin up the infrastructure: docker-compose up (requires Docker Desktop).

3. Build and run the app: ./mvnw spring-boot:run or via your IDE.
 
4. Once the application is running, you can access the interactive Swagger UI at:
👉 http://localhost:8080/swagger-ui/index.html

## 🤝 Contact
**Author:** Agnieszka Magura  
**LinkedIn:** [Agnieszka Magura](https://www.linkedin.com/in/agnieszka-magura-0714241a8/)

If you like this project, please consider giving it a ⭐!
