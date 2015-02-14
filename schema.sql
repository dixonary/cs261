#Suspicious Activity Detection Tool#
#Schema#




DROP TABLE IF EXISTS Trade CASCADE;
DROP TABLE IF EXISTS Comm CASCADE;
DROP TABLE IF EXISTS Trader CASCADE;
DROP TABLE IF EXISTS Symbol CASCADE;
DROP TABLE IF EXISTS Sector CASCADE;

CREATE TABLE Trader (#TRADER TABLE#
  email  VARCHAR(50) NOT NULL, #Contains entities which represent individual Traders and averages for Trading volume and Profit
  domain VARCHAR(30) NOT NULL, #There email addresses are unique and so are used as our primary key
  Tavg1  INTEGER     NOT NULL DEFAULT 0,
  Tavg2  INTEGER     NOT NULL DEFAULT 0,
  Tavg3  INTEGER     NOT NULL DEFAULT 0,
  Pavg1  INTEGER     NOT NULL DEFAULT 0,
  Pavg2  INTEGER     NOT NULL DEFAULT 0,
  Pavg3  INTEGER     NOT NULL DEFAULT 0,
  PRIMARY KEY (email)
);


CREATE TABLE Symbol (#SYMBOL TABLE#
  name      VARCHAR(10) NOT NULL, #Contains entities which represent individual Stocks and averages for Trading volume and Profit
  price       FLOAT       NOT NULL DEFAULT 0, #Each Stock is unique on it's name/symbol
  totalTrades INTEGER     NOT NULL DEFAULT 0,
  Tavg1       INTEGER     NOT NULL DEFAULT 0,
  Tavg2       INTEGER     NOT NULL DEFAULT 0,
  Tavg3       INTEGER     NOT NULL DEFAULT 0,
  Pavg1       INTEGER     NOT NULL DEFAULT 0,
  Pavg2       INTEGER     NOT NULL DEFAULT 0,
  Pavg3       INTEGER     NOT NULL DEFAULT 0,
  PRIMARY KEY (name)
);

CREATE TABLE Sector (#SECTOR TABLE#
  name        VARCHAR(40) NOT NULL, #Contains entities which represent individual sectors and averages for Trading volume
  totalTrades INTEGER     NOT NULL DEFAULT 0, #Each sector is unique on it's name
  avg1        INTEGER     NOT NULL DEFAULT 0,
  avg2        INTEGER     NOT NULL DEFAULT 0,
  avg3        INTEGER     NOT NULL DEFAULT 0,
  PRIMARY KEY (name)
);

CREATE TABLE Trade (#TRADE TABLE#
  id       INTEGER     NOT NULL AUTO_INCREMENT, #Contains the full representation of a Trade as received from the Trades feed
  time     LONG        NOT NULL, #Also has an id as primary key as the timestamp cannot be guranteed to be unique
  buyer    VARCHAR(50) NOT NULL,
  seller   VARCHAR(50) NOT NULL,
  price    FLOAT       NOT NULL,
  size     INTEGER     NOT NULL,
  currency VARCHAR(3)  NOT NULL,
  symbol   VARCHAR(10) NOT NULL,
  sector   VARCHAR(40) NOT NULL,
  bid      FLOAT       NOT NULL,
  ask      FLOAT       NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (buyer) REFERENCES Trader (email),
  FOREIGN KEY (seller) REFERENCES Trader (email),
  FOREIGN KEY (symbol) REFERENCES Symbol (name),
  FOREIGN KEY (sector) REFERENCES Sector (name)
);

CREATE TABLE Comm (#COMM TABLE#
  id        INTEGER     NOT NULL AUTO_INCREMENT, #Contains individual Communcations between Traders
  time      LONG        NOT NULL, #Has an id field as the timestamp cannot be guaranteed to be unique
  sender    VARCHAR(50) NOT NULL,
  recipient VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (sender) REFERENCES Trader (email),
  FOREIGN KEY (recipient) REFERENCES Trader (email)
);

/*
DROP TABLE IF EXISTS CommLink CASCADE;
CREATE TABLE CommLink (#COMMLINK TABLE#
  trader1     VARCHAR(50) NOT NULL, #Contains the link between individual Communications and individual Traders
  trader2     VARCHAR(50) NOT NULL, #Tracks the history of communication between two traders in both directions and averages
  totalTrades INTEGER     NOT NULL, #Each entity is unique for each Trader pair
  avg1        INTEGER     NOT NULL,
  avg2        INTEGER     NOT NULL,
  avg3        INTEGER     NOT NULL,
  PRIMARY KEY (trader1, trader2),
  FOREIGN KEY (trader1) REFERENCES Trader (email),
  FOREIGN KEY (trader2) REFERENCES Trader (email)
);




DROP TABLE IF EXISTS Cluster CASCADE;
CREATE TABLE Cluster (#CLUSTER TABLE#
  clusterId INTEGER NOT NULL, #Links together a group of related clusters
  PRIMARY KEY (clusterId)          #Each entity is unique for each clusterId
);

DROP TABLE IF EXISTS ClusterFactor CASCADE;
CREATE TABLE ClusterFactor (#FACTORCLUSTER TABLE#
  clusterId INTEGER NOT NULL, #Linking table to associate multiple factors with one cluster
  factorId  INTEGER NOT NULL, #Entities are unique for each clusterId, factorId pair
  PRIMARY KEY (clusterId, factorId),
  FOREIGN KEY (clusterId) REFERENCES Cluster (clusterId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS Factor CASCADE;
CREATE TABLE Factor (#FACTOR TABLE#
  factorId INTEGER NOT NULL, #Exists to provide a unique value for each individual factor to refer to
  PRIMARY KEY (factorId)          #Each factorId is unique
);

DROP TABLE IF EXISTS TradeFactor CASCADE;
CREATE TABLE TradeFactor (#TRADEFACTOR TABLE#
  tradeId  INTEGER NOT NULL, #Linking table between the Factor and Trade tables
  factorId INTEGER NOT NULL, #Each entity is unique for a tradeId factorId pair
  PRIMARY KEY (tradeId, factorId),
  FOREIGN KEY (tradeId) REFERENCES Trade (id),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS CommFactor CASCADE;
CREATE TABLE CommFactor (#COMMFACTOR TABLE#
  commId   INTEGER NOT NULL, #Linking table between the Factor and Communication tables
  factorId INTEGER NOT NULL, #Each entity is unique for a commId factorId pair
  PRIMARY KEY (commId, factorId),
  FOREIGN KEY (commId) REFERENCES Communication (id),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS EmailsFactor CASCADE;
CREATE TABLE EmailsFactor (#EMAILSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Which traders are communicating with who"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  timeFrom DATETIME    NOT NULL,
  timeTo   DATETIME    NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS PortfoliosFactor CASCADE;
CREATE TABLE PortfoliosFactor (#PORTFOLIOSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Traders with similar portfolios"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  timeFrom DATETIME    NOT NULL,
  timeTo   DATETIME    NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS StylesFactor CASCADE;
CREATE TABLE StylesFactor (#STYLESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Traders with similar styles/trading patterns"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  timeFrom DATETIME    NOT NULL,
  timeTo   DATETIME    NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS SectorsFactor CASCADE;
CREATE TABLE SectorsFactor (#SECTORSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Active sectors"
  sector   VARCHAR(40) NOT NULL, #Unique for each factorId
  timeFrom DATETIME    NOT NULL,
  timeTo   DATETIME    NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS SymbolsFactor CASCADE;
CREATE TABLE SymbolsFactor (#SYMBOLSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Active Stocks (symbol)"
  symbol   VARCHAR(10) NOT NULL, #Unique for each factorId
  timeFrom DATETIME    NOT NULL,
  timeTo   DATETIME    NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS TradersFactor CASCADE;
CREATE TABLE TradersFactor (#TRADERSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Particularly active Traders"
  email    VARCHAR(50) NOT NULL, #Unique for each factorId
  timeFrom DATETIME    NOT NULL,
  timeTo   DATETIME    NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS PerformancesFactor CASCADE;
CREATE TABLE PerformancesFactor (#PERFORMANCESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Anomalous market performance for trader"
  email    VARCHAR(50) NOT NULL, #Unique for each factorId
  timeFrom DATETIME    NOT NULL,
  timeTo   DATETIME    NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS PricesFactor CASCADE;
CREATE TABLE PricesFactor (#PRICESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Sudden price changes of a stock"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  symbol   VARCHAR(10) NOT NULL,
  timeFrom DATETIME    NOT NULL,
  timeTo   DATETIME    NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);



DROP TABLE IF EXISTS StockOwnership CASCADE;
CREATE TABLE StockOwnership (#STOCKOWNERSHIP TABLE#
  symbol     INTEGER     NOT NULL, #Represents the volume of each stock that a Trader owns (can go negative!)
  email      VARCHAR(50) NOT NULL, #Each entity is unique for a symbol, email pair
  volume     INTEGER     NOT NULL,
  lastUpdate DATETIME    NOT NULL,
  PRIMARY KEY (symbol, email),
  FOREIGN KEY (symbol) REFERENCES Symbol (name),
  FOREIGN KEY (email) REFERENCES Trader (email)
);
*/
