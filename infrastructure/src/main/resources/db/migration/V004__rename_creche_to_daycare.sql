-- Rename service type from Portuguese "CRECHE" to English "DAYCARE"
UPDATE appointments SET service_type = 'DAYCARE' WHERE service_type = 'CRECHE';

