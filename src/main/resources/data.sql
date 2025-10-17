-- ===============================
-- Default Season + League Tiers
-- ===============================

-- GROUP (nodig vanwege foreign key)
INSERT INTO groups (uuid, name)
VALUES ('11111111-1111-1111-1111-111111111111', 'Default Group');

-- SEASON
INSERT INTO seasons (uuid, name, group_id, start_date, end_date, is_active, description)
VALUES (
           '22222222-2222-2222-2222-222222222222',
           'Default Season',
           '11111111-1111-1111-1111-111111111111',
           '2025-01-01T00:00:00',
           '2025-12-31T23:59:59',
           true,
           'Default active season for all players'
       );

-- LEAGUE TIERS
INSERT INTO league_tiers (uuid, season_id, name, min_elo, max_elo, tier_level, color_hex, icon_url, description, is_custom_name)
VALUES
    ('33333333-3333-3333-3333-333333333333', '22222222-2222-2222-2222-222222222222', 'Bronze',      0,    499, 1, '#cd7f32', NULL, 'Beginner tier', false),
    ('44444444-4444-4444-4444-444444444444', '22222222-2222-2222-2222-222222222222', 'Silver',    500,    999, 2, '#c0c0c0', NULL, 'Novice tier', false),
    ('55555555-5555-5555-5555-555555555555', '22222222-2222-2222-2222-222222222222', 'Gold',     1000,   1499, 3, '#ffd700', NULL, 'Intermediate tier', false),
    ('66666666-6666-6666-6666-666666666666', '22222222-2222-2222-2222-222222222222', 'Platinum', 1500,   1999, 4, '#e5e4e2', NULL, 'Advanced tier', false),
    ('77777777-7777-7777-7777-777777777777', '22222222-2222-2222-2222-222222222222', 'Diamond',  2000,   2499, 5, '#b9f2ff', NULL, 'Expert tier', false),
    ('88888888-8888-8888-8888-888888888888', '22222222-2222-2222-2222-222222222222', 'Master',   2500,   2999, 6, '#9400d3', NULL, 'Elite tier', false),
    ('99999999-9999-9999-9999-999999999999', '22222222-2222-2222-2222-222222222222', 'Grandmaster', 3000, 9999, 7, '#ff4500', NULL, 'Top tier', false);
