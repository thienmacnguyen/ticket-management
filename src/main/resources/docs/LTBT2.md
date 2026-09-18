1. Nên dùng Enum, ưu điểm:   
- Chỉ cho phép các giá trị đã định nghĩa.  
- Tránh typo.  
- Code dễ đọc.  
- IDE hỗ trợ tốt.  
- Rất phù hợp với trạng thái/hành động có tập giá trị cố định.  
  nhược điểm:  
- thiếu linh hoạt với các giá trị thường xuyên thay đổi  
- phải sửa code khi muốn thêm giá trị  
2. Hàm thuần là hàm chỉ phụ thuộc vào input truyền vào, không có side effect (không ghi db, không sửa biến, … (không tác động ra bên ngoài). Nếu determineNextStatus() gọi repository thì lúc này nó không còn là pure function, nó sẽ phụ thuộc vào db.  
3. Không được gán actorIsReporter bằng true để vượt qua kiểm tra quyền vì nếu thế thì người được phân công sẽ có thể sửa quyền của mình, giả sử có thể sửa vé theo ý muốn của mình như thế sẽ sai business rule.  
4. @ManytoOne biểu diễn quan hệ nhiều \- một chẳng hạn như 1 assignee có thể được phân công xử lí nhiều ticket, FetchType.LAZY ảnh hưởng đến việc lấy dữ liệu chúng ta lấy ticket nó sẽ lấy ticket nhưng sẽ chưa lấy fullName, userName mà khi nào mapper lấy getUserName(), … thì mới lấy userName, lỗi lazy xảy ra khi lúc lấy chưa lấy nhưng thông tin cần thiết đến khi lấy thì nếu transaction/session đã đóng thì sẽ gây ra lỗi lazy.  
5. @transaction được hiểu như là một giao dịch bắt đầu khi chạy 1 chức năng cho đến khi kết thúc, nếu cập nhật ticket thành công nhưng lưu history thất bại thì sẽ xử lí bằng cách roll back lại để tránh dữ liệu bị sai sót, không khớp.