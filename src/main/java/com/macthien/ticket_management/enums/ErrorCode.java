package com.macthien.ticket_management.enums;

import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "Nhân viên không tồn tại"),
    EMPLOYEE_INACTIVE(HttpStatus.BAD_REQUEST, "Nhân viên đang bị vô hiệu hóa"),
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "Ticket không tồn tại"),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "Username đã được sử dụng"),
    DUPLICATE_EMPLOYEE(HttpStatus.CONFLICT, "Ticket này đã được phân công"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Dữ liệu yêu cầu không hợp lệ hoặc vượt quá giới hạn"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "Email đã được sử dụng"),
    INVALID_STATUS_TRANSITION(HttpStatus.CONFLICT, "Chuyển trạng thái không hợp lệ"),
    ASSIGNEE_REQUIRED(HttpStatus.BAD_REQUEST, "Cần phân công người xử lý trước khi thực hiện"),
    COMMENT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Không thể comment vào ticket đã đóng"),
    CONCURRENT_UPDATE(HttpStatus.CONFLICT, "Dữ liệu đã bị thay đổi bởi người khác, vui lòng tải lại"),
    UNAUTHORIZED_ACTION(HttpStatus.FORBIDDEN, "Bạn không có quyền thực hiện thao tác này"),
    INVALID_REASON(HttpStatus.BAD_REQUEST, "Lí do không được để trống cho thao tác này"),
    INVALID_NOTE(HttpStatus.BAD_REQUEST, "Ghi chú không được để trống cho thao tác này"),
    INVALID_ASSIGNMENT(HttpStatus.BAD_REQUEST, "Không thể phân công cho Ticket đã đóng"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Cố tình giả mạo người khác"),
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, "Bạn chưa đăng nhập hoặc token không hợp lệ"),
    FORBIDDEN_ACTION(HttpStatus.FORBIDDEN, "Bạn không có quyền thực hiện thao tác này");

    private final HttpStatus httpStatus;
    private final String message;
}
