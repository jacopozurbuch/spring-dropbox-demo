-- Table: public.users

-- DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    id SERIAL NOT NULL UNIQUE,
    name text,
    username text NOT NULL UNIQUE,
    birthday date NOT NULL,
    email text,
    CONSTRAINT users_pkey PRIMARY KEY (username)
);

CREATE TABLE IF NOT EXISTS public.password
(
  username text NOT NULL,
  salt bytea NOT NULL,
  hash bytea NOT NULL,
  PRIMARY KEY (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

ALTER TABLE IF EXISTS public.password
    OWNER to postgres;