# 🎰 Lotto Project - Backend Service
A robust, production-ready backend system for a lottery service built with **Java 17** and **Spring Boot**. This project demonstrates **Modular Monolith** design, **Hexagonal Architecture**, and advanced automated testing suites.

## 🚀 Project Overview
This repository contains the core business logic and infrastructure for the Lotto application. It handles user registration, secure ticket purchases, automated draws via schedulers, and real-time result verification.
The system is designed with a focus on **security**, **scalability**, and **clean code**, ensuring that the entire process from ticket purchase to result announcement is fully automated and protected.
* **Frontend Repository:** https://github.com/AgnieszkaMagura/lotto-frontend
  
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

## 🏗️ Architecture & Security

The system is designed with a focus on Clean Architecture principles, ensuring that the domain logic remains isolated from external frameworks and databases.
<img width="6557" height="6623" alt="lotto architecture security v2" src="https://github.com/user-attachments/assets/8fe62ad8-c962-430e-abea-5ad17b7d40d4" />

## 🧩 Modular Monolith & Facades
The application follows a **Modular Monolith** pattern. Each business capability (e.g., Number Receiver, Result Checker) is an independent module. Communication between modules happens strictly through **Facades**, which encapsulate internal logic and provide a clean API.

## 🔐 Security Implementation (JWT)
The application uses **Spring Security** combined with **JSON Web Tokens (JWT)** for robust authentication:
* **Registration**: Password hashing is handled via **BCrypt** through the `LoginAndRegisterFacade`.
* **Authentication**: The `/token` endpoint issues a JWT for authorized access.
* **Authorization**: The `JwtAuthTokenFilter` validates tokens and establishes the security context for protected resources.

## 🟢 Domain Logic & Ports (Green)
* **Number Receiver:** Validates user numbers (6 in range 1-99) and generates unique Ticket IDs.

* **Winning Generator:** Fetches winning numbers from an external service via a **Remote HTTP Clien.**

* **Result Checker:** Automatically matches tickets against winning numbers for Saturday draws.

* **Result Announcer:** Handles the logic of informing users about their winnings.

## 🔵 Infrastructure & Adapters (Blue)
* **MongoDB:** Primary storage for user accounts and ticket data.

* **Redis:** High-performance cache used to store draw results and optimize response times.

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
| Category | Technologies |
| :--- | :--- |
| **Core** | Java 17, Spring Boot, Spring Security, JWT, Maven |
| **Database** | MongoDB, Redis |
| **Infrastructure** | Docker, Spring Task (Scheduler) |
| **Testing** | JUnit 5, Mockito, Testcontainers, WireMock, Awaitility |

## 🧪 Testing Excellence
This project was built with a strong emphasis on **Quality Assurance:**
* **Testcontainers:** Integration tests run against real **MongoDB** instances in Docker containers.
* **WireMock:** Simulates the "Remote HTTP Server" for winning number generation to test external API reliability.
* **Awaitility:** Used to test asynchronous scheduled tasks and verify they complete as expected.

## 📦 Getting Started
1. Clone the repo: git clone https://github.com/AgnieszkaMagura/Lotto.git
2. **Start Infrastructure:** docker-compose up -d (requires Docker Desktop).
3. **Run Application:** ./mvnw spring-boot:run or via your IDE.
4. **Swagger UI:** Access interactive documentation at http://localhost:8080/swagger-ui/index.html.
---

<div align="center">
  <strong>🛠️ Backend Tech Stack</strong><br>
  <img src="https://img.shields.io/badge/Architecture-Hexagonal-3498db?style=for-the-badge&logo=architecture" alt="Hexagonal Architecture">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Lombok-bc473a?style=for-the-badge&logo=java&logoColor=white" alt="Lombok">
  <img src="https://img.shields.io/badge/Bean-Validation-009688?style=for-the-badge" alt="Bean Validation">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security">
  <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=json-web-tokens&logoColor=white" alt="JWT">
  <br>
  <img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
</div>

<br>

<div align="center">
  <strong>💻 Frontend Stack</strong><br>
  <img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB" alt="React">
  <img src="https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white" alt="TypeScript">
  <img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white" alt="Axios">
  <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" alt="CSS3">
</div>

<br>

<div align="center">
  <strong>🧪 Testing Tools</strong><br>
  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit5">
  <img src="https://img.shields.io/badge/Mockito-ff9c1e?style=for-the-badge" alt="Mockito">
  <img src="https://img.shields.io/badge/Testcontainers-61696e?style=for-the-badge&logo=testcontainers&logoColor=white" alt="Testcontainers">
  <img src="https://img.shields.io/badge/Wiremock-000000?style=for-the-badge&logo=wiremock&logoColor=white" alt="Wiremock">
  <img src="https://img.shields.io/badge/Awaitility-3498db?style=for-the-badge" alt="Awaitility">
</div>

<br>

<div align="center">
  <h3>🤝 Contact</h3>
  <em>Designed with ❤️ by <strong>Agnieszka Magura</strong></em><br>
  <a href="https://github.com/AgnieszkaMagura" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub">
  </a>
  <a href="https://www.linkedin.com/in/agnieszka-magura-0714241a8/" target="_blank">
    <img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="LinkedIn">
  </a>
  <br><br>
  If you like this project, please consider giving it a ⭐!
</div>
