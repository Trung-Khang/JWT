# Chuyển JJWT sang Nimbus JOSE + JWT

Commit `1e67553` là bản JJWT theo trang 15–29 PDF. Bản cuối thay `Jwts.builder`/`Claims` bằng `JWTClaimsSet.Builder`; `signWith` bằng `SignedJWT.sign(new MACSigner(secret))`; parser bằng `SignedJWT.parse`, `MACVerifier.verify` rồi mới lấy claims.

Khóa nhận ở `JWT_SECRET_BASE64`, giải mã Base64, tối thiểu 32 byte. Bản cuối chỉ chấp nhận HS256, xác minh chữ ký trước khi đọc claims, bắt buộc `sub`/`exp` và kiểm tra `nbf` khi có. `alg=none`, token lỗi, sai chữ ký, hết hạn đều trả 401 từ filter. Không còn dependency/import `io.jsonwebtoken` tại HEAD.

Xem bản cũ: `git show 1e67553:src/main/java/vn/hcmute/jwt/service/JwtService.java`.
