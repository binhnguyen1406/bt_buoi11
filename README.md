# Selenium Framework - Lab 11

Framework Selenium + TestNG + POM cho Lab 11: GitHub Actions, Matrix Strategy, GitHub Secrets, Selenium Grid, Allure Report, GitHub Pages, Test Strategy và Test Plan.

## Công nghệ
- Java 17
- Maven
- Selenium WebDriver
- TestNG
- WebDriverManager
- Allure Report
- GitHub Actions
- Docker + Selenium Grid

## Cấu trúc chính
- `.github/workflows/selenium-ci.yml`: pipeline cơ bản cho Bài 1
- `.github/workflows/selenium-full.yml`: pipeline đầy đủ cho Bài 2, 3, 5, 6, 7
- `docker-compose.yml`: Selenium Grid cho Bài 4
- `docs/TestStrategy_ShopEasy.md`: Bài 7 - Test Strategy
- `docs/TestPlan_Sprint5_VPBank.md`: Bài 7 - Test Plan Sprint 5

## Thiết lập local
1. Cài Java 17+
2. Cài Maven
3. Đặt biến môi trường:
   - `APP_USERNAME`
   - `APP_PASSWORD`
4. Chạy smoke test local:
   ```bash
   mvn clean test -Dbrowser=chrome -Denv=dev -DsuiteXmlFile=testng-smoke.xml
   ```

## Chạy với Selenium Grid
Khởi động Grid:
```bash
docker-compose up -d
```

Mở UI:
```text
http://localhost:4444
```

Chạy test với Grid:
```bash
mvn clean test -Dgrid.url=http://localhost:4444 -DsuiteXmlFile=testng-grid.xml
```

## Chạy Allure
```bash
mvn clean test -Dbrowser=chrome -Denv=dev -DsuiteXmlFile=testng-smoke.xml
mvn allure:serve
```

## GitHub Secrets cần tạo
- `SAUCEDEMO_USERNAME`
- `SAUCEDEMO_PASSWORD`
- `VPBANK_API_KEY` (cho payment test job)

## Ghi chú
- Không hardcode credential trong source code.
- Workflow CI sẽ tự bật headless khi chạy trên GitHub Actions.
- `testng-payment.xml` dùng cho job riêng của Sprint 5.
