-- Create appointments table
CREATE TABLE IF NOT EXISTS appointments (
    id           VARCHAR(36)  PRIMARY KEY,
    pet_id       VARCHAR(36)  NOT NULL,
    service_type VARCHAR(50)  NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    start_at     TIMESTAMP    NOT NULL,
    end_at       TIMESTAMP    NOT NULL,
    notes        TEXT,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NOT NULL,
    CONSTRAINT fk_appointments_pet FOREIGN KEY (pet_id) REFERENCES pets(id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_appointments_pet_id      ON appointments(pet_id);
CREATE INDEX IF NOT EXISTS idx_appointments_status      ON appointments(status);
CREATE INDEX IF NOT EXISTS idx_appointments_start_at    ON appointments(start_at);
CREATE INDEX IF NOT EXISTS idx_appointments_created_at  ON appointments(created_at);

