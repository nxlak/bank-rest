CREATE SEQUENCE IF NOT EXISTS users_seq
  START WITH 1
  INCREMENT BY 50
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

CREATE SEQUENCE IF NOT EXISTS card_seq
  START WITH 1
  INCREMENT BY 50
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

CREATE SEQUENCE IF NOT EXISTS transaction_seq
  START WITH 1
  INCREMENT BY 50
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS card (
    id BIGINT PRIMARY KEY DEFAULT nextval('card_seq'),
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    card_number VARCHAR(50) NOT NULL UNIQUE,
    expiry VARCHAR(7) NOT NULL,
    status VARCHAR(20) NOT NULL,
    balance NUMERIC(19,2) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_card_user_id ON card(user_id);
CREATE INDEX IF NOT EXISTS idx_card_number ON card(card_number);

CREATE TABLE IF NOT EXISTS transaction (
    id BIGINT PRIMARY KEY DEFAULT nextval('transaction_seq'),
    from_card_id BIGINT NOT NULL REFERENCES card(id) ON DELETE CASCADE,
    to_card_id   BIGINT NOT NULL REFERENCES card(id) ON DELETE CASCADE,
    amount       NUMERIC(19,2) NOT NULL,
    timestamp    TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_transaction_from_card ON transaction(from_card_id);
CREATE INDEX IF NOT EXISTS idx_transaction_to_card   ON transaction(to_card_id);