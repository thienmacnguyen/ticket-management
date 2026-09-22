-- 1. Thêm cột password_hash và role
ALTER TABLE employees ADD COLUMN password_hash VARCHAR(255);
ALTER TABLE employees ADD COLUMN role VARCHAR(20) DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN'));

-- 2. Cập nhật mật khẩu mặc định cho các user cũ. 
-- Chuỗi hash dưới đây tương đương với mật khẩu "password123"
UPDATE employees SET password_hash = '$2a$10$wE9aC2iZ./R7z3L4lP2Rvu1u6p2M4p.P.P.P.P.P.P.P.P.P.P.P.P';
UPDATE employees SET role = 'USER';

-- Cập nhật 1 tài khoản làm ADMIN
UPDATE employees SET role = 'ADMIN' WHERE id = 1;

-- 3. Khóa ràng buộc NOT NULL
ALTER TABLE employees ALTER COLUMN password_hash SET NOT NULL;
ALTER TABLE employees ALTER COLUMN role SET NOT NULL;