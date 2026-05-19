# 🚀 [7-Eleven-Shop-BE]

> [Đây là một RESTful API cho hệ thống bán hàng (shop system) được xây dựng bằng Spring Boot. Dự án cung cấp các chức năng cốt lõi của một hệ thống thương mại điện tử như quản lý sản phẩm, danh mục, đơn hàng và người dùng.]

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?logo=mysql)
![License](https://img.shields.io/badge/license-MIT-green)

## 🛠 Tech Stack

| Thành phần   | Công nghệ                   |
| ------------ | --------------------------- |
| Ngôn ngữ     | Java 21                     |
| Framework    | Spring Boot 3.x             |
| Database     | MySQL 8.x                   |
| ORM          | Spring Data JPA / Hibernate |
| Bảo mật      | Spring Security + JWT       |
| Build tool   | Maven                       |
| Tài liệu API | Swagger / OpenAPI 3         |

---

## ⚙️ Yêu cầu hệ thống

- Java 21+
- Maven 3.8+
- MySQL 8.x

---

## 🚀 Cài đặt & Chạy

### 1. Clone repository

```bash
git clone https://github.com/PhuocTai1704/7-eleven-shop-BE.git
cd 7-eleven-shop-BE
```

### 2. Tạo database

```sql
CREATE DATABASE ten_database CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'ten_user'@'localhost' IDENTIFIED BY 'mat_khau';
GRANT ALL PRIVILEGES ON ten_database.* TO 'ten_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Cấu hình môi trường

Chỉnh sửa `application.properties` với thông tin của bạn (xem phần [Cấu hình](#cấu-hình)).

### 4. Build & chạy

```bash
./mvnw clean install
./mvnw spring-boot:run
```

API sẽ chạy tại: `http://localhost:8080`

---

## 🔧 Cấu hình

Project dùng **biến môi trường** để bảo vệ thông tin nhạy cảm. Nội dung `application.properties`:

```properties
spring.application.name=BEShop

# Database
spring.datasource.url=jdbc:mysql://${DB_HOST:your_host}:${DB_PORT:your_port}/${DB_NAME:your_db_name}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=${DB_USERNAME:your_db_username}
spring.datasource.password=${DB_PASSWORD:your_db_password}

# ORM
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# JWT
jwt_secret=${JWT_SERCET:your_secret_key_here}

# Upload ảnh
project.image=${PROJECT_IMAGE:your_image_folder}
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Swagger
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

Các biến môi trường cần set khi deploy:

| Biến            | Mô tả                       |
| --------------- | --------------------------- |
| `DB_HOST`       | Host của MySQL server       |
| `DB_PORT`       | Port MySQL (mặc định: 3306) |
| `DB_NAME`       | Tên database                |
| `DB_USERNAME`   | Tên đăng nhập MySQL         |
| `DB_PASSWORD`   | Mật khẩu MySQL              |
| `JWT_SERCET`    | Secret key để ký JWT token  |
| `PROJECT_IMAGE` | Đường dẫn thư mục lưu ảnh   |

---

## 📚 API Documentation

Sau khi chạy project, truy cập Swagger UI tại:

```
http://localhost:8080/swagger-ui.html
```

### Các endpoint chính

#### 🔐 Authentication

| Method | Endpoint             | Mô tả                 | Auth |
| ------ | -------------------- | --------------------- | ---- |
| POST   | `/api/auth/register` | Đăng ký tài khoản mới | ❌   |
| POST   | `/api/auth/login`    | Đăng nhập, nhận token | ❌   |

#### 🛒 Orders

| Method | Endpoint                    | Mô tả                                                   | Auth     |
| ------ | --------------------------- | ------------------------------------------------------- | -------- |
| GET    | `/api/orders/{orderId}`     | Lấy chi tiết đơn hàng theo ID                           | ✅       |
| GET    | `/api/orders/user/{userId}` | Lấy danh sách đơn hàng của user (có filter, phân trang) | ✅       |
| GET    | `/api/orders`               | Lấy tất cả đơn hàng (có filter, phân trang)             | ✅ ADMIN |
| POST   | `/api/orders`               | Tạo đơn hàng mới                                        | ✅       |
| PATCH  | `/api/orders/status`        | Cập nhật trạng thái đơn hàng                            | ✅       |

Query params cho các endpoint có phân trang: `status`, `pageNumber`, `pageSize`, `sortBy`, `sortOrder`

#### 📦 Products

| Method | Endpoint                    | Mô tả                                          | Auth     |
| ------ | --------------------------- | ---------------------------------------------- | -------- |
| GET    | `/api/products/{id}`        | Lấy chi tiết sản phẩm theo ID                  | ❌       |
| GET    | `/api/products/slug/{slug}` | Lấy chi tiết sản phẩm theo slug                | ❌       |
| GET    | `/api/products`             | Lấy danh sách sản phẩm (có filter, phân trang) | ❌       |
| POST   | `/api/products`             | Tạo sản phẩm mới (kèm ảnh)                     | ✅ ADMIN |
| PUT    | `/api/products`             | Cập nhật sản phẩm (kèm ảnh)                    | ✅ ADMIN |
| DELETE | `/api/products/{id}`        | Xoá sản phẩm                                   | ✅ ADMIN |

Query params: `categoryId`, `isSale`, `status`, `pageNumber`, `pageSize`, `sortBy`, `sortOrder`

#### 🗂️ Categories

| Method | Endpoint                      | Mô tả                                          | Auth     |
| ------ | ----------------------------- | ---------------------------------------------- | -------- |
| GET    | `/api/categories/{id}`        | Lấy chi tiết danh mục theo ID                  | ❌       |
| GET    | `/api/categories/slug/{slug}` | Lấy chi tiết danh mục theo slug                | ❌       |
| GET    | `/api/categories`             | Lấy danh sách danh mục (có filter, phân trang) | ❌       |
| POST   | `/api/categories`             | Tạo danh mục mới (kèm ảnh)                     | ✅ ADMIN |
| PUT    | `/api/categories`             | Cập nhật danh mục (kèm ảnh)                    | ✅ ADMIN |
| DELETE | `/api/categories/{id}`        | Xoá danh mục                                   | ✅ ADMIN |

Query params: `status`, `pageNumber`, `pageSize`, `sortBy`, `sortOrder`

#### 🖼️ Images

| Method | Endpoint               | Mô tả                 | Auth |
| ------ | ---------------------- | --------------------- | ---- |
| GET    | `/api/file/{fileName}` | Lấy file ảnh theo tên | ❌   |

### Ví dụ request/response

**POST** `/api/auth/login`

```json
// Request
{
  "username": "user@example.com",
  "password": "password123"
}

// Response 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## 📁 Cấu trúc project

```
APIshop/
└── BEShop/
    ├── config/              # Cấu hình Spring (Security, Swagger, CORS, ...)
    ├── controller/          # REST Controllers, định nghĩa các endpoint
    ├── entity/              # JPA Entity classes, ánh xạ với bảng database
    ├── enums/               # Các enum dùng chung (Role, Status, ...)
    ├── exceptions/          # Custom exceptions & Global exception handler
    ├── payloads/            # Request/Response DTOs
    ├── repository/          # Spring Data JPA Repositories
    ├── security/            # JWT, UserDetails, Filter, ...
    ├── service/             # Business logic
    ├── Specification/       # JPA Specification để filter/search nâng cao
    ├── utils/               # Tiện ích dùng chung
    └── BeShopApplication.java  # Entry point
```

---

### Quy ước commit

Sử dụng [Conventional Commits](https://www.conventionalcommits.org/):

| Prefix      | Ý nghĩa                            |
| ----------- | ---------------------------------- |
| `feat:`     | Thêm tính năng mới                 |
| `fix:`      | Sửa lỗi                            |
| `docs:`     | Cập nhật tài liệu                  |
| `style:`    | Format code (không thay đổi logic) |
| `refactor:` | Tái cấu trúc code                  |
| `test:`     | Thêm/sửa test                      |
| `chore:`    | Cập nhật build, dependencies...    |

---

## 🌐 Live Demo

Bạn có thể truy cập bản demo của dự án tại:

- 🔗 Swagger UI: https://7-eleven-shop-be-production.up.railway.app/swagger-ui/index.html
- 👉 Backend (API): https://7-eleven-shop-be-production.up.railway.app/api/
- 👉 Frontend: https://7-eleven-shop-fe-production.up.railway.app/

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 📬 Liên hệ

**[Tên của bạn]** — [email@example.com]

Project Link: [https://github.com/PhuocTai1704/7-eleven-shop-BE](https://github.com/PhuocTai1704/7-eleven-shop-BE)
