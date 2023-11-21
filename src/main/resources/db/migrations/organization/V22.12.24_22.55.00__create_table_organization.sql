CREATE TABLE organization
(
    id            UUID        NOT NULL PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    is_root       BOOLEAN     NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE,
    updated_at    TIMESTAMP WITH TIME ZONE,
    deleted_at    TIMESTAMP WITH TIME ZONE,
    is_deleted    BOOLEAN,
    registered_by UUID,
    updated_by    UUID
);
