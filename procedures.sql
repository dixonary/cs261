


DELIMITER //
DROP PROCEDURE IF EXISTS InsertTrader;
CREATE PROCEDURE InsertTrader(newTrader TEXT, inDomain TEXT)
  BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE otherEmail VARCHAR(50);
    DECLARE tradersCursor CURSOR FOR SELECT email FROM Trader;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    IF NOT EXISTS(SELECT * FROM Trader WHERE email = newTrader)
    THEN
      INSERT INTO Counter VALUES ();
      INSERT INTO Trader (email, domain, tradeCnt)
          VALUES (newTrader, inDomain, LAST_INSERT_ID());

      OPEN tradersCursor;

      traderLoop: LOOP
        FETCH tradersCursor INTO otherEmail;

        IF done THEN
          LEAVE traderLoop;
        END IF;

        INSERT INTO Counter VALUES (), (), ();

        IF newTrader < otherEmail THEN
          INSERT INTO TraderPair(trader1, trader2, stockCnt, tradeCnt, commCnt)
            VALUES (newTrader, otherEmail, LAST_INSERT_ID(), LAST_INSERT_ID()+1, LAST_INSERT_ID()+2);
        ELSEIF otherEmail < newTrader THEN
          INSERT INTO TraderPair(trader1, trader2, stockCnt, tradeCnt, commCnt)
            VALUES (otherEmail, newTrader, LAST_INSERT_ID(), LAST_INSERT_ID()+1, LAST_INSERT_ID()+2);
        END IF;

      END LOOP;

      CLOSE tradersCursor;



      #INSERT INTO TraderPair (trader1, trader2)
        #(SELECT inEmail, email FROM Trader WHERE email > inEmail);

      #INSERT INTO TraderPair (trader1, trader2)
        #(SELECT email, inEmail FROM Trader WHERE email < inEmail);

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

    CALL InsertSector(sector);
    CALL InsertSymbol(symbol, sector);

    INSERT INTO Trade (time, buyer, seller, price, currency, size, symbol, sector, bid, ask)
    VALUES (time, buyer, seller, price,currency, size, symbol, sector, bid, ask);
  END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertSector;
CREATE PROCEDURE InsertSector(inSector VARCHAR(40))
  BEGIN
    IF NOT EXISTS(SELECT
                    *
                  FROM Sector
                  WHERE sector = inSector)
    THEN
      INSERT INTO Counter VALUES (), ();
      INSERT INTO Sector (sector, tradeCnt)
      VALUES (inSector, LAST_INSERT_ID());
    END IF;
  END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertSymbol;
CREATE PROCEDURE InsertSymbol(inSymbol VARCHAR(10), inSEctor VARCHAR(40))
  BEGIN
    IF NOT EXISTS(SELECT
                    *
                  FROM Symbol
                  WHERE symbol = inSymbol)
    THEN
      INSERT INTO Counter VALUES (), ();
      INSERT INTO Symbol (symbol, sector, tradeCnt, priceCnt)
      VALUES (inSymbol, inSEctor, LAST_INSERT_ID(), LAST_INSERT_ID() + 1);
    END IF;
  END //
DELIMITER ;

