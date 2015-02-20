


DELIMITER //
DROP PROCEDURE IF EXISTS InsertTrader;
CREATE PROCEDURE InsertTrader(inEmail TEXT, inDomain TEXT)
  BEGIN
    #DECLARE inDomain TEXT;
    #inDomain =

    IF NOT EXISTS(SELECT
                    *
                  FROM Trader
                  WHERE email = inEmail)
    THEN
      INSERT INTO Counter VALUES ();
      INSERT INTO Trader (email, domain, tradeCnt)
      VALUES (inEmail, inDomain, LAST_INSERT_ID());
    END IF;
  END //
DELIMITER ;



#INSERT INTO Trade (time, buyer, seller, price, currency, size, symbol, sector, bid, ask) VALUES (?,?,?,?,?,?,?,?,?,?);

/*
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
 */




DELIMITER //

DROP PROCEDURE IF EXISTS InsertTrade;
CREATE PROCEDURE InsertTrade(
  time     LONG,
  buyer    VARCHAR(50),
  seller   VARCHAR(50),
  price    FLOAT,
  size     INTEGER,
  currency VARCHAR(3),
  symbol   VARCHAR(10),
  sector   VARCHAR(40),
  bid      FLOAT,
  ask      FLOAT
)
  BEGIN

    #CALL InsertTrader

    IF NOT EXISTS(SELECT
                    *
                  FROM Sector
                  WHERE name = inName)
    THEN
      INSERT INTO Counter VALUES (), ();
      INSERT INTO Symbol (name, sector, tradeCnt, priceCnt)
      VALUES (inName, inSector, LAST_INSERT_ID(), LAST_INSERT_ID() + 1);
    END IF;
  END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertSector;
CREATE PROCEDURE InsertSector(inName TEXT)
  BEGIN
    IF NOT EXISTS(SELECT
                    *
                  FROM Sector
                  WHERE name = inName)
    THEN
      INSERT INTO Counter VALUES (), ();
      INSERT INTO Sector (name, tradeCnt)
      VALUES (inName, LAST_INSERT_ID());
    END IF;
  END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertSymbol;
CREATE PROCEDURE InsertSymbol(inName TEXT, inSector VARCHAR(40))
  BEGIN
    IF NOT EXISTS(SELECT
                    *
                  FROM Symbol
                  WHERE name = inName)
    THEN
      INSERT INTO Counter VALUES (), ();
      INSERT INTO Symbol (name, sector, tradeCnt, priceCnt)
      VALUES (inName, inSector, LAST_INSERT_ID(), LAST_INSERT_ID() + 1);
    END IF;
  END //
DELIMITER ;


#INSERT INTO Symbol (name, sector, tradeCnt, priceCnt)
#    SELECT 'a', 'b', 'c', 'd' FROM Symbol WHERE NOT EXISTS (
#      INSERT INTO Counter VALUES (), ()
#)

#"INSERT INTO Counter VALUES (), ()" +
#            "INSERT IGNORE INTO Symbol (name, sector, tradeCnt, priceCnt) VALUES (?, ?, LAST_INSERT_ID(), LAST_INSERT_ID()+1);";