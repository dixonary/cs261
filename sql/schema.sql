#Suspicious Activity Detection Tool#
#Schema#

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Prop CASCADE;
CREATE TABLE Prop (
  `key` VARCHAR(40)  NOT NULL,
  value VARCHAR(256) NOT NULL,
  PRIMARY KEY (`key`)
);

# statistics
DROP TABLE IF EXISTS Counter CASCADE;
CREATE TABLE Counter (#RAWTRADE TABLE#
  id   INTEGER NOT NULL AUTO_INCREMENT, #Contains the full representation of a Trade as received from the Trades feed
  val  INTEGER NOT NULL DEFAULT 0,
  avg1 FLOAT   NOT NULL DEFAULT 0,
  avg2 FLOAT   NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS Tick CASCADE;
CREATE TABLE Tick (#RAWTRADE TABLE#
  tick         INTEGER NOT NULL, #Contains the full representation of a Trade as received from the Trades feed
  trades       INTEGER NOT NULL,
  comms        INTEGER NOT NULL,
  analysed     BOOLEAN NOT NULL DEFAULT FALSE,
  analysisTime INT     NOT NULL DEFAULT 0,
  PRIMARY KEY (tick)
);


# raw data tables
DROP TABLE IF EXISTS RawEvent CASCADE;
CREATE TABLE RawEvent (#RAWTRADE TABLE#
  id   INTEGER                NOT NULL AUTO_INCREMENT, #Contains the full representation of a Trade as received from the Trades feed
  type ENUM ('TRADE', 'COMM') NOT NULL,
  time BIGINT                 NOT NULL,
  raw  TEXT                   NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS RawTrade CASCADE;
CREATE TABLE RawTrade (#RAWTRADE TABLE#
  id  INTEGER NOT NULL AUTO_INCREMENT, #Contains the full representation of a Trade as received from the Trades feed
  raw TEXT    NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS RawComm CASCADE;
CREATE TABLE RawComm (#RAWCOMM TABLE#
  id  INTEGER NOT NULL AUTO_INCREMENT, #Contains individual Communcations between Traders
  raw TEXT    NOT NULL,
  PRIMARY KEY (id)
);


# nodes
DROP TABLE IF EXISTS Node CASCADE;
CREATE TABLE Node (
  id INTEGER NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS Trader CASCADE;
CREATE TABLE Trader (#TRADER TABLE#
  id       INTEGER     NOT NULL,
  email    VARCHAR(50) NOT NULL, #Contains entities which represent individual Traders and averages for Trading volume and Profit
  domain   VARCHAR(30) NOT NULL, #There email addresses are unique and so are used as our primary key
  tradeCnt INTEGER     NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (email),
  FOREIGN KEY (id) REFERENCES Node (id)#,
  #FOREIGN KEY (tradeCnt) REFERENCES Counter (id)
);

DROP TABLE IF EXISTS Symbol CASCADE;
CREATE TABLE Symbol (#SYMBOL TABLE#
  id       INTEGER     NOT NULL,
  symbol   VARCHAR(10) NOT NULL, #Contains entities which represent individual Stocks and averages for Trading volume and Profit
  sector   VARCHAR(40) NOT NULL, #Each Stock is unique on it's name/symbol
  price    FLOAT       NOT NULL DEFAULT 0,
  tradeCnt INTEGER     NOT NULL,
  priceCnt INTEGER     NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES Node (id),
  UNIQUE KEY (symbol),
  FOREIGN KEY (sector) REFERENCES Sector (sector)
#FOREIGN KEY (tradeCnt) REFERENCES Counter (id),
#FOREIGN KEY (priceCnt) REFERENCES Counter (id)
);

DROP TABLE IF EXISTS Sector CASCADE;
CREATE TABLE Sector (#SECTOR TABLE#
  id       INTEGER     NOT NULL,
  sector   VARCHAR(40) NOT NULL, #Contains entities which represent individual sectors and averages for Trading volume
  tradeCnt INTEGER     NOT NULL, #Each sector is unique on it's name
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES Node (id),
  UNIQUE KEY (sector)#,
  #FOREIGN KEY (tradeCnt) REFERENCES Counter (id)
);

DROP TABLE IF EXISTS Trade CASCADE;
CREATE TABLE Trade (#TRADE TABLE#
  id       INTEGER     NOT NULL AUTO_INCREMENT, #Contains the full representation of a Trade as received from the Trades feed
  time     BIGINT      NOT NULL, #Also has an id as primary key as the timestamp cannot be guranteed to be unique
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
  FOREIGN KEY (symbol) REFERENCES Symbol (symbol),
  FOREIGN KEY (sector) REFERENCES Sector (sector)
);

DROP TABLE IF EXISTS Comm CASCADE;
CREATE TABLE Comm (#COMM TABLE#
  id        INTEGER     NOT NULL AUTO_INCREMENT, #Contains individual Communcations between Traders
  time      BIGINT      NOT NULL, #Has an id field as the timestamp cannot be guaranteed to be unique
  sender    VARCHAR(50) NOT NULL,
  recipient VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (sender) REFERENCES Trader (email),
  FOREIGN KEY (recipient) REFERENCES Trader (email)
);

DROP TABLE IF EXISTS TraderStock CASCADE;
CREATE TABLE TraderStock (#TRADERSTOCK TABLE#
  id         INTEGER     NOT NULL AUTO_INCREMENT, #Each entity is unique for a symbol, Trader pair
  email      VARCHAR(50) NOT NULL, #Represents the volume of each stock that a Trader owns (can go negative!)
  symbol     VARCHAR(10) NOT NULL,
  volume     INTEGER     NOT NULL DEFAULT 0,
  lastUpdate BIGINT      NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (email, symbol),
  FOREIGN KEY (email) REFERENCES Trader (email),
  FOREIGN KEY (symbol) REFERENCES Symbol (symbol)
);

# edges

DROP TABLE IF EXISTS Edge CASCADE;
CREATE TABLE Edge (#FACTOR TABLE#
  id     INTEGER NOT NULL AUTO_INCREMENT, #Exists to provide a unique value for each individual factor to refer to
  source INTEGER,
  target INTEGER,
  weight FLOAT,
  PRIMARY KEY (id),
  UNIQUE KEY (source, target),
  FOREIGN KEY (source) REFERENCES Node (id),
  FOREIGN KEY (target) REFERENCES Node (id)
);

DROP TABLE IF EXISTS TraderTraderEdge CASCADE;
CREATE TABLE TraderTraderEdge (#TRADERPAIR TABLE#
  id       INTEGER     NOT NULL AUTO_INCREMENT,
  trader1  VARCHAR(50) NOT NULL, #Each entity is unique for a Trader pair
  trader2  VARCHAR(50) NOT NULL, #Represents the stocks in common, Trades and Communications between Trader pairs
  # counts for last time interval
  comms    INTEGER     NOT NULL, #comms exchanged
  stocks   INTEGER     NOT NULL, #common stocks traded
#  stockWgt INTEGER     NOT NULL,


  stockWgt FLOAT       NOT NULL,
  tradeWgt FLOAT       NOT NULL,
  commWgt  FLOAT       NOT NULL,

  stockCnt INTEGER     NOT NULL, #number of stocks in common
  tradeCnt INTEGER     NOT NULL, #number of trades between them
  commCnt  INTEGER     NOT NULL, #number of communications between them
  PRIMARY KEY (id),
  UNIQUE KEY (trader1, trader2),
  FOREIGN KEY (trader1) REFERENCES Trader (email),
  FOREIGN KEY (trader2) REFERENCES Trader (email),
  FOREIGN KEY (id) REFERENCES Edge (id)
#,
#FOREIGN KEY (stockCnt) REFERENCES Counter (id),
#FOREIGN KEY (tradeCnt) REFERENCES Counter (id),
#FOREIGN KEY (commCnt) REFERENCES Counter (id)
);

DROP TABLE IF EXISTS TraderSymbolEdge CASCADE;
CREATE TABLE TraderSymbolEdge (#TRADERPAIR TABLE#
  id       INTEGER     NOT NULL AUTO_INCREMENT,
  email    VARCHAR(50) NOT NULL, #Each entity is unique for a Trader pair
  symbol   VARCHAR(10) NOT NULL, #Represents the stocks in common, Trades and Communications between Trader pairs
  # edge weights
  tradeWgt FLOAT       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (email, symbol),
  FOREIGN KEY (email) REFERENCES Trader (email),
  FOREIGN KEY (symbol) REFERENCES Symbol (symbol),
  FOREIGN KEY (id) REFERENCES Edge (id)
);

DROP TABLE IF EXISTS TraderSectorEdge CASCADE;
CREATE TABLE TraderSectorEdge (#TRADERPAIR TABLE#
  id       INTEGER     NOT NULL AUTO_INCREMENT,
  email    VARCHAR(50) NOT NULL, #Each entity is unique for a Trader pair
  sector   VARCHAR(40) NOT NULL, #Represents the stocks in common, Trades and Communications between Trader pairs
  # edge weights
  tradeWgt FLOAT       NOT NULL, # represents strength of trader activity in sector
  PRIMARY KEY (id),
  UNIQUE KEY (email, sector),
  FOREIGN KEY (email) REFERENCES Trader (email),
  FOREIGN KEY (sector) REFERENCES Sector (sector),
  FOREIGN KEY (id) REFERENCES Edge (id)
);


# clustering

DROP TABLE IF EXISTS Cluster CASCADE;
CREATE TABLE Cluster (#CLUSTER TABLE#
  clusterId INTEGER NOT NULL AUTO_INCREMENT, #Links together a group of related factors
  time      BIGINT  NOT NULL,
  day       BIGINT  NOT NULL,
  PRIMARY KEY (clusterId)          #Each entity is unique for each clusterId
);

DROP TABLE IF EXISTS Factor CASCADE;
CREATE TABLE Factor (#FACTOR TABLE#
  factorId INTEGER NOT NULL AUTO_INCREMENT, #Exists to provide a unique value for each individual factor to refer to
  #time   BIGINT  NOT NULL,
  #weight INT     NOT NULL,
  timeFrom BIGINT  NOT NULL,
  timeTo   BIGINT  NOT NULL,
  PRIMARY KEY (factorId)          #Each factorId is unique
);

DROP TABLE IF EXISTS ClusterFactor CASCADE;
CREATE TABLE ClusterFactor (#FACTORCLUSTER TABLE#
  clusterId INTEGER NOT NULL, #Linking table to associate multiple factors with one cluster
  factorId  INTEGER NOT NULL, #Entities are unique for each clusterId, factorId pair
  PRIMARY KEY (clusterId, factorId),
  FOREIGN KEY (clusterId) REFERENCES Cluster (clusterId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
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
  FOREIGN KEY (commId) REFERENCES Comm (id),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS EmailsFactor CASCADE;
CREATE TABLE EmailsFactor (#EMAILSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Which traders are communicating with who"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS PortfoliosFactor CASCADE;
CREATE TABLE PortfoliosFactor (#PORTFOLIOSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Traders with similar portfolios"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS StylesFactor CASCADE;
CREATE TABLE StylesFactor (#STYLESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Traders with similar styles/trading patterns"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS SectorsFactor CASCADE;
CREATE TABLE SectorsFactor (#SECTORSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Active sectors"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  sector   VARCHAR(40) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS SymbolsFactor CASCADE;
CREATE TABLE SymbolsFactor (#SYMBOLSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Active Stocks (symbol)"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  symbol   VARCHAR(10) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS TradersFactor CASCADE;
CREATE TABLE TradersFactor (#TRADERSFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Particularly active Traders"
  email    VARCHAR(50) NOT NULL, #Unique for each factorId
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS PerformancesFactor CASCADE;
CREATE TABLE PerformancesFactor (#PERFORMANCESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Anomalous market performance for trader"
  email    VARCHAR(50) NOT NULL, #Unique for each factorId
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);

DROP TABLE IF EXISTS PricesFactor CASCADE;
CREATE TABLE PricesFactor (#PRICESFACTOR TABLE#
  factorId INTEGER     NOT NULL, #Represents the factor "Sudden price changes of a stock"
  email1   VARCHAR(50) NOT NULL, #Unique for each factorId
  email2   VARCHAR(50) NOT NULL,
  symbol   VARCHAR(10) NOT NULL,
  PRIMARY KEY (factorId),
  FOREIGN KEY (factorId) REFERENCES Factor (factorId)
);


SET FOREIGN_KEY_CHECKS = 1;