#Suspicious Activity Detection Tool#
#Schema#

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS RawTrade CASCADE;
DROP TABLE IF EXISTS RawComm CASCADE;
DROP TABLE IF EXISTS Trader CASCADE;
DROP TABLE IF EXISTS Symbol CASCADE;
DROP TABLE IF EXISTS Sector CASCADE;
DROP TABLE IF EXISTS Trade CASCADE;
DROP TABLE IF EXISTS Comm CASCADE;
DROP TABLE IF EXISTS CommLink CASCADE;
DROP TABLE IF EXISTS StockOwnership CASCADE;

DROP TABLE IF EXISTS TraderTrader CASCADE;
DROP TABLE IF EXISTS ClusterFactor CASCADE;
DROP TABLE IF EXISTS Factor CASCADE;
DROP TABLE IF EXISTS Cluster CASCADE;

DROP TABLE IF EXISTS TradeFactor CASCADE;
DROP TABLE IF EXISTS CommFactor CASCADE;
DROP TABLE IF EXISTS EmailsFactor CASCADE;
DROP TABLE IF EXISTS PortfoliosFactor CASCADE;
DROP TABLE IF EXISTS StylesFactor CASCADE;
DROP TABLE IF EXISTS SectorsFactor CASCADE;
DROP TABLE IF EXISTS SymbolsFactor CASCADE;
DROP TABLE IF EXISTS TradersFactor CASCADE;
DROP TABLE IF EXISTS PerformancesFactor CASCADE;
DROP TABLE IF EXISTS PricesFactor CASCADE;

SET FOREIGN_KEY_CHECKS = 1;

# raw data tables

CREATE TABLE RawTrade (#RAWTRADE TABLE#
  id  INTEGER NOT NULL AUTO_INCREMENT, #Contains the full representation of a Trade as received from the Trades feed
  raw TEXT    NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE RawComm (#RAWCOMM TABLE#
  id  INTEGER NOT NULL AUTO_INCREMENT, #Contains individual Communcations between Traders
  raw TEXT    NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE Trader (#TRADER TABLE#
  email       VARCHAR(50) NOT NULL, #Contains entities which represent individual Traders and averages for Trading volume and Profit
  domain      VARCHAR(30) NOT NULL, #There email addresses are unique and so are used as our primary key
  totalTrades INTEGER     NOT NULL DEFAULT 0,
  tAvg1       INTEGER     NOT NULL DEFAULT 0,
  tAvg2       INTEGER     NOT NULL DEFAULT 0,
  tAvg3       INTEGER     NOT NULL DEFAULT 0,
  pAvg1       INTEGER     NOT NULL DEFAULT 0,
  pAvg2       INTEGER     NOT NULL DEFAULT 0,
  pAvg3       INTEGER     NOT NULL DEFAULT 0,
  PRIMARY KEY (email)
);

CREATE TABLE Symbol (#SYMBOL TABLE#
  name        VARCHAR(10) NOT NULL, #Contains entities which represent individual Stocks and averages for Trading volume and Profit
  price       FLOAT       NOT NULL DEFAULT 0, #Each Stock is unique on it's name/symbol
  totalTrades INTEGER     NOT NULL DEFAULT 0,
  tAvg1       INTEGER     NOT NULL DEFAULT 0,
  tAvg2       INTEGER     NOT NULL DEFAULT 0,
  tAvg3       INTEGER     NOT NULL DEFAULT 0,
  pAvg1       INTEGER     NOT NULL DEFAULT 0,
  pAvg2       INTEGER     NOT NULL DEFAULT 0,
  pAvg3       INTEGER     NOT NULL DEFAULT 0,
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

CREATE TABLE CommLink (#COMMLINK TABLE#
  trader1     VARCHAR(50) NOT NULL, #Contains the link between individual Communications and individual Traders
  trader2     VARCHAR(50) NOT NULL, #Tracks the history of communication between two traders in both directions and averages
  totalTrades INTEGER     NOT NULL DEFAULT 0, #Each entity is unique for each Trader pair
  avg1        INTEGER     NOT NULL DEFAULT 0,
  avg2        INTEGER     NOT NULL DEFAULT 0,
  avg3        INTEGER     NOT NULL DEFAULT 0,
  PRIMARY KEY (trader1, trader2),
  FOREIGN KEY (trader1) REFERENCES Trader (email),
  FOREIGN KEY (trader2) REFERENCES Trader (email)
);

CREATE TABLE StockOwnership (#STOCKOWNERSHIP TABLE#
  symbol     VARCHAR(10) NOT NULL, #Represents the volume of each stock that a Trader owns (can go negative!)
  email      VARCHAR(50) NOT NULL, #Each entity is unique for a symbol, Trader pair
  volume     INTEGER     NOT NULL DEFAULT 0,
  lastUpdate LONG        NOT NULL,
  PRIMARY KEY (symbol, email),
  FOREIGN KEY (symbol) REFERENCES Symbol (name),
  FOREIGN KEY (email)  REFERENCES Trader (email)
);

# analysis results

CREATE TABLE TraderTrader (#TRADERTRADER TABLE#
  trader1     VARCHAR(50) NOT NULL, #Represents the number of Stocks a pair of Traders have in common
  trader2     VARCHAR(50) NOT NULL, #Each entity is unique for each Trader pair
  totalStocks INTEGER     NOT NULL DEFAULT 0, 
  avg1        INTEGER     NOT NULL DEFAULT 0,
  avg2        INTEGER     NOT NULL DEFAULT 0,
  avg3        INTEGER     NOT NULL DEFAULT 0,
  PRIMARY KEY (trader1, trader2),
  FOREIGN KEY (trader1) REFERENCES Trader (email),
  FOREIGN KEY (trader2) REFERENCES Trader (email)
);

CREATE TABLE Cluster (#CLUSTER TABLE#
  clusterId INTEGER NOT NULL AUTO_INCREMENT, #Links together a group of related factors
  time      LONG    NOT NULL,
  day       LONG    NOT NULL,
  PRIMARY KEY (clusterId)          #Each entity is unique for each clusterId
);

CREATE TABLE Factor (#FACTOR TABLE#
  factorId INTEGER NOT NULL AUTO_INCREMENT, #Exists to provide a unique value for each individual factor to refer to
  timeFrom LONG    NOT NULL,
  timeTo   LONG    NOT NULL,
  PRIMARY KEY (factorId)          #Each factorId is unique
);

CREATE TABLE ClusterFactor (#FACTORCLUSTER TABLE#
  clusterId INTEGER NOT NULL, #Linking table to associate multiple factors with one cluster
  factorId  INTEGER NOT NULL, #Entities are unique for each clusterId, factorId pair
  PRIMARY KEY (clusterId, factorId),
  FOREIGN KEY (clusterId) REFERENCES Cluster (clusterId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE TradeFactor (#TRADEFACTOR TABLE#
  tradeId  INTEGER NOT NULL, #Linking table between the Factor and Trade tables
  factorId INTEGER NOT NULL, #Each entity is unique for a tradeId factorId pair
  PRIMARY KEY (tradeId, factorId),
  FOREIGN KEY (tradeId)  REFERENCES Trade (id),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE CommFactor (#COMMFACTOR TABLE#
  commId   INTEGER NOT NULL, #Linking table between the Factor and Communication tables
  factorId INTEGER NOT NULL, #Each entity is unique for a commId factorId pair
  PRIMARY KEY (commId, factorId),
  FOREIGN KEY (commId)   REFERENCES Comm (id),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE EmailsFactor (#EMAILSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Which traders are communicating with who"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE PortfoliosFactor (#PORTFOLIOSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Traders with similar portfolios"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE StylesFactor (#STYLESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Traders with similar styles/trading patterns"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE SectorsFactor (#SECTORSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Active sectors"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  sector   VARCHAR(40) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE SymbolsFactor (#SYMBOLSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Active Stocks (symbol)"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  symbol   VARCHAR(10) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE TradersFactor (#TRADERSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Particularly active Traders"
  email    VARCHAR(50) NOT NULL, #Unique for each factorId
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE PerformancesFactor (#PERFORMANCESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Anomalous market performance for trader"
  email    VARCHAR(50) NOT NULL, #Unique for each factorId
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

CREATE TABLE PricesFactor (#PRICESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Sudden price changes of a stock"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  symbol   VARCHAR(10) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);