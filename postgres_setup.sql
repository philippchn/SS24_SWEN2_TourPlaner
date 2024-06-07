CREATE DATABASE DB_TourPlanner;

\c DB_TourPlanner

DROP TABLE t_tourLogs;
DROP TABLE t_tours;

CREATE TABLE t_tours (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(255),
    "tour_description" VARCHAR(255),
    "start_location" VARCHAR(255),
    "end_location" VARCHAR(255),
    "transport_type" VARCHAR(255),
    "distance" VARCHAR(255),
    "estimated_time" VARCHAR(255),
    "route_information" VARCHAR(255)
);

CREATE TABLE t_tourLogs (
	"id" SERIAL PRIMARY KEY,
	"date" VARCHAR(255),
	"comment" VARCHAR(255),
	"difficulty" VARCHAR(255),
	"total_distance" VARCHAR(255),
	"total_time" VARCHAR(255),
	"rating" VARCHAR(255),
	"fk_tour" INTEGER REFERENCES t_tours(id)
);



