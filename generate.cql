DROP TABLE IF EXISTS test.log;
DROP TABLE IF EXISTS test.drone;
DROP KEYSPACE IF EXISTS test;

CREATE KEYSPACE test WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 };

CREATE TABLE test.drone (
  id    INT,
  brand VARCHAR,

  PRIMARY KEY (id)
);

CREATE TABLE test.log (
  id          INT,
  id_drone    INT,
  speed       FLOAT,
  altitude    DOUBLE,
  latitude    DOUBLE,
  longitude   DOUBLE,
  datetime    VARCHAR,
  temperature INT,
  battery     FLOAT,

  PRIMARY KEY (id)
);
