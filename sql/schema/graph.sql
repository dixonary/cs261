#Suspicious Activity Detection Tool#
#Schema#

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Node CASCADE;
CREATE TABLE Node (
  id    INTEGER     NOT NULL AUTO_INCREMENT,
  label VARCHAR(64) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS Trader CASCADE;
CREATE TABLE Trader (#TRADER TABLE#
  id       INTEGER     NOT NULL,
  email    VARCHAR(50) NOT NULL, #Contains entities which represent individual Traders and averages for Trading volume and Profit
  domain   VARCHAR(30) NOT NULL, #There email addresses are unique and so are used as our primary key
  tradeCnt INTEGER     NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (id) REFERENCES Node (id),
  UNIQUE KEY (email)
#,
#FOREIGN KEY (tradeCnt) REFERENCES Counter (id)
);

DROP TABLE IF EXISTS Symbol CASCADE;
CREATE TABLE Symbol (#SYMBOL TABLE#
  id              INTEGER     NOT NULL,
#sectorId INTEGER NOT NULL,

  symbol          VARCHAR(10) NOT NULL, #Contains entities which represent individual Stocks and averages for Trading volume and Profit
  sector          VARCHAR(40) NOT NULL, #Each Stock is unique on it's name/symbol
  price           FLOAT       NOT NULL DEFAULT 0,

# counts (for present interval)
  trades          INTEGER     NOT NULL,
  buys            INTEGER     NOT NULL,
  sells           INTEGER     NOT NULL,

# rates (for present interval)
  tradesPerTrader DOUBLE      NOT NULL,
  buysPerTrader   DOUBLE      NOT NULL,
  sellsPerTrader  DOUBLE      NOT NULL,

# significance threshold (for present interval)
  tradesSig INTEGER      NOT NULL,
  buysSig   INTEGER      NOT NULL,
  sellsSig  INTEGER      NOT NULL,


  tradeCnt        INTEGER     NOT NULL,
  priceCnt        INTEGER     NOT NULL,
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

DROP TABLE IF EXISTS TraderPair CASCADE;
CREATE TABLE TraderPair (#TRADERPAIR TABLE#
  id          INTEGER     NOT NULL,
  trader1Id   INT         NOT NULL,
  trader2Id   INT         NOT NULL,




  # counts for last time interval
  common      INTEGER     NOT NULL,
  commonBuys  INTEGER     NOT NULL,
  commonSells INTEGER     NOT NULL,

  comms       INTEGER     NOT NULL, #comms exchanged


  symbols     INTEGER     NOT NULL, #common symbols traded
  sectors     INTEGER     NOT NULL, #common sectors traded

#  stockWgt INTEGER     NOT NULL,

  commWgt     DOUBLE      NOT NULL DEFAULT 1.0,
  symbolWgt   DOUBLE      NOT NULL DEFAULT 1.0,
  sectorWgt   DOUBLE      NOT NULL DEFAULT 1.0,

#stockWgt FLOAT       NOT NULL,
#tradeWgt FLOAT       NOT NULL,

  # redundant
  trader1     VARCHAR(50) NOT NULL, #Each entity is unique for a Trader pair
  trader2     VARCHAR(50) NOT NULL, #Represents the stocks in common, Trades and Communications between Trader pairs


  PRIMARY KEY (id),
  FOREIGN KEY (trader1Id) REFERENCES Trader (id),
  FOREIGN KEY (trader2Id) REFERENCES Trader (id),

  UNIQUE KEY (trader1, trader2),
  FOREIGN KEY (trader1) REFERENCES Trader (email),
  FOREIGN KEY (trader2) REFERENCES Trader (email),
  FOREIGN KEY (id) REFERENCES Edge (id)
);

DROP TABLE IF EXISTS TraderSymbol CASCADE;
CREATE TABLE TraderSymbol (#TRADERPAIR TABLE#
  id       INTEGER     NOT NULL,
  traderId INT         NOT NULL,
  symbolId INT         NOT NULL,

  #trades
  trades   INTEGER     NOT NULL, # trades
  buys     INTEGER     NOT NULL, # trades
  sells    INTEGER     NOT NULL, # trades

  # edge weights
  tradeWgt FLOAT       NOT NULL,

  # redundant conveniences
  email    VARCHAR(50) NOT NULL, #Each entity is unique for a Trader pair
  symbol   VARCHAR(10) NOT NULL, #Represents the stocks in common, Trades and Communications between Trader pairs

  PRIMARY KEY (id),
  UNIQUE KEY (traderId, symbolId),
  FOREIGN KEY (traderId) REFERENCES Trader (id),
  FOREIGN KEY (symbolId) REFERENCES Symbol (id),

  UNIQUE KEY (email, symbol),
  FOREIGN KEY (email) REFERENCES Trader (email),
  FOREIGN KEY (symbol) REFERENCES Symbol (symbol),
  FOREIGN KEY (id) REFERENCES Edge (id)
);

DROP TABLE IF EXISTS TraderSector CASCADE;
CREATE TABLE TraderSector (#TRADERPAIR TABLE#
  id       INTEGER     NOT NULL AUTO_INCREMENT,
  email    VARCHAR(50) NOT NULL, #Each entity is unique for a Trader pair
  sector   VARCHAR(40) NOT NULL, #Represents the stocks in common, Trades and Communications between Trader pairs

  # counts
  trades   INTEGER     NOT NULL, #comms exchanged for interval

  # edge weights
  tradeWgt FLOAT       NOT NULL, # represents strength of trader activity in sector
  PRIMARY KEY (id),
  UNIQUE KEY (email, sector),
  FOREIGN KEY (email) REFERENCES Trader (email),
  FOREIGN KEY (sector) REFERENCES Sector (sector),
  FOREIGN KEY (id) REFERENCES Edge (id)
);


SET FOREIGN_KEY_CHECKS = 1;
