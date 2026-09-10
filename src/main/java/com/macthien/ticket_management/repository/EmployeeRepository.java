package com.macthien.ticket_management.repository;

import com.macthien.ticket_management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Employee> findByIdAndActiveTrue(Long id);
}
