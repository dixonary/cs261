/*
aggregations happen at beginning of a tick and are no longer valid by its end
 */

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS AggrTrade CASCADE;
CREATE TABLE AggrTrade (
  edgeId   INT NOT NULL,
  traderId INT NOT NULL,
  symbolId INT NOT NULL,
  #sectorId INT NOT NULL,
  trades   INT NOT NULL,
  buys     INT NOT NULL,
  sells    INT NOT NULL
);

DROP TABLE IF EXISTS AggrTrader CASCADE;
CREATE TABLE AggrTrader (
  trader VARCHAR(50) NOT NULL,
  trades INT         NOT NULL,
  buys   INT         NOT NULL,
  sells  INT         NOT NULL,
  comms  INT         NOT NULL,
  sends  INT         NOT NULL,
  recs   INT         NOT NULL
);

DROP TABLE IF EXISTS AggrSymbol CASCADE;
CREATE TABLE AggrSymbol (
  symbolId INT NOT NULL,
  trades   INT NOT NULL,
  buys     INT NOT NULL,
  sells    INT NOT NULL
);

DROP TABLE IF EXISTS AggrSector CASCADE;
CREATE TABLE AggrSector (
  sectorId INT NOT NULL,
  trades   INT NOT NULL,
  buys     INT NOT NULL,
  sells    INT NOT NULL
);






DROP TABLE IF EXISTS AggrTraderCommon CASCADE;
CREATE TABLE AggrTraderCommon (
  edgeId      INT         NOT NULL,
  trader1Id   INT         NOT NULL,
  trader2Id   INT         NOT NULL,

  common      INT         NOT NULL,
  commonBuys  INT         NOT NULL,
  commonSells INT         NOT NULL,

  trader1     VARCHAR(50) NOT NULL,
  trader2     VARCHAR(50) NOT NULL

);




DROP TABLE IF EXISTS AggrComm CASCADE;
CREATE TABLE AggrComm (#TRADER TABLE#
  #trader VARCHAR(50) NOT NULL,
  traderId INT NOT NULL,
  comms    INT NOT NULL,
  sends    INT NOT NULL,
  recs     INT NOT NULL
);


DROP TABLE IF EXISTS AggrTraderComm CASCADE;
CREATE TABLE AggrTraderComm (#TRADER TABLE#
  edgeId    INT NOT NULL,
  trader1Id INT NOT NULL,
  trader2Id INT NOT NULL,
  comms     INT NOT NULL,
  sent1     INT NOT NULL,
  sent2     INT NOT NULL
);


SET FOREIGN_KEY_CHECKS = 1;
