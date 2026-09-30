# Checklist yêu cầu JWT

Nguồn chính: PDF `04_JWT.pdf`, trang 15–34; các trang 4–14 giải thích cấu trúc, claims và luồng JWT.

| Nhóm | Trang PDF | Triển khai / kiểm chứng |
|---|---:|---|
| Dependency, entity, DTO, repository | 15–21 | `pom.xml`, `entity`, `model`, `repository` |
| Configuration và filter Security | 22–25 | `config`, `filter`, test MockMvc |
| API signup/login/users | 26–29 | `controller`, `JwtApiIntegrationTest` |
| Quy ước lỗi | 30–31 | entry point, access denied handler, advice, tests |
| AJAX login/profile | 32–34 | `templates`, `static/js/mainjs.js` |
| Thay JJWT bằng Nimbus | Yêu cầu bài | `JwtService`, `docs/jjwt-to-nimbus.md`, dependency tree |

## Mốc đối chiếu

Các yêu cầu được cập nhật cùng commit theo từng giai đoạn. Bản cuối dùng Nimbus JOSE + JWT; commit `feat: implement lecture JWT authentication with JJWT` lưu bản theo slide.
