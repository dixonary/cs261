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
CREATE TABLE Counter (#COUNTER TABLE#
  id   INTEGER NOT NULL AUTO_INCREMENT, #Counters for all other tables are stored here
  val  INTEGER NOT NULL DEFAULT 0,
  avg1 FLOAT   NOT NULL DEFAULT 0,
  avg2 FLOAT   NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);


DROP TABLE IF EXISTS Counts CASCADE;
CREATE TABLE Counts (
  intvl        INT NOT NULL,
  time         BIGINT    NOT NULL, #realtime
  sealed        BOOL NOT NULL ,

  tradesRead   INTEGER NOT NULL DEFAULT 0,
  commsRead    INTEGER NOT NULL DEFAULT 0, # comms for the interval
  tradesParsed INTEGER NOT NULL DEFAULT 0,
  commsParsed  INTEGER NOT NULL DEFAULT 0, # comms for the interval
  PRIMARY KEY (intvl)
);


DROP TABLE IF EXISTS Tick CASCADE;
CREATE TABLE Tick (#RAWTRADE TABLE#
  tick               INTEGER NOT NULL, #Contains the full representation of a Trade as received from the Trades feed
  start              BIGINT  NOT NULL,
  end                BIGINT  NOT NULL,
  status             ENUM
                     ('UNPARSED',
                      'PARSED',
                      'ANALYSING',
                      'ANALYSED',
                      'CLUSTERED')
                             NOT NULL DEFAULT 'UNPARSED',

# counts
  trades             INTEGER NOT NULL DEFAULT 0,
  comms              INTEGER NOT NULL DEFAULT 0, # comms for the interval

#


  common             INTEGER NOT NULL,
  commonBuys         INTEGER NOT NULL,
  commonSells        INTEGER NOT NULL,

# frequencies
  tradesPerTrader    DOUBLE  NOT NULL,

  commonPerPair      DOUBLE  NOT NULL,
  commonBuysPerPair  DOUBLE  NOT NULL,
  commonSellsPerPair DOUBLE  NOT NULL,


  commsPerTrader     DOUBLE  NOT NULL DEFAULT 0, # average trader comm frequency
  commsPerPair       DOUBLE  NOT NULL DEFAULT 0, # average pair-wise comm frequency



# means
  commsMa            FLOAT   NOT NULL DEFAULT 0, # comms moving average
  tradesMa           FLOAT   NOT NULL DEFAULT 0,


  aggrTime           INT,
  analysisTime       INT     NOT NULL DEFAULT 0,

  mclInput           BLOB,

  clusters           BLOB,
  clusters2          BLOB,
  clusters3          BLOB,
  PRIMARY KEY (tick)
);

DROP TABLE IF EXISTS TickEdge CASCADE;
CREATE TABLE TickEdge (#TRADER TABLE#
  tick   INTEGER NOT NULL,
  edge   INTEGER NOT NULL, #Contains entities which represent individual Traders and averages for Trading volume and Profit
  weight FLOAT   NOT NULL,
  PRIMARY KEY (tick, edge),
  FOREIGN KEY (tick) REFERENCES Tick (tick),
  FOREIGN KEY (edge) REFERENCES Edge (id)#,
  #FOREIGN KEY (tradeCnt) REFERENCES Counter (id)
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


DROP TABLE IF EXISTS Trade CASCADE;
CREATE TABLE Trade (#TRADE TABLE#
  id       INTEGER     NOT NULL AUTO_INCREMENT, #Contains the full representation of a Trade with each field separated out
  time     BIGINT      NOT NULL, #Also has an id as primary key as the timestamp cannot be guranteed to be unique
  timestamp VARCHAR(50) NOT NULL,
  tick     INTEGER     NOT NULL,
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
  FOREIGN KEY (tick) REFERENCES Tick (tick),
  FOREIGN KEY (buyer) REFERENCES Trader (email),
  FOREIGN KEY (seller) REFERENCES Trader (email),
  FOREIGN KEY (symbol) REFERENCES Symbol (symbol),
  FOREIGN KEY (sector) REFERENCES Sector (sector),

#references
  buyerId  INT         NOT NULL,
  sellerId INT         NOT NULL,
  symbolId INT         NOT NULL,
  sectorId INT         NOT NULL,

  FOREIGN KEY (buyerId) REFERENCES Trader (id),
  FOREIGN KEY (sellerId) REFERENCES Trader (id),
  FOREIGN KEY (symbolId) REFERENCES Symbol (id),
  FOREIGN KEY (sectorId) REFERENCES Sector (id)
);

DROP TABLE IF EXISTS Comm CASCADE;
CREATE TABLE Comm (#COMM TABLE#
  id          INTEGER     NOT NULL AUTO_INCREMENT, #Contains individual Communcations between Traders
  time        BIGINT      NOT NULL, #Has an id field as the timestamp cannot be guaranteed to be unique
  timestamp VARCHAR(50) NOT NULL,
  tick        INTEGER     NOT NULL,
  sender      VARCHAR(50) NOT NULL,
  recipient   VARCHAR(50) NOT NULL,

  PRIMARY KEY (id),
  FOREIGN KEY (tick) REFERENCES Tick (tick),
  FOREIGN KEY (sender) REFERENCES Trader (email),
  FOREIGN KEY (recipient) REFERENCES Trader (email),

  senderId    INT         NOT NULL,
  recipientId INT         NOT NULL,
  FOREIGN KEY (senderId) REFERENCES Trader (id),
  FOREIGN KEY (recipientId) REFERENCES Trader (id)
);

DROP TABLE IF EXISTS TraderStock CASCADE;
CREATE TABLE TraderStock (#TRADERSTOCK TABLE#
  id         INTEGER     NOT NULL AUTO_INCREMENT, #Each entity is unique for a symbol, Trader pair
  email      VARCHAR(50) NOT NULL, #Represents the volume of each stock that a Trader owns (can go negative!)
  symbol     VARCHAR(10) NOT NULL,
  volume     INTEGER     NOT NULL DEFAULT 0,
  affinity   INTEGER     NOT NULL DEFAULT 0,
  lastUpdate BIGINT      NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (email, symbol),
  FOREIGN KEY (email) REFERENCES Trader (email),
  FOREIGN KEY (symbol) REFERENCES Symbol (symbol)
);


# clustering
/*
DROP TABLE IF EXISTS Cluster CASCADE;
CREATE TABLE Cluster (#CLUSTER TABLE#
  id   INTEGER NOT NULL AUTO_INCREMENT, #Links together a group of related factors
  time BIGINT  NOT NULL, #Each entity is unique for each clusterId
  day  BIGINT  NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS ClusterNode CASCADE;
CREATE TABLE ClusterNode (#FACTORCLUSTER TABLE#
  id   INTEGER NOT NULL, #Linking table to associate multiple factors with one cluster
  node INTEGER NOT NULL, #Entities are unique for each clusterId, factorId pair
  PRIMARY KEY (id, node),
  FOREIGN KEY (id) REFERENCES Cluster (id),
  FOREIGN KEY (node) REFERENCES Node (id)
);

DROP TABLE IF EXISTS ClusterEdge CASCADE;
CREATE TABLE ClusterEdge (#FACTORCLUSTER TABLE#
  id   INTEGER NOT NULL, #Linking table to associate multiple factors with one cluster
  edge INTEGER NOT NULL, #Entities are unique for each clusterId, factorId pair
  PRIMARY KEY (id, edge),
  FOREIGN KEY (id) REFERENCES Cluster (id),
  FOREIGN KEY (edge) REFERENCES Edge (id)
);

DROP TABLE IF EXISTS Factor CASCADE;
CREATE TABLE Factor (#FACTOR TABLE#
  factorId INTEGER NOT NULL AUTO_INCREMENT, #Exists to provide a unique value for each individual factor to refer to
  time     BIGINT  NOT NULL,
  weight   INT     NOT NULL,
  timeFrom BIGINT  NOT NULL,
  timeTo   BIGINT  NOT NULL,
  PRIMARY KEY (factorId)
);

DROP TABLE IF EXISTS ClusterFactor CASCADE;
CREATE TABLE ClusterFactor (#FACTORCLUSTER TABLE#
  clusterId INTEGER NOT NULL, #Linking table to associate multiple factors with one cluster
  factorId  INTEGER NOT NULL, #Entities are unique for each clusterId, factorId pair
  PRIMARY KEY (clusterId, factorId),
  FOREIGN KEY (clusterId) REFERENCES Cluster (id),
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
*/

SET FOREIGN_KEY_CHECKS = 1;
