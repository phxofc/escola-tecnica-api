CREATE TABLE answer
(
    id                  UUID            NOT NULL,
    organization_id     UUID            NOT NULL,
    description         VARCHAR,
    attached_file_name  VARCHAR(70),
    user_id             UUID            NOT NULL,
    material_id         UUID            NOT NULL,

    created_at      TIMESTAMP WITH TIME ZONE,
    updated_at      TIMESTAMP WITH TIME ZONE,
    deleted_at      TIMESTAMP WITH TIME ZONE,
    is_deleted      BOOLEAN,
    registered_by   UUID,
    updated_by      UUID,

    PRIMARY KEY (id, organization_id, user_id, material_id)
);
