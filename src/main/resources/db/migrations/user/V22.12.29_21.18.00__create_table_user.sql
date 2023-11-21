CREATE TABLE "user"
(
    id                  UUID            NOT NULL,
    organization_id     UUID            NOT NULL,
    name                VARCHAR(60)     NOT NULL,
    last_name           VARCHAR(100)    NOT NULL,
    cpf                 VARCHAR(20)     NOT NULL,
    password            VARCHAR(60)     NOT NULL,
    email               VARCHAR(60)     NOT NULL,
    role                VARCHAR(60)     NOT NULL,
    enabled_access      BOOLEAN         NOT NULL,
    documentation_id    UUID,

    created_at      TIMESTAMP WITH TIME ZONE,
    updated_at      TIMESTAMP WITH TIME ZONE,
    deleted_at      TIMESTAMP WITH TIME ZONE,
    is_deleted      BOOLEAN,
    registered_by   UUID,
    updated_by      UUID,

    PRIMARY KEY (id, organization_id)
);
