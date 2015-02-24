DELIMITER //
DROP PROCEDURE IF EXISTS InsertTick;
CREATE PROCEDURE InsertTick(tick INT, time1 BIGINT, time2 BIGINT)
  BEGIN
    SELECT @trades := COUNT(*) FROM Trade WHERE time1 <= time AND time < time2;
    SELECT @comms := COUNT(*) FROM Comm WHERE time1 <= time AND time < time2;

    INSERT INTO Tick (tick, trades, comms)
      VALUES (tick, @trades, @comms);
  END //
DELIMITER ;





DELIMITER //
DROP PROCEDURE IF EXISTS InsertTrader;
CREATE PROCEDURE InsertTrader(newTrader TEXT, inDomain TEXT)
  BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE tId INT;
    DECLARE tEmail VARCHAR(50);
    DECLARE cur CURSOR FOR SELECT id, email FROM Trader;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    IF NOT EXISTS(SELECT * FROM Trader WHERE email = newTrader)
    THEN
      INSERT INTO Node VALUES ();
      INSERT INTO Trader (id, email, domain)
          VALUES (@newId := LAST_INSERT_ID(), newTrader, inDomain);

      OPEN cur;

      traderLoop: LOOP
        FETCH cur INTO tId, tEmail;

        IF done THEN
          LEAVE traderLoop;
        END IF;

        IF newTrader < tEmail THEN
          INSERT INTO Edge (source, target) VALUES (@newId, tId);
          INSERT INTO TraderTraderEdge(id, trader1, trader2)
            VALUES (LAST_INSERT_ID(), newTrader, tEmail);
        ELSEIF tEmail < newTrader THEN
          INSERT INTO Edge (source, target) VALUES (tId, @newId);
          INSERT INTO TraderTraderEdge(id, trader1, trader2)
          VALUES (LAST_INSERT_ID(), tEmail, newTrader);
        END IF;

      END LOOP;

      CLOSE cur;
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

    DECLARE done INT DEFAULT FALSE;
    DECLARE tId INT;
    DECLARE tEmail VARCHAR(50);
    DECLARE cur CURSOR FOR SELECT id, email FROM Trader;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    IF NOT EXISTS(SELECT * FROM Sector WHERE sector = inSector)
    THEN
      #INSERT INTO Counter VALUES (), ();
      #INSERT INTO Sector (sector, tradeCnt)
      #VALUES (inSector, LAST_INSERT_ID());
      INSERT INTO Node VALUES ();
      INSERT INTO Sector (id, sector)
      VALUES (@sectorId := LAST_INSERT_ID(), inSector);


      OPEN cur;

      traderLoop: LOOP
        FETCH cur INTO tId, tEmail;

        IF done THEN
          LEAVE traderLoop;
        END IF;

        INSERT INTO Edge (source, target) VALUES (tId, @sectorId);
        INSERT INTO TraderSectorEdge (id, email, sector)
        VALUES (LAST_INSERT_ID(), tEmail, inSector);

      END LOOP;

      CLOSE cur;



    END IF;
  END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS InsertSymbol;
CREATE PROCEDURE InsertSymbol(inSymbol VARCHAR(10), inSector VARCHAR(40))
  BEGIN

    DECLARE done INT DEFAULT FALSE;
    DECLARE tId INT;
    DECLARE tEmail VARCHAR(50);
    DECLARE cur CURSOR FOR SELECT id, email FROM Trader;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    IF NOT EXISTS(SELECT * FROM Symbol WHERE symbol = inSymbol)
    THEN
      #INSERT INTO Counter VALUES (), ();
      #INSERT INTO Symbol (symbol, sector, tradeCnt, priceCnt)
      #VALUES (inSymbol, inSEctor, LAST_INSERT_ID(), LAST_INSERT_ID() + 1);
      INSERT INTO Node VALUES ();
      INSERT INTO Symbol (id, symbol, sector)
      VALUES (@symbolId := LAST_INSERT_ID(), inSymbol, inSector);



      OPEN cur;

      traderLoop: LOOP
        FETCH cur INTO tId, tEmail;

        IF done THEN
          LEAVE traderLoop;
        END IF;

        INSERT INTO Edge (source, target) VALUES (tId, @symbolId);
        INSERT INTO TraderSymbolEdge (id, email, symbol)
        VALUES (LAST_INSERT_ID(), tEmail, inSymbol);

      END LOOP;

      CLOSE cur;


    END IF;
  END //
DELIMITER ;

