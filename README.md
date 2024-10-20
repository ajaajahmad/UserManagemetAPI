# User Management API

This API provides CRUD functionality for managing users, including user registration, login, and updates. It ensures secure handling of user data by validating unique emails and usernames. Below is a detailed overview of the available endpoints, including their URLs, methods, descriptions, and request/response formats.

## API Endpoints Overview

| Endpoint                              | Method | Description                                                        |
|---------------------------------------|--------|--------------------------------------------------------------------|
| `/api/register`                       | POST   | Registers a new user, ensuring the email and username are unique.  |
| `/api/login`                          | POST   | Logs in an existing user by verifying the username and password.   |
| `/api/users`                          | GET    | Retrieves a list of all users that are not soft-deleted.           |
| `/api/users/{id}`                     | GET    | Retrieves a user by their ID, provided they are not soft-deleted.  |
| `/api/users/{id}`                     | PUT    | Updates the details of a user by their ID.                         |
| `/api/users/{id}`                     | DELETE | Soft-deletes a user by setting a `deletedAt` timestamp.            |
| `/api/users/status/{status}`          | GET    | Retrieves a list of users based on their status (e.g., `ACTIVE`).  |

## Detailed API Documentation

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

---

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

---

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

---

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
- **Error Response** (404):
    ```json
    {
      "message": "User not found"
    }
    ```

---

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
- **Error Response** (403):
    ```json
    {
      "message": "Updation is not allowed for this user."
    }
    ```

---

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

---

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

---

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
