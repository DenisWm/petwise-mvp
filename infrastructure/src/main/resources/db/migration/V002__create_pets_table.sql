-- Create pets table
CREATE TABLE IF NOT EXISTS pets (
    id          VARCHAR(36)  PRIMARY KEY,
    tutor_id    VARCHAR(36)  NOT NULL,
    name        VARCHAR(255) NOT NULL,
    species     VARCHAR(255),
    breed       VARCHAR(255),
    birth_date  DATE,
    notes       TEXT,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    CONSTRAINT fk_pets_tutor FOREIGN KEY (tutor_id) REFERENCES tutors(id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_pets_tutor_id   ON pets(tutor_id);
CREATE INDEX IF NOT EXISTS idx_pets_name        ON pets(name);
CREATE INDEX IF NOT EXISTS idx_pets_created_at  ON pets(created_at);

