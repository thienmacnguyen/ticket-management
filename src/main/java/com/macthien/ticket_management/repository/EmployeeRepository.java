package com.macthien.ticket_management.repository;

import com.macthien.ticket_management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // TODO [MENTOR REVIEW]: Cách tìm này gộp hai trường hợp "không tồn tại" và "đã inactive" thành Optional.empty().
    // Service vì vậy luôn trả EMPLOYEE_NOT_FOUND và không bao giờ dùng được EMPLOYEE_INACTIVE. Hãy tách hai bước kiểm tra.
    Optional<Employee> findById(Long id);
}
