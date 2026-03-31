# Test Plan - Sprint 5 - Thanh toán trả góp VPBank

## 1. Mục tiêu sprint
Sprint 5 triển khai tính năng **Thanh toán trả góp VPBank** cho đơn hàng đủ điều kiện. Người dùng có thể chọn kỳ hạn 3/6/12 tháng, và được áp dụng mức phí 0% khi đơn hàng có tổng giá trị từ 3.000.000 VNĐ trở lên.

## 2. Rủi ro nghiệp vụ liên quan đến tiền thật
1. **Đơn hàng dưới 3 triệu nhưng vẫn cho phép trả góp** → người dùng bị tính phí hoặc tham gia gói không hợp lệ.
2. **Hệ thống tính sai số tiền trả mỗi tháng** → người dùng bị trừ thiếu hoặc thừa so với chính sách.
3. **Thanh toán thành công nhưng đơn hàng không được xác nhận** → mất tiền nhưng không tạo đơn thành công.
4. **Hệ thống lỗi ở bước callback từ VPBank** → tiền bị giữ, đơn hàng ở trạng thái pending không rõ ràng.
5. **Session hết hạn giữa quá trình thanh toán** → người dùng bị văng khỏi luồng thanh toán, có nguy cơ phát sinh thanh toán lặp hoặc mất dữ liệu.

## 3. Blockers có thể làm trễ tiến độ
- Sandbox VPBank chưa sẵn sàng hoặc rate limit quá thấp.
- Chưa có test account và test card/bank profile của VPBank.
- API contract hoặc Postman/Swagger cho payment chưa hoàn thiện.
- Môi trường staging chưa deploy đúng build Sprint 5.
- Không có dữ liệu đơn hàng >= 3 triệu để test happy path.

## 4. Kế hoạch xử lý blockers
- Yêu cầu đối tác VPBank cung cấp sandbox trước sprint tối thiểu 2 tuần.
- PM/BA chuẩn bị sớm tài khoản test và rule test data.
- Backend phải chốt API contract trước khi dev hoàn tất.
- Tuần 1 test với mock service, tuần 2 chuyển sang staging integration.
- Dùng script seed dữ liệu để tạo order đủ điều kiện nhanh.

## 5. 15 test case cho Sprint 5

| TC-ID | Tiêu đề | Loại | Ưu tiên | Bước thực hiện tóm tắt | Kết quả mong đợi |
|---|---|---|---|---|---|
| TC-001 | Thanh toán trả góp 3 tháng thành công với đơn >= 3 triệu | API | P1 | Gửi request tạo payment installment 3 tháng với order đủ điều kiện | 201, `status=APPROVED` |
| TC-002 | Từ chối trả góp khi đơn < 3 triệu | API | P1 | Gửi request payment với order dưới ngưỡng | 400, `error=ORDER_TOO_SMALL` |
| TC-003 | UI hiển thị đúng 3 tùy chọn 3/6/12 tháng | UI | P1 | Mở màn hình payment với order đủ điều kiện | Hiển thị đủ 3 radio button |
| TC-004 | Tính số tiền trả mỗi tháng chính xác cho kỳ hạn 3 tháng | Unit | P1 | Gọi hàm tính installment với dữ liệu chuẩn | Số tiền theo công thức đúng, phí = 0 |
| TC-005 | Hiển thị rõ tổng tiền gốc, phí và số tiền hàng tháng | UI | P2 | Chọn phương thức trả góp trên checkout | Màn hình hiển thị đầy đủ breakdown |
| TC-006 | Thanh toán trả góp 6 tháng thành công | API | P1 | Gửi request tạo payment installment 6 tháng | 201, `status=APPROVED` |
| TC-007 | Thanh toán trả góp 12 tháng thành công | API | P1 | Gửi request tạo payment installment 12 tháng | 201, `status=APPROVED` |
| TC-008 | E2E thanh toán thành công với tài khoản VPBank hợp lệ | E2E | P1 | Từ checkout chọn VPBank installment và hoàn tất | Đơn hàng được APPROVED |
| TC-009 | Từ chối thanh toán khi thẻ/tài khoản VPBank hết hạn | API | P1 | Gửi request với dữ liệu thẻ hết hạn | 400, `error=CARD_EXPIRED` |
| TC-010 | Xử lý thân thiện khi VPBank sandbox lỗi | API | P2 | Mô phỏng response 503 từ gateway | 503 và message dễ hiểu cho người dùng |
| TC-011 | Người dùng hủy giữa chừng không bị trừ tiền | E2E | P1 | Bắt đầu thanh toán rồi nhấn Hủy | Không ghi nhận giao dịch thành công |
| TC-012 | Session hết hạn giữa lúc thanh toán | E2E | P2 | Để session timeout trong bước nhập xác nhận | Chuyển về đăng nhập, dữ liệu order được lưu phù hợp |
| TC-013 | Ẩn tùy chọn trả góp với đơn < 3 triệu | UI | P2 | Mở checkout với order không đủ điều kiện | Không hiển thị option trả góp |
| TC-014 | Gửi email xác nhận sau khi trả góp thành công | API | P2 | Hoàn tất payment rồi kiểm tra queue/email service | Email xác nhận được gửi trong 5 phút |
| TC-015 | Lịch sử đơn hàng hiển thị đúng thông tin trả góp | UI | P3 | Mở order history sau giao dịch thành công | Hiển thị kỳ hạn, số tiền, trạng thái đúng |

## 6. Definition of Done cho Sprint 5
- 100% smoke test PASS.
- Tối thiểu 95% payment regression PASS.
- Không còn bug P1/P2 mở liên quan tiền, trạng thái đơn, callback.
- Có log theo dõi transaction id/correlation id để đối soát.
- Allure report và kết quả CI/CD được publish đầy đủ.
- UAT được PM/PO xác nhận cho 3 luồng kỳ hạn 3, 6 và 12 tháng.

## 7. Pipeline riêng cho Sprint 5
Workflow `selenium-full.yml` đã có job `payment-test` chỉ chạy khi PR nhắm vào nhánh `feature/vpbank-payment`. Job này gọi suite `testng-payment.xml` và yêu cầu secret `VPBANK_API_KEY` để đảm bảo cấu hình CI/CD cho nhánh payment.
