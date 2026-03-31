# Test Strategy Document - ShopEasy

## 1. Mục tiêu và phạm vi kiểm thử
ShopEasy là nền tảng mua sắm online gồm API Java Spring Boot, web React và mobile React Native. Trong bối cảnh team có 4 developer, 1 QA, 1 designer và 1 PM, chiến lược kiểm thử phải tập trung vào các luồng tạo doanh thu và các rủi ro có tác động trực tiếp đến trải nghiệm người dùng cuối. Mục tiêu của tài liệu này là xác định phạm vi kiểm thử, tỉ lệ các loại test, tiêu chí dừng, rủi ro và lịch vận hành test cho dự án trong giai đoạn Sprint 5 chuẩn bị ra mắt tính năng trả góp VPBank.

**IN SCOPE** gồm 5 module chính. Thứ nhất là **Đăng ký tài khoản**, vì đây là cửa ngõ chuyển đổi user mới thành khách hàng. Nếu lỗi đăng ký xảy ra, tỷ lệ đăng nhập và mua hàng giảm ngay từ đầu funnel. Thứ hai là **Đăng nhập/Xác thực**, đây là module mang tính bảo mật cao, ảnh hưởng quyền truy cập, session, thông tin tài khoản, địa chỉ giao hàng và lịch sử đơn. Thứ ba là **Tìm kiếm sản phẩm**, vì với thương mại điện tử, search là tính năng có tần suất sử dụng lớn; kết quả sai, chậm hoặc không chính xác sẽ làm giảm chuyển đổi. Thứ tư là **Giỏ hàng**, đây là điểm nối giữa hành vi duyệt sản phẩm và quyết định mua. Giỏ hàng phải xử lý đúng số lượng, giá, khuyến mãi và trạng thái tồn kho. Thứ năm là **Thanh toán**, gồm cả phương thức thường và trả góp VPBank, vì đây là nơi liên quan trực tiếp đến tiền thật, doanh thu, lỗi tài chính và uy tín thương hiệu.

**OUT OF SCOPE** gồm 2 module. Module **Admin Dashboard** nằm ngoài phạm vi sprint vì chưa có hạng mục release cho end-user ở giai đoạn này; test sâu dashboard trong khi thanh toán đang là ưu tiên sẽ làm phân tán nguồn lực. Module **Báo cáo thống kê nội bộ** cũng được đưa ra ngoài phạm vi vì không tác động trực tiếp đến trải nghiệm mua hàng của khách trong Sprint 5. Hai module này có thể lên kế hoạch regression riêng ở phase sau.

## 2. Phân loại test và tỉ lệ
Với đặc thù ứng dụng thương mại điện tử, chiến lược nên ưu tiên API test nhiều hơn UI test vì nghiệp vụ giá, giỏ hàng, đơn hàng, payment và đồng bộ trạng thái phần lớn nằm ở lớp backend. Tỉ lệ đề xuất như sau:
- **Unit Test: 20%** - dùng JUnit 5 và Mockito. Unit test do developer chịu trách nhiệm chính, kiểm tra logic nhỏ như tính giá, validate rule, mapping DTO, xử lý trạng thái đơn hàng. Loại test này rẻ, chạy nhanh, phù hợp để chặn lỗi sớm trong mỗi commit.
- **API Test: 45%** - dùng RestAssured. Đây là tầng quan trọng nhất vì thanh toán, giỏ hàng, order lifecycle, search/filter và tích hợp VPBank đều phụ thuộc API. API test giúp xác minh contract, schema, status code, business rule và negative cases hiệu quả hơn UI.
- **UI Test: 20%** - dùng Selenium + POM. UI automation chỉ tập trung vào smoke và các E2E path quan trọng như login, add to cart, checkout, chọn phương thức trả góp, xác nhận thông tin hiển thị. Không nên đẩy quá nhiều trường hợp xuống UI vì vừa chậm vừa dễ flaky.
- **Performance Test: 10%** - dùng JMeter hoặc kịch bản tương đương. ShopEasy cần chịu tải cao vào dịp flash sale, payday hoặc campaign ngân hàng. Kiểm thử hiệu năng phải tập trung vào search, add-to-cart, checkout và payment callback.
- **Security Test: 5%** - dùng OWASP ZAP, dependency scan, secret scan. Vì có dữ liệu cá nhân và giao dịch tài chính, các luồng đăng nhập và thanh toán phải được quét bảo mật trước release.

## 3. Definition of Done
Một tính năng chỉ được xem là “đã test xong” khi thỏa tất cả tiêu chí sau. Thứ nhất, **Smoke test phải pass 100%** trên môi trường staging sau khi deploy build mới. Thứ hai, **Regression test phải đạt tối thiểu 95% PASS**; phần fail còn lại nếu có phải được phân tích rõ nguyên nhân và không thuộc nhóm rủi ro cao. Thứ ba, **không còn bug P1/Blocker đang mở**. Thứ tư, **không còn bug P2/Critical nào chưa có kế hoạch xử lý hoặc chấp thuận rủi ro** từ PM/tech lead. Thứ năm, **code coverage tối thiểu đạt 80% cho các module nghiệp vụ quan trọng** như payment, order, pricing và authentication. Thứ sáu, **Allure report phải được publish và review** để toàn team nhìn được xu hướng pass/fail, step chi tiết và ảnh fail. Cuối cùng, dữ liệu test, môi trường và tài liệu release note phải sẵn sàng cho UAT.

## 4. Quản lý rủi ro
**Rủi ro 1 - Sandbox VPBank không ổn định.** Xác suất cao, tác động cao. Nếu sandbox chập chờn, toàn bộ test payment E2E sẽ bị block hoặc cho kết quả sai lệch. Kế hoạch giảm thiểu là làm việc với đối tác sớm, có mock API dự phòng, và tách test contract khỏi test live integration.

**Rủi ro 2 - Dữ liệu staging bị xóa hoặc reset đột xuất.** Xác suất trung bình, tác động cao. Hệ quả là test data user, cart, đơn hàng và thông tin ngân hàng bị mất, gây chậm tiến độ. Giảm thiểu bằng script seed data tự động trước mỗi lần chạy regression và lưu bộ test account chuẩn.

**Rủi ro 3 - API 3rd party hoặc callback payment bị down.** Xác suất trung bình, tác động cao vì có thể làm đơn bị pending hoặc lệch trạng thái. Giảm thiểu bằng cách log correlation id, có cơ chế retry hợp lý, mock callback và dashboard theo dõi lỗi tích hợp.

**Rủi ro 4 - CI server hoặc GitHub Actions lỗi artifact/storage.** Xác suất thấp, tác động trung bình đến cao. Nếu artifact hoặc Allure không được upload thì khó phân tích fail và mất tính minh bạch. Giảm thiểu bằng retention policy, cleanup định kỳ, upload screenshot khi fail và workflow tách test/publish rõ ràng.

## 5. Lịch trình kiểm thử
- **Smoke Test**: chạy sau mỗi commit/PR, thời gian khoảng 5 phút, trigger tự động bằng GitHub Actions.
- **Regression Test**: chạy hàng đêm lúc 2:00 AM, thời gian khoảng 45 phút, trigger bằng cron schedule.
- **Performance Test**: chạy hàng tuần hoặc trước chiến dịch lớn, thời gian khoảng 2 giờ, có thể manual hoặc cron riêng.
- **Security Scan**: chạy trước mỗi release production, thời gian khoảng 3 giờ, do QA phối hợp DevOps/Security.
- **UAT**: thực hiện cuối mỗi sprint trong 2-3 ngày, có PM/PO tham gia xác nhận business flow.

Chiến lược này ưu tiên giá trị kinh doanh, rủi ro tài chính và tính thực tế của nguồn lực. Với chỉ 1 QA, việc dồn trọng tâm vào API, smoke E2E quan trọng và tự động hóa báo cáo là cách tối ưu nhất để giữ chất lượng mà vẫn đáp ứng nhịp release 4 tuần.
