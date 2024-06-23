CREATE DATABASE DB_TourPlanner;

\c DB_TourPlanner

DROP TABLE IF EXISTS t_tourLogs;
DROP TABLE IF EXISTS t_tours;

CREATE TABLE t_tours (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    tour_description VARCHAR(255),
    start_location VARCHAR(255),
    end_location VARCHAR(255),
    transport_type VARCHAR(255),
    distance BIGINT,
    estimated_time VARCHAR(255),
    route_information TEXT
);

CREATE TABLE t_tourLogs (
    id SERIAL PRIMARY KEY,
    date DATE,
    comment VARCHAR(255),
    difficulty INTEGER,
    total_distance DOUBLE PRECISION,
    total_time BIGINT,
    rating INTEGER,
    fk_tour INTEGER REFERENCES t_tours(id)
);
