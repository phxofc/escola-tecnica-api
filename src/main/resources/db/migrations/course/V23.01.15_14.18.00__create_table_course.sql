CREATE TABLE course
(
    id              UUID            NOT NULL,
    organization_id UUID            NOT NULL,
    name            VARCHAR(60)     NOT NULL,
    code            VARCHAR(20)     NOT NULL,

    created_at      TIMESTAMP WITH TIME ZONE,
    updated_at      TIMESTAMP WITH TIME ZONE,
    deleted_at      TIMESTAMP WITH TIME ZONE,
    is_deleted      BOOLEAN,
    registered_by   UUID,
    updated_by      UUID,

    PRIMARY KEY (id, organization_id)
);
