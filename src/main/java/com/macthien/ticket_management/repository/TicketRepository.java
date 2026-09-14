package com.macthien.ticket_management.repository;

import com.macthien.ticket_management.entity.Ticket;
import com.macthien.ticket_management.enums.Priority;
import com.macthien.ticket_management.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>{
    @EntityGraph(attributePaths = {"assignee", "reporter"})
    Optional<Ticket> findById(Long id);

    // TODO [MENTOR REVIEW]: Kết quả search được map sang DTO và truy cập reporter/assignee.
    // Query này chưa fetch hai quan hệ đó nên có nguy cơ N+1 (hoặc LazyInitializationException khi tắt OSIV).
    // Hãy chọn @EntityGraph, fetch join phù hợp với pagination, hoặc projection và chứng minh bằng số câu SQL.
    @EntityGraph(attributePaths = {"assignee", "reporter"})
    @Query("SELECT t FROM Ticket t WHERE " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority) AND " +
            "(:assigneeId IS NULL OR t.assignee.id = :assigneeId) AND " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(t.ticketCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Ticket> searchTickets(
            @Param("keyword") String keyword,
            @Param("status") TicketStatus status,
            @Param("priority") Priority priority,
            @Param("assigneeId") Long assigneeId,
            Pageable pageable
    );
}
