# Authentication & OTP System

A robust full-stack application demonstrating secure user authentication and Email OTP verification. Built with **Spring Boot** (Backend) and **React 19** (Frontend).

## 🚀 Built With

### Frontend
*   **React 19** - The library for web and native user interfaces
*   **Vite** - Next Generation Frontend Tooling
*   **Bootstrap 5** - Powerful, extensible, and feature-packed frontend toolkit
*   **Axios** - Promise based HTTP client for the browser and node.js
*   **React Router Dom** - Declarative routing for React web applications
*   **React Toastify** - React notifications made easy

### Backend
*   **Spring Boot 3.5** - Framework for building production-ready Java applications
*   **Spring Security** - Authentication and access-control framework
*   **JWT (JSON Web Tokens)** - Stateless authentication mechanism
*   **Spring Data JPA** - Persistence API with Hibernate
*   **PostgreSQL** - Open Source Relational Database
*   **Java Mail Sender** - For sending OTP functionality

## 📋 Prerequisites

Before you begin, ensure you have the following installed:
*   **Java Development Kit (JDK) 21**
*   **Node.js** (Latest LTS version recommended)
*   **PostgreSQL** Database

## 🛠️ Getting Started

Follow these steps to set up the project locally.

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/your-repo-name.git
cd back_and_front_end_auth_otp
```

### 2. Backend Setup
1.  Navigate to the backend directory:
    ```bash
    cd back-end
    ```
2.  Configure your database and email settings in `src/main/resources/application.properties` or `application.yml`:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/your_db_name
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password

    # Mail Configuration
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=your_email@gmail.com
    spring.mail.password=your_app_password
    ```
3.  Run the application:
    ```bash
    ./gradlew bootRun
    ```

### 3. Frontend Setup
1.  Open a new terminal and navigate to the frontend directory:
    ```bash
    cd front-end
    ```
2.  Install dependencies:
    ```bash
    npm install
    ```
3.  Start the development server:
    ```bash
    npm run dev
    ```

## ✨ Features

*   **User Registration**: Secure account creation with validation.
*   **Login**: Traditional username/password login.
*   **OAuth2 Login**: Sign in with third-party providers.
*   **OTP Verification**: Email-based One-Time Password for enhanced security using Java Mail Sender.
*   **JWT Application**: Secure, stateless session management.
*   **Responsive UI**: Clean and responsive interface using Bootstrap.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
