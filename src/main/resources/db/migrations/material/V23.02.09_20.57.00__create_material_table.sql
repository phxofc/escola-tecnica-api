CREATE TABLE material
(
    id                      UUID            NOT NULL,
    title                   VARCHAR(60)     NOT NULL,
    description             VARCHAR         NOT NULL,
    type                    VARCHAR(50)     NOT NULL,
    attached_file_name      VARCHAR(100),
    is_active               BOOLEAN,
    course_id               UUID    NOT NULL,
    clazz_id                UUID    NOT NULL,
    discipline_id           UUID    NOT NULL,

    organization_id         UUID            NOT NULL,

    created_at              TIMESTAMP WITH TIME ZONE,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    is_deleted              BOOLEAN,
    registered_by           UUID,
    updated_by              UUID,

    PRIMARY KEY (id, organization_id)
);