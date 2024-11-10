CREATE TABLE "users"
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username   VARCHAR(255) UNIQUE                     NOT NULL,
    password   VARCHAR(255)                            NOT NULL,
    role       VARCHAR(255)                            NOT NULL,
    first_name VARCHAR(255)                            NOT NULL,
    last_name  VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

INSERT INTO "users" (username, password, role, first_name, last_name)
VALUES ('admin', '$2a$10$D5xFhOzmZbmKGtcrFToxxepQU7DoDY50Vdmfwyc0NDXC1ImaVcf/q', 'ROLE_ADMIN', 'Admin', 'Adminovich'),
       ('user', '$2a$10$kaAOdh6z/7JW4mpxU6fyJOhvVvcAhdfIlFWbmK5tJoZgSVQf2V5KS', 'ROLE_USER', 'User', 'Userovich'),
       ('boss', '$2a$10$NvX/R9PUS8EgfYC0.eWnEemAJ.o1hwzN2s.JAB5GVsdj8HavJgb4e', 'ROLE_ADMIN', 'Giga', 'Chad');