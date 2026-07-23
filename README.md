\# E-Commerce Backend



Backend API for a simplified e-commerce platform built with Spring Boot.



\## Tech Stack



\- Java 21

\- Spring Boot

\- Spring Security (JWT)

\- Spring Data JPA

\- H2 Database (in-memory)



\## How to Run



1\. Clone the repository

2\. Make sure you have Java 21 and Maven installed

3\. Run: `mvn spring-boot:run`

4\. The API will be available at `http://localhost:8080`



\## API Endpoints



\### Authentication

\- `POST /api/auth/signup` — Register new user

\- `POST /api/auth/login` — Login user



\### Users

\- `GET /api/users` — Get current user details

\- `PATCH /api/users` — Update current user details



\### Categories

\- `POST /api/categories` — Create category

\- `GET /api/categories` — Get all categories

\- `GET /api/categories/{id}` — Get category by ID

\- `PATCH /api/categories/{id}` — Update category

\- `DELETE /api/categories/{id}` — Delete category



\### Products

\- `POST /api/products` — Create product

\- `GET /api/products` — Get all products

\- `GET /api/products/{id}` — Get product by ID

\- `PATCH /api/products/{id}` — Update product

\- `DELETE /api/products/{id}` — Delete product



\### Cart

\- `POST /api/cart` — Create cart (or return existing)

\- `DELETE /api/cart` — Delete cart and all items

\- `GET /api/cart/{cartId}/items` — Get cart items



\### Cart Items

\- `POST /api/cartItems/{cartId}` — Add item to cart

\- `GET /api/cartItems/{cartItemId}` — Get cart item

\- `PATCH /api/cartItems/{cartItemId}` — Update cart item quantity



\## Authentication



Most endpoints require a JWT token in the Authorization header:
Authorization: Bearer <token>


Get a token by signing up or logging in via `/api/auth/signup` or `/api/auth/login`.



\## Notes



\- H2 in-memory database resets on server restart

\- Default JWT secret is hardcoded for development

