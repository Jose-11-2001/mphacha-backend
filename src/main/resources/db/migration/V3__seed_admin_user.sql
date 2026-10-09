-- Seed the Mphacha-Yanga admin account.
-- Email: adminmphacha@gmail.com
-- Password: Admin@2024!   (change before going live!)
INSERT INTO users (email, password, full_name, phone, role, created_at)
VALUES (
    'adminmphacha@gmail.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'Mphacha Admin',
    NULL,
    'ADMIN',
    NOW()
);