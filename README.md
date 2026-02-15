# Lotto Game Service 🎰

A robust, production-ready backend system for a lottery service built with **Java 17** and **Spring Boot 2.7.8**. This project demonstrates Clean Architecture, Domain-Driven Design (DDD) principles, and an advanced automated testing suite.

## 🚀 Key Features

* **Number Receiver:** Accepts user numbers, validates them (6 numbers in range 1-99), and issues a unique Ticket ID.
* **Number Generator:** Fetches winning numbers from an external HTTP server (simulated via WireMock in tests).
* **Result Checker:** Automatically matches user tickets against winning numbers for specific draw dates.
* **Result Announcer:** Provides a clear "win/loss" status based on specific Ticket IDs.
* **Scheduled Draws:** Automated logic for determining the next draw date.

---

## 🏗 Architecture & Design

The project follows a **Modular Monolith** pattern with a strong emphasis on the **Facade Pattern**. Each module is self-contained, ensuring that business logic is decoupled from infrastructure and other domains.

```mermaid
graph TD
    User((User))

    subgraph "Lotto System (Spring Boot)"
        NRF[NumberReceiverFacade]
        NGF[NumberGeneratorFacade]
        RCF[ResultCheckerFacade]
        RAF[ResultAnnouncerFacade]
        
        %% Scheduler component
        SCH[[ResultCheckerScheduler]]
        
        DB[(MongoDB)]
    end

    subgraph "External Services"
        RHS[Remote HTTP Server]
    end

    %% User interactions
    User -->|"1. POST /inputNumbers"| NRF
    NRF -->|"Save Ticket"| DB
    
    %% Scheduled process
    SCH -->|"Trigger every Saturday"| NGF
    SCH -->|"Generate Results"| RCF
    
    %% Internal flow & External API
    NGF -->|"2. Fetch Winning Numbers"| RHS
    NGF -->|"Save Winning Numbers"| DB
    
    RCF -->|"3. Match Tickets"| NRF
    RCF -->|"3. Match Tickets"| NGF
    RCF -->|"Save Computed Result"| DB
    
    User -->|"4. GET /results/{id}"| RAF
    RAF -->|"Fetch Final Result"| RCF
```


### Domain Modules:
1.  **`number-receiver`**: Handles input validation and ticket persistence.
2.  **`number-generator`**: Manages the generation and retrieval of winning numbers.
3.  **`result-checker`**: Orchestrates the comparison logic between tickets and results.
4.  **`result-announcer`**: Handles the presentation and caching of results for the user.

---

## 🛠 Tech Stack

* **Core:** Java 17, Spring Boot 2.7.8
* **Database:** MongoDB (via Spring Data MongoDB)
* **Documentation:** Swagger / Springfox 3.0.0
* **Utilities:** Lombok, Spring Validation, Awaitility
* **Build Tool:** Maven

---

## 🧪 Testing Excellence

This project follows a **Test-Driven Development (TDD)** approach, utilizing a sophisticated testing stack to ensure 100% reliability:

* **Testcontainers (MongoDB):** Integration tests run against a real MongoDB instance in a Docker container, providing a production-like environment.
* **WireMock:** Used to mock the external "Remote HTTP Server" for number generation, allowing for reliable testing of external API integrations.
* **Awaitility:** Handles testing of asynchronous processes and schedulers.
* **AssertJ & Mockito:** For expressive, readable assertions and clean unit mocks.
## 🛠️ Technologies & Skills

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

## 📡 API Endpoints
Once the application is running, you can access the interactive Swagger UI at:
👉 http://localhost:8080/swagger-ui/index.html

| Method | Endpoint | Description | 
| :--- | :--- | :--- | 
| `POST` | `/inputNumbers` | Submit your 6 numbers. Returns a Ticket ID. |
| `GET` | `/results/{id}` | Check if your ticket won. |

## 📦 Getting Started

1. Clone the repository:
   git clone [https://github.com/AgnieszkaMagura/Lotto.git](https://github.com/AgnieszkaMagura/Lotto.git)

2. Spin up the infrastructure: docker-compose up (requires Docker Desktop).

3. Build and run the app: ./mvnw spring-boot:run or via your IDE.

4. API Documentation is available at: http://localhost:8000/swagger-ui/index.html (port depends on your local configuration).

## 🤝 Contact
**Author:** Agnieszka Magura  
**LinkedIn:** [Agnieszka Magura](https://www.linkedin.com/in/agnieszka-magura-0714241a8/)

If you like this project, please consider giving it a ⭐!
