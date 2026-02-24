DROP  TABLE IF EXISTS bilheteria_anos_80;

CREATE TABLE bilheteria_anos_80 (
    id NUMBER PRIMARY KEY,
    titulo VARCHAR(255),
    ano NUMBER,
    bilheteria_mundial_usd NUMBER
);