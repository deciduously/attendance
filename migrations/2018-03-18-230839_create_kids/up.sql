CREATE TABLE IF NOT EXISTS kids (
       id integer PRIMARY KEY UNIQUE,
       full_name text NOT NULL,
       letter text NOT NULL,
       mon text,
       tue text,
       wed text,
       thu text,
       fri text
);
