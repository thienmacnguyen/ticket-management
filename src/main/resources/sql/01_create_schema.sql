DROP TABLE IF EXISTS ticket_status_history;
DROP TABLE IF EXISTS ticket_comments;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS employees;
-- Chạy lệnh tạo DB thủ công trước: CREATE DATABASE ticket_management;
-- Sau đó connect vào DB ticket_management và chạy script dưới đây:

-- 1. Bảng nhân viên
CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    -- TODO [MENTOR REVIEW]: full_name là bắt buộc nhưng schema vẫn cho phép NULL/chuỗi trắng.
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng Ticket
CREATE TABLE tickets (
    id BIGSERIAL PRIMARY KEY,
    ticket_code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    -- TODO [MENTOR REVIEW]: Đồng bộ tính bắt buộc của description giữa đề bài, DTO, Entity và database.
    description TEXT NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    reporter_id BIGINT NOT NULL,
    assignee_id BIGINT,
    version BIGINT NOT NULL DEFAULT 0, -- Dùng cho Optimistic Locking
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    resolved_at TIMESTAMP,
    
    -- Foreign Keys
    CONSTRAINT fk_ticket_reporter FOREIGN KEY (reporter_id) REFERENCES employees(id),
    CONSTRAINT fk_ticket_assignee FOREIGN KEY (assignee_id) REFERENCES employees(id),
    
    -- Check Constraints thay cho Enum
    CONSTRAINT chk_ticket_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
    CONSTRAINT chk_ticket_status CHECK (status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'))
);

CREATE TABLE ticket_assignment_history (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    old_assignee_id BIGINT,
    new_assignee_id BIGINT NOT NULL,
    changed_by BIGINT NOT NULL,
    reason TEXT,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_assign_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    CONSTRAINT fk_assign_old_emp FOREIGN KEY (old_assignee_id) REFERENCES employees(id),
    CONSTRAINT fk_assign_new_emp FOREIGN KEY (new_assignee_id) REFERENCES employees(id),
    CONSTRAINT fk_assign_changer FOREIGN KEY (changed_by) REFERENCES employees(id)
);

-- 3. Bảng Comments
CREATE TABLE ticket_comments (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    -- TODO [MENTOR REVIEW]: content <> '' vẫn chấp nhận chuỗi toàn dấu cách; dùng TRIM(content) để chặn.
    content TEXT NOT NULL CHECK (content <> ''),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_comment_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES employees(id)
);

-- 4. Bảng lịch sử chuyển trạng thái
CREATE TABLE ticket_status_history (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL,
    from_status VARCHAR(20),
    to_status VARCHAR(20) NOT NULL,
    changed_by BIGINT NOT NULL,
    note TEXT NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_history_ticket FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    CONSTRAINT fk_history_changer FOREIGN KEY (changed_by) REFERENCES employees(id),
    CONSTRAINT chk_history_from_status CHECK (from_status IS NULL OR from_status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')),
    CONSTRAINT chk_history_to_status CHECK (to_status IN ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'))
);

-- 5. Tạo INDEX tối ưu hoá truy vấn
CREATE INDEX idx_tickets_status ON tickets(status);
CREATE INDEX idx_tickets_priority ON tickets(priority);
CREATE INDEX idx_tickets_assignee ON tickets(assignee_id);
CREATE INDEX idx_tickets_created_at ON tickets(created_at);
CREATE INDEX idx_comments_ticket_id ON ticket_comments(ticket_id);
CREATE INDEX idx_history_ticket_id ON ticket_status_history(ticket_id);
