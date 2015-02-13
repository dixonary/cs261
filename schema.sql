--Suspicious Activity Detection Tool--
--Schema--

DROP TABLE IF EXISTS Trader CASCADE;
CREATE TABLE Trader (						--TRADER TABLE--
	email varchar(50) NOT NULL,				--Contains entities which represent individual Traders and moving averages for their use
	domain varchar(30) NOT NULL,			--There email addresses are unique and so are used as our primary key
	avg1 integer NOT NULL,
	avg2 integer NOT NULL,
	avg3 integer NOT NULL,
	PRIMARY KEY(email)
);

DROP TABLE IF EXISTS Symbol CASCADE;
CREATE TABLE symbol (						--SYMBOL TABLE--
	name varchar(10) NOT NULL,				--Contains entities which represent individual Stocks
	totalTrades integer NOT NULL,			--Each Stock is unique on it's name/symbol
	avg1 integer NOT NULL,
	avg2 integer NOT NULL,
	avg3 integer NOT NULL,
	PRIMARY KEY(name)
);

DROP TABLE IF EXISTS Sector CASCADE;
CREATE TABLE sector (						--SECTOR TABLE--
	name varchar(40) NOT NULL,				--Contains entities which represent individual sectors
	totalTrades integer NOT NULL,			--Each sector is unique on it's name
	avg1 integer NOT NULL,
	avg2 integer NOT NULL,
	avg3 integer NOT NULL,
	PRIMARY KEY(name)
);

DROP TABLE IF EXISTS Trade CASCADE;
CREATE TABLE Trade (						--TRADE TABLE--
	id integer NOT NULL,					--Contains the full representation of a Trade as received from the Trades feed
	time datetime NOT NULL,					--Also has an id as primary key as the timestamp cannot be guranteed to be unique
	buyer varchar(50) NOT NULL,
	seller varchar(50) NOT NULL,
	price float NOT NULL,
	currency varchar(3) NOT NULL,
	size integer NOT NULL,
	symbol varchar(10) NOT NULL,
	sector varchar(40) NOT NULL,
	bid float NOT NULL,
	ask float NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(buyer)  REFERENCES Trader(email),
	FOREIGN KEY(seller) REFERENCES Trader(email),
	FOREIGN KEY(symbol) REFERENCES Symbol(name),
	FOREIGN KEY(sector) REFERENCES Sector(name)
);

DROP TABLE IF EXISTS CommLink CASCADE;
CREATE TABLE CommLink (						--COMMLINK TABLE--
	trader1 varchar(50) NOT NULL,			--Contains the link between individual Communications and individual Traders
	trader2 varchar(50) NOT NULL,			--Tracks the history of communication between two traders in both directions
	totalTrades integer NOT NULL,			--Each entity is unique for each Trader pair
	avg1 integer NOT NULL,
	avg2 integer NOT NULL,
	avg3 integer NOT NULL,
	PRIMARY KEY(trader1,trader2),
	FOREIGN KEY(trader1) REFERENCES Trader(email),
	FOREIGN KEY(trader2) REFERENCES Trader(email)
);
	
DROP TABLE IF EXISTS Communication CASCADE;
CREATE TABLE Communication (				--COMMUNICATION TABLE--
	id integer NOT NULL,					--Contains individual Communcations between Traders
	time datetime NOT NULL,					--Has an id field as the timestamp cannot be guaranteed to be unique
	sender varchar(50) NOT NULL,
	recipient varchar(50) NOT NULL,
	PRIMARY KEY(id),
	FOREIGN KEY(sender)    REFERENCES Trader(email),
	FOREIGN KEY(recipient) REFERENCES Trader(email)
);
	
DROP TABLE IF EXISTS Factor CASCADE;
CREATE TABLE Factor (						--FACTOR TABLE--
	factorId integer NOT NULL,				--Exists to provide a unique value for each individual factor to refer to
	PRIMARY KEY(factorId)					--Each factorId is unique
);
	
DROP TABLE IF EXISTS TradeFactor CASCADE;
CREATE TABLE TradeFactor (					--TRADEFACTOR TABLE--
	tradeId integer NOT NULL,				--Linking table between the Factor and Trade tables
	factorId integer NOT NULL,				--Each entity is unique for a tradeId factorId pair
	PRIMARY KEY(tradeId,factorId),
	FOREIGN KEY(tradeId)  REFERENCES Trade(id),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS CommFactor CASCADE;
CREATE TABLE CommFactor (					--COMMFACTOR TABLE--
	commId integer NOT NULL,				--Linking table between the Factor and Communication tables
	factorId integer NOT NULL,				--Each entity is unique for a commId factorId pair
	PRIMARY KEY(commId,factorId),
	FOREIGN KEY(commId)   REFERENCES Communication(id),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS CommsFactor CASCADE;
CREATE TABLE CommsFactor (					--COMMSFACTOR TABLE--
	factorId integer NOT NULL,				--Represents the factor "Which traders are communicating with who"
	email1 varchar(50) NOT NULL,			--Unique for each factorId
	email2 varchar(50) NOT NULL,
	timeFrom datetime NOT NULL,
	timeTo datetime NOT NULL,
	PRIMARY KEY(factorId),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS PortfoliosFactor CASCADE;
CREATE TABLE PortfoliosFactor (				--PORTFOLIOSFACTOR TABLE--
	factorId integer NOT NULL,				--Represents the factor "Traders with similar portfolios"
	email1 varchar(50) NOT NULL,			--Unique for each factorId
	email2 varchar(50) NOT NULL,
	timeFrom datetime NOT NULL,
	timeTo datetime NOT NULL,
	PRIMARY KEY(factorId),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS StylesFactor CASCADE;
CREATE TABLE StylesFactor (					--STYLESFACTOR TABLE--
	factorId integer NOT NULL,				--Represents the factor "Traders with similar styles/trading patterns"
	email1 varchar(50) NOT NULL,			--Unique for each factorId
	email2 varchar(50) NOT NULL,
	timeFrom datetime NOT NULL,
	timeTo datetime NOT NULL,
	PRIMARY KEY(factorId),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS sectorsFactor CASCADE;
CREATE TABLE sectorsFactor (				--SECTORSFACTOR TABLE--
	factorId integer NOT NULL,				--Represents the factor "Active sectors"
	sector varchar(40) NOT NULL,			--Unique for each factorId
	timeFrom datetime NOT NULL,
	timeTo datetime NOT NULL,
	PRIMARY KEY(factorId),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS symbolsFactor CASCADE;
CREATE TABLE symbolsFactor (				--SYMBOLSFACTOR TABLE--
	factorId integer NOT NULL,				--Represents the factor "Active Stocks (symbol)"
	symbol varchar(10) NOT NULL,			--Unique for each factorId
	timeFrom datetime NOT NULL,
	timeTo datetime NOT NULL,
	PRIMARY KEY(factorId),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS TradersFactor CASCADE;
CREATE TABLE TradersFactor (				--TRADERSFACTOR TABLE--
	factorId integer NOT NULL,				--Represents the factor "Particularly active Traders"
	email varchar(50) NOT NULL,				--Unique for each factorId
	timeFrom datetime NOT NULL,
	timeTo datetime NOT NULL,
	PRIMARY KEY(factorId),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS PerformancesFactor CASCADE;
CREATE TABLE PerformancesFactor (			--PERFORMANCESFACTOR TABLE--
	factorId integer NOT NULL,				--Represents the factor "Anomalous market performance for trader"
	email varchar(50) NOT NULL,				--Unique for each factorId
	timeFrom datetime NOT NULL,
	timeTo datetime NOT NULL,
	PRIMARY KEY(factorId),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS PricesFactor CASCADE;
CREATE TABLE PricesFactor (					--PRICESFACTOR TABLE--
	factorId integer NOT NULL,				--Represents the factor "Sudden price changes of a stock"
	email1 varchar(50) NOT NULL,			--Unique for each factorId
	email2 varchar(50) NOT NULL,
	symbol varchar(10) NOT NULL,
	timeFrom datetime NOT NULL,
	timeTo datetime NOT NULL,
	PRIMARY KEY(factorId),
	FOREIGN KEY(factorId) REFERENCES Factor(factorId)
);

DROP TABLE IF EXISTS Cluster CASCADE;
CREATE TABLE Cluster (						--CLUSTER TABLE--
	clusterId integer NOT NULL,				--Links together a group of related clusters
	PRIMARY KEY(clusterId)					--Each entity is unique for each clusterId
);
	
DROP TABLE IF EXISTS FactorCluster CASCADE;
CREATE TABLE FactorCluster (				--FACTORCLUSTER TABLE--
	clusterId integer NOT NULL,				--Linking table to associate multiple factors with one cluster
	factorId integer NOT NULL,				--Entities are unique for each clusterId, factorId pair
	PRIMARY KEY(clusterId,factorId),
	FOREIGN KEY(clusterId) REFERENCES Cluster(clusterId),
	FOREIGN KEY(factorId)  REFERENCES Factor(factorId)
);
	
DROP TABLE IF EXISTS StockOwnership CASCADE;
CREATE TABLE StockOwnership (				--STOCKOWNERSHIP TABLE--
	symbol integer NOT NULL,				--Represents the volume of each stock that a Trader owns (can go negative!)
	email varchar(50) NOT NULL,				--Each entity is unique for a symbol, email pair
	volume integer NOT NULL,
	lastUpdate datetime NOT NULL,
	PRIMARY KEY(symbol,email),
	FOREIGN KEY(symbol) REFERENCES Symbol(name),
	FOREIGN KEY(email) REFERENCES Trader(email)
);
