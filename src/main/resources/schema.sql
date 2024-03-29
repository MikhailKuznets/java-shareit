DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS requests CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255)                            NOT NULL,
    email   VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    request_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(255)  NOT NULL,
    requester_id   BIGINT REFERENCES users ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_request PRIMARY KEY (request_id)
);

CREATE TABLE IF NOT EXISTS items
(
    item_id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                            NOT NULL,
    description VARCHAR(255)                            NOT NULL,
    is_available   BOOLEAN                                 NOT NULL,
    owner_id    BIGINT REFERENCES users ON DELETE CASCADE,
    request_id    BIGINT REFERENCES requests ON DELETE CASCADE,
    CONSTRAINT pk_item PRIMARY KEY (item_id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT REFERENCES items ON DELETE CASCADE,
    booker_id   BIGINT REFERENCES users ON DELETE CASCADE,
    status     VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (booking_id)
);

CREATE TABLE IF NOT EXISTS comments
(
    comment_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text VARCHAR(255)  NOT NULL,
    item_id    BIGINT REFERENCES items ON DELETE CASCADE,
    author_id   BIGINT REFERENCES users ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comment PRIMARY KEY (comment_id)
);
