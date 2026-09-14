package com.macthien.ticket_management.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreateDTO {
    @NotBlank(message = "Username không được để trống")
    private String username;

    // TODO [MENTOR REVIEW]: fullName là dữ liệu bắt buộc theo đề bài nhưng hiện chưa có @NotBlank.
    // Đồng bộ validation này với ràng buộc NOT NULL/CHECK trong schema PostgreSQL.
    @NotBlank
    private String fullName;
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;
}
