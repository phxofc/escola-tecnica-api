INSERT INTO "user" (
                    id,
                    organization_id,
                    name,
                    last_name,
                    cpf,
                    password,
                    email,
                    role,
                    created_at,
                    is_deleted,
                    registered_by,
                    enabled_access
        )
VALUES (
        '00000000-0000-0000-0000-000000000001',
        '00000000-0000-0000-0000-000000000001',
        'Administrador Geral',
        'Geral',
        '111.111.111-00',
        '$2a$10$ZcDTdxR.QpPAeW1O5ut80eC.HTCJDDaKrCrnx0libMVQ6n4pn9l/C',
        'admin@ect.br',
        'ADMIN',
        '2022-12-02 00:00:00.000000 +00:00',
        false,
        '00000000-0000-0000-0000-000000000001',
        true
        );
