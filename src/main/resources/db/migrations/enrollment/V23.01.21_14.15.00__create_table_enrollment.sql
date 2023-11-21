CREATE TABLE enrollment
(
    id                      UUID    NOT NULL,
    organization_id         UUID    NOT NULL,
    course_id               UUID    NOT NULL,
    user_id                 UUID    NOT NULL,
    clazz_id                UUID    NOT NULL,
    registration_approved   BOOLEAN NOT NULL,

    created_at              TIMESTAMP WITH TIME ZONE,
    updated_at              TIMESTAMP WITH TIME ZONE,
    deleted_at              TIMESTAMP WITH TIME ZONE,
    is_deleted              BOOLEAN,
    registered_by           UUID,
    updated_by              UUID,

    PRIMARY KEY (id, organization_id, course_id, user_id, clazz_id)
);
