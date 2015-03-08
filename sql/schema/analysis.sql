/*
aggregations happen at beginning of a tick and are no longer valid by its end
 */

SET FOREIGN_KEY_CHECKS = 0;


DROP TABLE IF EXISTS Poisson CASCADE;
CREATE TABLE Poisson (#COUNTER TABLE#
  lambda DOUBLE  NOT NULL,
  x      INTEGER NOT NULL, #Counters for all other tables are stored here
  pdf    DOUBLE  NOT NULL DEFAULT 0, # P(X = x)
  cdf    DOUBLE  NOT NULL DEFAULT 0, # P(X <= x)
  sig    DOUBLE  NOT NULL DEFAULT 0, # P(X >= x)
  PRIMARY KEY (lambda, x)
);


DROP TABLE IF EXISTS TickFactor CASCADE;
CREATE TABLE TickFactor (
  id      INTEGER        NOT NULL AUTO_INCREMENT,
  tick    INTEGER        NOT NULL,
  edge    INTEGER        NOT NULL,
#weight FLOAT          NOT NULL DEFAULT 0,
  factor  ENUM ('COMMON',
                'COMMON_BUYS',
                'COMMON_SELLS',
                'COMMS') NOT NULL,
  value   INTEGER        NOT NULL,
  centile DOUBLE         NOT NULL,
  sig     DOUBLE         NOT NULL,

  PRIMARY KEY (id),
  UNIQUE KEY (tick, edge, factor),
  FOREIGN KEY (tick) REFERENCES Tick (tick),
  FOREIGN KEY (edge) REFERENCES Edge (id)#,
  #FOREIGN KEY (tradeCnt) REFERENCES Counter (id)
);


DROP TABLE IF EXISTS Factor CASCADE;
CREATE TABLE Factor (
  id      INTEGER        NOT NULL AUTO_INCREMENT,
  tick    INTEGER        NOT NULL,
  edge    INTEGER        NOT NULL,
#weight FLOAT          NOT NULL DEFAULT 0,
  factor  ENUM ('COMMON',
                'COMMON_BUYS',
                'COMMON_SELLS',
                'COMMS') NOT NULL,
  value   INTEGER        NOT NULL,
  centile DOUBLE         NOT NULL,
  sig     DOUBLE         NOT NULL,

  score   DOUBLE         NOT NULL,

  PRIMARY KEY (id),
  UNIQUE KEY (tick, edge, factor),
  FOREIGN KEY (tick) REFERENCES Tick (tick),
  FOREIGN KEY (edge) REFERENCES Edge (id)#,
  #FOREIGN KEY (tradeCnt) REFERENCES Counter (id)
);


DROP TABLE IF EXISTS FactorFreq CASCADE;
CREATE TABLE FactorFreq (
  tick   INTEGER        NOT NULL,
#weight FLOAT          NOT NULL DEFAULT 0,
  factor ENUM ('COMMON',
               'COMMON_BUYS',
               'COMMON_SELLS',
               'COMMS') NOT NULL,
  x      INTEGER        NOT NULL,
  fx     INTEGER        NOT NULL,

  UNIQUE KEY (tick, factor, x)
#FOREIGN KEY (tradeCnt) REFERENCES Counter (id)
);


DROP TABLE IF EXISTS Cluster CASCADE;
CREATE TABLE Cluster (
  id    INTEGER NOT NULL AUTO_INCREMENT,
  tick  INTEGER NOT NULL,

  nodes INTEGER NOT NULL,
  edges INTEGER NOT NULL,

  meta  TEXT,

  status  ENUM('UNSEEN', 'SEEN', 'INVESTIGATED'),

  PRIMARY KEY (id),
  FOREIGN KEY (tick) REFERENCES Tick (tick)
);

DROP TABLE IF EXISTS ClusterNode CASCADE;
CREATE TABLE ClusterNode (
  cluster INTEGER NOT NULL,
  node    INTEGER NOT NULL,

  PRIMARY KEY (cluster, node),
  FOREIGN KEY (cluster) REFERENCES Cluster (id),
  FOREIGN KEY (node) REFERENCES Node (id)
);

DROP TABLE IF EXISTS ClusterEdge CASCADE;
CREATE TABLE ClusterEdge (
  cluster INTEGER NOT NULL,
  edge    INTEGER NOT NULL,
  weight  DOUBLE  NOT NULL,

  meta    TEXT,

  PRIMARY KEY (cluster, edge),
  FOREIGN KEY (cluster) REFERENCES Cluster (id),
  FOREIGN KEY (edge) REFERENCES Edge (id)
);


DROP TABLE IF EXISTS TickClusterEdge CASCADE;
CREATE TABLE TickClusterEdge (
  tick   INTEGER NOT NULL,
  edge   INTEGER NOT NULL,
  weight DOUBLE  NOT NULL,

  PRIMARY KEY (tick, edge),
  FOREIGN KEY (tick) REFERENCES Tick (tick),
  FOREIGN KEY (edge) REFERENCES Edge (id)
);


SET FOREIGN_KEY_CHECKS = 1;
