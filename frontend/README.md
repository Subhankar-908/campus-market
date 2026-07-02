# Campus Marketplace Frontend

React frontend for your Spring Boot backend (JWT auth + protected routes).

## Setup

From the `frontend/` folder:

```bash
npm install
npm run dev
```

Open the app at `http://localhost:5173`.

## Backend APIs used

- `POST http://localhost:8080/api/auth/login`
  - Sends JSON: `{ "username": "...", "password": "..." }`
  - Expects a JWT token in response (commonly `{ "token": "..." }`)
- `GET http://localhost:8080/api/products`
  - Sends header: `Authorization: Bearer <token>`

## Project Structure

- `src/components/`
  - `Navbar.jsx`
  - `ProtectedRoute.jsx`
- `src/pages/`
  - `Login.jsx`
  - `Products.jsx`
- `src/services/`
  - `api.js` (Axios base URL + auth header interceptor)
  - `auth.js` (localStorage token helpers)
- `src/App.js` (routing)

