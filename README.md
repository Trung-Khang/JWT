# JWT Spring Boot 3 với Nimbus JOSE + JWT

Sinh viên: **Nguyễn Trung Khang - MSSV 24133028**.

Đây là bài thực hành theo `04_JWT.pdf` (trang 15-34): đăng ký/đăng nhập, phát JWT, lọc Bearer token, API người dùng và giao diện AJAX. Lịch sử Git lưu riêng bản JJWT theo slide rồi chuyển bản cuối sang **Nimbus JOSE + JWT** (`HS256`).

## Stack và cấu trúc

- Java 21, Spring Boot 3.4, Spring Security 6, Spring Data JPA, Thymeleaf, MySQL.
- `src/main/java/.../entity`: `User` implements `UserDetails`; `model`, `repository`, `service`, `filter`, `config`, `controller`, `exception` phân tách theo trách nhiệm.
- `src/main/resources/templates`: `login.html`, `profile.html`; `static/js/mainjs.js` dùng jQuery AJAX.
- `src/test`: integration test bằng H2 riêng, không phải bằng chứng đã kiểm tra MySQL thật.

## Chuẩn bị MySQL và biến môi trường (Windows PowerShell)

Tạo database: `CREATE DATABASE jwt_springboot3 CHARACTER SET utf8mb4;`.

Không commit password/secret. Sinh khóa đủ 256 bit rồi đặt biến cho cửa sổ PowerShell hiện tại:

```powershell
$bytes = New-Object byte[] 32
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET_BASE64 = [Convert]::ToBase64String($bytes)
$env:DB_URL = 'jdbc:mysql://localhost:3306/jwt_springboot3?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false'
$env:DB_USERNAME = 'root'
$env:DB_PASSWORD = 'mat-khau-cua-ban'
```

Biến tùy chọn: `SERVER_PORT` (mặc định `8005`), `JWT_EXPIRATION_MS` (mặc định `3600000`, milliseconds), `CORS_ALLOWED_ORIGIN` (mặc định `http://localhost:8005`).

## Build, test và chạy

```powershell
mvn test
mvn spring-boot:run
```

Mở `http://localhost:8005/login`; dừng tiến trình bằng `Ctrl+C`. API: `POST /auth/signup`, `POST /auth/login`, `GET /users/me`, `GET /users` (cả hai GET cần `Authorization: Bearer <token>`). Import collection và environment mẫu trong `postman/`; chúng không chứa secret/token thật.

Ví dụ đăng ký:

```json
{"email":"student@example.com","password":"secret12","fullName":"Nguyen Trung Khang"}
```

Login trả `{ "token": "...", "expiresIn": 3600000 }`; `expiresIn` là milliseconds. Đăng ký email trùng trả 409, validation 400, sai login/missing/malformed/expired JWT trả 401, access denied trả 403.

## Lưu ý bảo mật

CSRF được tắt vì API stateless dùng Bearer header, không dùng cookie session. CORS chỉ cấu hình origin cụ thể và credentials, không wildcard. Giao diện giữ token trong `localStorage` để khớp demo PDF; đây có rủi ro XSS. Logout chỉ xóa khóa `jwt-demo-token` phía client, không thể thu hồi token đã phát hành trước hạn.

## Đối chiếu PDF và lịch sử

| Nội dung | PDF | Kết quả |
|---|---:|---|
| Dependencies, entity, DTO, services | 15-22 | `pom.xml`, packages backend |
| Filter/Security/API | 23-31 | Bearer filter, SecurityFilterChain, REST API và error handlers |
| AJAX | 32-34 | Login -> profile -> logout |
| JJWT -> Nimbus | yêu cầu bài | [docs/jjwt-to-nimbus.md](docs/jjwt-to-nimbus.md) |

Mốc JJWT: `1e67553`. Mốc chuyển Nimbus: `e701dd1` và các commit kiểm thử/tài liệu sau đó. Checklist chi tiết ở [docs/requirements-checklist.md](docs/requirements-checklist.md).
Baitap_30thang9_laptrinhWEB
