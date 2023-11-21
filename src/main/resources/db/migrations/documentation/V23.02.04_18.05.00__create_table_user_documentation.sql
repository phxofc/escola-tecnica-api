CREATE TABLE documentation
(
    id                  UUID                NOT NULL,
    organization_id     UUID                NOT NULL,

    cpf_file_name       VARCHAR(70),
    rg_file_name        VARCHAR(70),
    cr_file_name        VARCHAR(70),
    validated           BOOLEAN,

    created_at          TIMESTAMP WITH TIME ZONE,
    updated_at          TIMESTAMP WITH TIME ZONE,
    deleted_at          TIMESTAMP WITH TIME ZONE,
    is_deleted          BOOLEAN,
    registered_by       UUID,
    updated_by          UUID,

    PRIMARY KEY (id, organization_id)
);
