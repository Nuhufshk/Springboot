# IdeoHub API Documentation

This document provides a detailed overview of the IdeoHub API endpoints.

## Base URL
All endpoints are relative to the base URL of the running application (e.g., `http://localhost:8080`).

## Authentication
Most endpoints require a JSON Web Token (JWT) for authentication. To authenticate, send a `POST` request to the `/login` endpoint with a valid email and password. The server will return a JWT.

Include this token in the `Authorization` header for all protected endpoints:
`Authorization: Bearer <your_jwt_token>`

## General Response Format
The API generally returns responses in the following JSON format:

```json
{
  "status": true,
  "message": "Descriptive message",
  "data": { ... }
}
```
- `status`: `true` for success, `false` for an error.
- `message`: A description of the result.
- `data`: The payload of the response. For errors, this is often `null`.

---

# User Management

### 1. Register a New User
- **Endpoint:** `POST /register`
- **Description:** Creates a new user account.
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "User registered successfully",
    "data": {
      "id": "user_id",
      "email": "user@example.com",
      "createdAt": "2025-07-21T10:00:00.000Z",
      "updatedAt": null
    }
  }
  ```
- **Error Responses:**
  - **400 Bad Request:** If the email is invalid, the password is too short, or the email already exists.
    ```json
    {
      "status": false,
      "message": "Email already exists",
      "data": null
    }
    ```

### 2. Login
- **Endpoint:** `POST /login`
- **Description:** Authenticates a user and returns a JWT.
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "password123"
  }
  ```
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "User authenticated successfully",
    "data": {
      "jwt": "your_jwt_token",
      "userId": "user_id",
      "email": "user@example.com"
    }
  }
  ```
- **Error Responses:**
  - **400 Bad Request:** If credentials are incorrect.
  - **404 Not Found:** If the email does not exist.

### 3. Reset Password
- **Endpoint:** `POST /reset-password`
- **Description:** Resets the password for a user.
- **Request Body:**
  ```json
  {
    "email": "user@example.com",
    "password": "new_password123"
  }
  ```
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Password reset successfully",
    "data": "success"
  }
  ```
- **Error Responses:**
  - **404 Not Found:** If the email does not exist.

### 4. Logout
- **Endpoint:** `GET /logout`
- **Description:** Logs the user out. (Note: JWT is stateless, so this endpoint is for client-side token removal).
- **Headers:** `Authorization: Bearer <your_jwt_token>`
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "User logged out successfully",
    "data": "Logout successful"
  }
  ```
- **Error Responses:**
  - **400 Bad Request:** If no token is provided.

---

# Boards API
- **Base Path:** `/api/boards`
- **Authentication:** Required for all endpoints.

### 1. Get All Boards
- **Endpoint:** `GET /api/boards`
- **Description:** Retrieves all boards the current user owns or has been invited to.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Success",
    "data": [ ...list of board objects... ]
  }
  ```

### 2. Create a Board
- **Endpoint:** `POST /api/boards`
- **Description:** Creates a new board.
- **Request:** `multipart/form-data`
  - `board` (part): JSON object with `BoardsDTO` structure.
    ```json
    {
      "Title": "My New Board",
      "description": "A board for my ideas.",
      "layout": "grid",
      "isPublic": false
    }
    ```
  - `image` (part, optional): An image file for the board.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Board created successfully",
    "data": { ...board object... }
  }
  ```

### 3. Get Board by ID
- **Endpoint:** `GET /api/boards/{id}`
- **Description:** Retrieves a specific board by its ID.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Success",
    "data": { ...board object... }
  }
  ```
- **Error Responses:**
  - **404 Not Found:** If the board does not exist.

### 4. Update a Board
- **Endpoint:** `PUT /api/boards/{id}`
- **Description:** Updates a board's details.
- **Request Body:** `BoardsDTO`
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Board updated successfully",
    "data": { ...updated board object... }
  }
  ```
- **Error Responses:**
  - **404 Not Found:** If the board does not exist.

### 5. Delete a Board
- **Endpoint:** `DELETE /api/boards/{boardId}`
- **Description:** Deletes a board.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Board deleted successfully",
    "data": null
  }
  ```
- **Error Responses:**
  - **404 Not Found:** If the board does not exist.

### 6. Add a Comment
- **Endpoint:** `POST /api/boards/comments/{id}`
- **Description:** Adds a comment, image, or link to a board.
- **Request:** `multipart/form-data`
  - `entity` (part): JSON object with `BoardsCommentDTO` structure.
    ```json
    {
      "commentText": "This is a comment.",
      "LinkUrl": "http://example.com"
    }
    ```
  - `image` (part, optional): An image file for the comment.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Comment added successfully",
    "data": null
  }
  ```
- **Error Responses:**
  - **404 Not Found:** If the board does not exist.

### 7. Delete a Comment
- **Endpoint:** `DELETE /api/boards/comments/{boardId}/{commentId}`
- **Description:** Deletes a comment from a board.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Comment deleted successfully",
    "data": null
  }
  ```
- **Error Responses:**
  - **400 Bad Request:** If the comment doesn't exist or the user is not authorized to delete it.
  - **404 Not Found:** If the board does not exist.

### 8. Get Public Boards
- **Endpoint:** `GET /api/boards/public/boards`
- **Description:** Retrieves all boards marked as public.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Success",
    "data": [ ...list of public board DTOs... ]
  }
  ```

### 9. Join Board with Code
- **Endpoint:** `GET /api/boards/join/code/{code}`
- **Description:** Joins a private board using a 6-digit code.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Success",
    "data": { ...board object... }
  }
  ```
- **Error Responses:**
  - **400 Bad Request:** If the code format is invalid.
  - **404 Not Found:** If no board matches the code.

### 10. Join Board with Link
- **Endpoint:** `GET /api/boards/join/link/{boardId}`
- **Description:** Joins a private board using its ID (from a shared link).
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Success",
    "data": { ...board object... }
  }
  ```
- **Error Responses:**
  - **404 Not Found:** If the board does not exist.

---

# Profile API
- **Base Path:** `/api/profile`
- **Authentication:** Required for all endpoints.

### 1. Get User Profile
- **Endpoint:** `GET /api/profile`
- **Description:** Retrieves the profile of the currently authenticated user.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Profile retrieved successfully",
    "data": {
      "username": "user123",
      "firstName": "John",
      "lastName": "Doe",
      "bio": "Software developer.",
      "profilePictureUrl": "http://...",
      "email": "user@example.com",
      "userId": "user_id"
    }
  }
  ```
- **Error Responses:**
  - **404 Not Found:** If the profile or user does not exist.

### 2. Edit User Profile
- **Endpoint:** `POST /api/profile`
- **Description:** Updates the user's profile.
- **Request:** `multipart/form-data`
  - `profile` (part): JSON object with `ProfileGetDTO` structure.
    ```json
    {
      "username": "new_username",
      "firstName": "John",
      "lastName": "Doe",
      "bio": "Updated bio."
    }
    ```
  - `profilePicture` (part, optional): A new profile picture file.
- **Success Response (200):**
  ```json
  {
    "status": true,
    "message": "Profile updated successfully",
    "data": "Profile updated successfully"
  }
  ```

---

# AI Chat API
- **Base Path:** `/api/chat`
- **Authentication:** Required for all endpoints.

### 1. Get All Messages
- **Endpoint:** `GET /api/chat/messages`
- **Description:** Retrieves the chat history for the current user.
- **Success Response (200):**
  ```json
  {
    "success": true,
    "message": "Successfully retrieved messages",
    "data": [ ...list of chat message objects... ]
  }
  ```

### 2. Send a Message
- **Endpoint:** `POST /api/chat/messages`
- **Description:** Sends a message to the AI and gets a response.
- **Request Body:**
  ```json
  {
    "message": "What is the capital of France?"
  }
  ```
- **Success Response (200):**
  ```json
  {
    "success": true,
    "message": "Successfully sent message",
    "data": "The capital of France is Paris."
  }
  ```
