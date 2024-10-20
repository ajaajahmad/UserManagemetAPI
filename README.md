# User Management API

This API provides CRUD functionality for managing users, including user registration, login, and updates. It ensures secure handling of user data by validating unique emails and usernames.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21**: [Download JDK 21](https://www.oracle.com/java/technologies/javase-jdk21-downloads.html)
- **Maven**: [Download Maven](https://maven.apache.org/download.cgi)
- **MySQL**: [Download MySQL](https://dev.mysql.com/downloads/mysql/)
- **Git**: [Download Git](https://git-scm.com/downloads)

## Setup Guide

### Step 1: Clone the Repository

Clone the project from the public GitHub repository.

```bash
git clone https://github.com/yourusername/UserManagementAPI.git
cd UserManagementAPI
```

### Step 2: Set Up the MySQL Database

Create a new MySQL database for the project.

```sql
CREATE DATABASE user_management_db;
```

Make sure you have a MySQL user with the necessary privileges to access the `user_management_db` database.

### Step 3: Configure Application Properties

Update the MySQL credentials in the `application.properties` file located at `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_management_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
```

### Step 4: Run Database Migrations

The project uses Liquibase for database schema management. Run the following command to apply migrations to your database:

```bash
mvn liquibase:update
```

### Step 5: Build the Project

Use Maven to clean and build the project.

```bash
mvn clean install
```

### Step 6: Run the Application

After building, run the Spring Boot application using the following command:

```bash
java -jar target/api-v1.0.0.jar
```

### Step 7: Access the API

Once the application is running, you can access the API at:

```arduino
http://localhost:8080
```

## API Endpoints Overview

| Endpoint                    | Method | Description                                                        |
|-----------------------------|--------|--------------------------------------------------------------------|
| `/api/register`             | POST   | Registers a new user, ensuring the email and username are unique.  |
| `/api/login`                | POST   | Logs in an existing user by verifying the username and password.   |
| `/api/users`                | GET    | Retrieves a list of all users that are not soft-deleted.           |
| `/api/users/{id}`           | GET    | Retrieves a user by their ID, provided they are not soft-deleted.  |
| `/api/users/{id}`           | PUT    | Updates the details of a user by their ID.                         |
| `/api/users/{id}`           | DELETE | Soft-deletes a user by setting a `deletedAt` timestamp.              |
| `/api/users/status/{status}`| GET    | Retrieves a list of users based on their status (e.g., `ACTIVE`).    |

## API Documentation

### 1. Create a User

- **URL**: `/api/register`
- **Method**: `POST`
- **Request Body**:

```json
{
    "name": "John Doe",
    "username": "john_doe",
    "email": "john.doe@example.com",
    "password": "SecurePass1!"
}
```

- **Description**: Registers a new user, ensuring that the email and username are unique.
- **Response**:

```json
{
    "id": 1,
    "name": "John Doe",
    "username": "john_doe",
    "email": "john.doe@example.com",
    "password": "$2a$10$DLMsPDFRU449lVn5aZgFoe3aTz1jd1VCeqx6Y3YftgEfNPYsD.LzG",
    "status": "ACTIVE",
    "createdAt": "2024-10-07T00:27:45.6304268",
    "updatedAt": "2024-10-07T00:27:45.6324219",
    "deletedAt": null
}
```

### 2. Login a User

- **URL**: `/api/login`
- **Method**: `POST`
- **Request Body**:

```json
{
    "username": "john_doe",
    "password": "SecurePass1!"
}
```

- **Description**: Logs in an existing user by verifying the username and password.
- **Response**:

```json
{
    "message": "Login successful",
    "token": "jwt_token_value"
}
```

### 3. Get All Users

- **URL**: `/api/users`
- **Method**: `GET`
- **Description**: Retrieves a list of all users that are not soft-deleted.
- **Response**:

```json
[
    {
        "id": 1,
        "name": "John Doe",
        "username": "john_doe",
        "email": "john.doe@example.com",
        "status": "ACTIVE"
    },
    {
        "id": 2,
        "name": "Jane Smith",
        "username": "jane_smith",
        "email": "jane.smith@example.com",
        "status": "INACTIVE"
    }
]
```

### 4. Get a User by ID

- **URL**: `/api/users/{id}`
- **Method**: `GET`
- **Path Parameter**: `id` - The ID of the user.
- **Description**: Retrieves a user by their ID, provided they are not soft-deleted.
- **Response**:

```json
{
    "id": 1,
    "name": "John Doe",
    "username": "john_doe",
    "email": "john.doe@example.com",
    "status": "ACTIVE"
}
```

- **Error Response (404)**:

```json
{
    "message": "User not found"
}
```

### 5. Update a User by ID

- **URL**: `/api/users/{id}`
- **Method**: `PUT`
- **Request Body**:

```json
{
    "name": "John Updated",
    "username": "john_updated",
    "email": "john.updated@example.com",
    "password": "NewPass1!"
}
```

- **Path Parameter**: `id` - The ID of the user.
- **Description**: Updates the details of a user by their ID.
- **Error Response (403)**:

```json
{
    "message": "Updation is not allowed for this user."
}
```

### 6. Soft-Delete a User by ID

- **URL**: `/api/users/{id}`
- **Method**: `DELETE`
- **Path Parameter**: `id` - The ID of the user.
- **Description**: Soft-deletes a user by setting a `deletedAt` timestamp instead of removing them from the database.
- **Response**:

```json
{
    "message": "User soft-deleted successfully."
}
```

### 7. Get Users by Status

- **URL**: `/api/users/status/{status}`
- **Method**: `GET`
- **Path Parameter**: `status` - The status of the users (e.g., `ACTIVE`, `INACTIVE`).
- **Description**: Retrieves a list of users based on their status.
- **Response**:

```json
[
    {
        "id": 1,
        "name": "John Doe",
        "username": "john_doe",
        "email": "john.doe@example.com",
        "status": "ACTIVE"
    }
]
```

## Error Responses

### 1. Duplicate User Error (409 Conflict)

When attempting to register or update a user with a duplicate email or username:

```json
{
    "message": "Username or email already exists."
}
```

### 2. Not Found Response (404) for Getting User by ID

```json
{
    "message": "User not found"
}
```

## Troubleshooting

### 1. Port Conflict

If port 8080 is already in use, you can change the port in `application.properties`:

```properties
server.port=8081
```

### 2. Database Connectivity Issues

If you face issues connecting to MySQL, ensure that MySQL is running and the credentials in `application.properties` are correct.

### 3. Log Configuration

This project uses Log4j2 for logging. Log files will be generated in the `/logs` folder.

## Changelog

For a detailed changelog, see our [Wiki Changelog](https://github.com/ajaajahmad/UserManagemetAPI/wiki/Changelog).