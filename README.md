# 🎰 Lotto Project - Backend Service
A robust, production-ready backend system for a lottery service built with **Java 17** and **Spring Boot**. This project demonstrates **Modular Monolith** design, **Hexagonal Architecture**, and advanced automated testing suites.

## 🚀 Project Overview
This repository contains the core business logic and infrastructure for the Lotto application. It handles user registration, secure ticket purchases, automated draws via schedulers, and real-time result verification.

The system is designed with a focus on **security**, **scalability**, and **clean code**, ensuring that the entire process from ticket purchase to result announcement is fully automated and protected.

## 💡 Key Challenges Solved

### 🏗️ Enterprise-Grade Architecture
Implemented **Hexagonal Architecture (Ports and Adapters)** to decouple core business logic from external infrastructures like MongoDB, Redis, and external APIs. This ensures high maintainability and makes the system easily adaptable to future changes.

### 🧪 Advanced Integration Testing
Instead of relying solely on mocks, I used **Testcontainers** to spin up real **Docker** containers for **MongoDB** and **Redis** during integration tests. Combined with **WireMock** to simulate external winning numbers providers, this guarantees 100% environment consistency between testing and production.

### ☁️ Production-Ready Cloud Deployment
Configured a full-stack environment on **AWS (EC2)** using **Docker Compose**. This included managing networking between containers (Frontend, Backend, Database, Cache) and ensuring secure communication through **JWT-based authentication**.

### ⏱️ Reliable Automated Processes
Used **Spring Scheduler** to automate weekly draw cycles, ensuring the system independently handles data fetching, result verification, and ticket expiration without manual intervention.

## 🔗 Quick Links
* **🌐 Live Demo:** [View Application](http://ec2-3-124-216-135.eu-central-1.compute.amazonaws.com/)
* **🖥️ Frontend Repository:** [View Code](https://github.com/AgnieszkaMagura/lotto-frontend)
* **🔑 Interactive API Docs (Swagger):** [Explore API](http://ec2-3-124-216-135.eu-central-1.compute.amazonaws.com:8000/swagger-ui/index.html#/)
  
## ⚙️ How it works:
1. **Ticket Submission**: Users send their 6 numbers to the system. Each ticket is assigned a unique ID and saved in the database.

2. **Automated Draw**: Every Saturday at 12:00 PM (configurable), the **Spring Scheduler** triggers the draw process.

3. **External Integration**: The application fetches winning numbers from an external generator service using WireMock for reliable integration testing.

4. **Result Verification**: The system automatically compares user tickets with winning numbers using a dedicated **Result Checker** module.

5. **Security**: All sensitive operations and result checking are secured with **JWT tokens**, ensuring that only authorized users can access their data.

## 🏗️ Architecture & Security

The system is designed with a focus on Clean Architecture principles, ensuring that the domain logic remains isolated from external frameworks and databases.

<img width="850" alt="lotto architecture security v2" src="https://github.com/user-attachments/assets/8fe62ad8-c962-430e-abea-5ad17b7d40d4" />

## 🧩 Modular Monolith & Facades
The application follows a **Modular Monolith** pattern. Each business capability (e.g., Number Receiver, Result Checker) is an independent module. Communication between modules happens strictly through **Facades**, which encapsulate internal logic and provide a clean API.

## 🔐 Security Implementation (JWT)
The application uses **Spring Security** combined with **JSON Web Tokens (JWT)** for robust authentication:
* **Registration**: Password hashing is handled via **BCrypt** through the `LoginAndRegisterFacade`.
* **Authentication**: The `/token` endpoint issues a JWT for authorized access.
* **Authorization**: The `JwtAuthTokenFilter` validates tokens and establishes the security context for protected resources.

## 🟢 Domain Logic & Ports (Green)
* **Number Receiver:** Validates user numbers (6 in range 1-99) and generates unique Ticket IDs.

* **Winning Generator:** Fetches winning numbers from an external service via a **Remote HTTP Clien**.

* **Result Checker:** Automatically matches tickets against winning numbers for Saturday draws.

* **Result Announcer:** Handles the logic of informing users about their winnings.

## 🔵 Infrastructure & Adapters (Blue)
* **MongoDB:** Primary storage for user accounts and ticket data.

* **Redis:** High-performance cache used to store draw results and optimize response times.

## 🌐 API Endpoints
| Module | Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- | :--- |
| **Security** | `POST` | `/register` | **Public** |  Create a new user account. |
| **Security** | `POST` | `/token` | **Public** | Authenticate and retrieve JWT. |
| **Number Receiver** | `POST` | `/inputNumbers` | **Authenticated** | Submit user lottery numbers. |
| **Result Announcer** | `GET` | `/results/{id}` | **Authenticated** | Check draw result by ticket ID. |

## 🧩 Domain Highlights
* **Number Receiver**: Acts as the central "Source of Truth" for ticket draw dates.
* **Winning Numbers Generator**: Fetches **6 unique numbers** from a remote service via `RestTemplate`.
* **Result Checker**: Automatically validates tickets against winning numbers after the draw.

## 🧪 Testing Excellence
This project was built with a strong emphasis on **Quality Assurance:**
* **Testcontainers:** Integration tests run against real **MongoDB** instances in Docker containers.
* **WireMock:** Simulates the "Remote HTTP Server" for winning number generation to test external API reliability.
* **Awaitility:** Used to test asynchronous scheduled tasks and verify they complete as expected.

## 📦 Getting Started

To run the project locally and explore the API, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AgnieszkaMagura/Lotto.git
   cd Lotto
   ```
2. **Start Infrastructure (Database & Cache):**
   
   *Ensure **Docker Desktop** is running, then execute:*
   ```bash
   docker-compose up -d
   ```
4. **Run Application:**
   Run the following command in your terminal:
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Explore the API:**
   Once the application is running, the best way to test the endpoints is through the **Swagger UI**:
   * **Local Environment:** [💻 Local Environment](http://localhost:8080/swagger-ui/index.html)
   * **Production (AWS):** [🚀 Click here to test on AWS](http://ec2-3-124-216-135.eu-central-1.compute.amazonaws.com:8000/swagger-ui/index.html#/)
  
## 💡 How to test quickly:
1. Go to **Swagger (AWS)**.
2. Use `POST /register` to create an account.
3. Use `POST /token` to get your JWT.
4. Click **Authorize** at the top and paste the token.
5. Now you can play! Send your numbers via `POST /inputNumbers`.   
---

<div align="center">
  <strong>🛠️ Backend Tech Stack</strong><br>
  <img src="https://img.shields.io/badge/Architecture-Hexagonal-3498db?style=for-the-badge&logo=architecture" alt="Hexagonal Architecture">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Spring_Scheduler-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Scheduler">
  <img src="https://img.shields.io/badge/RestTemplate-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="RestTemplate">
  <img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven">
  <img src="https://img.shields.io/badge/Lombok-bc473a?style=for-the-badge&logo=java&logoColor=white" alt="Lombok">
  <img src="https://img.shields.io/badge/Bean-Validation-009688?style=for-the-badge" alt="Bean Validation">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white" alt="Spring Security">
  <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=json-web-tokens&logoColor=white" alt="JWT">
  <br>
  <img src="https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS">
  <img src="https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white" alt="EC2">
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black" alt="Swagger">
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
  <img src="https://img.shields.io/badge/Log4j2-D22128?style=for-the-badge&logo=apache&logoColor=white" alt="Log4j2">
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
