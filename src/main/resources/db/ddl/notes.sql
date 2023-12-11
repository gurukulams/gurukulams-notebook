CREATE TABLE annotation (
   id VARCHAR NOT NULL,
   on_type VARCHAR NOT NULL,
   on_instance VARCHAR NOT NULL,
   type VARCHAR NOT NULL,
   motivation VARCHAR(80),
   locale VARCHAR(8),
   body JSON NOT NULL,
   target JSON NOT NULL,
   PRIMARY KEY(id, on_type, on_instance)
);
