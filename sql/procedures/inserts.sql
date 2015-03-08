DELIMITER //
DROP PROCEDURE IF EXISTS InsertTick;
CREATE PROCEDURE InsertTick(tick INT, time1 BIGINT, time2 BIGINT)
  BEGIN

    INSERT INTO Tick (tick, `start`, `end`)
      VALUES (tick, time1, time2);

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
      INSERT INTO Node (label) VALUES (newTrader);
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
          INSERT INTO TraderPair
            (id, trader1Id, trader2Id, trader1, trader2)
          VALUES
            (LAST_INSERT_ID(), @newId, tId, newTrader, tEmail);

        ELSEIF tEmail < newTrader THEN
          INSERT INTO Edge (source, target) VALUES (tId, @newId);
          INSERT INTO TraderPair
            (id, trader1Id, trader2Id, trader1, trader2)
          VALUES
            (LAST_INSERT_ID(), tId, @newId, tEmail, newTrader);
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
  time     BIGINT,
    tick INT,
  buyer    VARCHAR(50),
  seller   VARCHAR(50),
  price    FLOAT,
  size     INTEGER,
  currency VARCHAR(3),
  inSymbol   VARCHAR(10),
  inSector   VARCHAR(40),
  bid      FLOAT,
  ask      FLOAT
)
  BEGIN

    #CALL InsertTrader

    CALL InsertSector(inSector);
    CALL InsertSymbol(inSymbol, inSector);

    SELECT @buyerId := id FROM Trader WHERE `email` = buyer;
    SELECT @sellerId := id FROM Trader WHERE `email` = seller;
    SELECT @symbolId := id FROM Symbol WHERE `symbol` = inSymbol;
    SELECT @sectorId := id FROM Sector WHERE `sector` = inSector;



    INSERT INTO Trade
      (time, tick, buyer, seller, price, currency, size, symbol, sector, bid, ask, buyerId, sellerId, symbolId, sectorId)
    VALUES
      (time, tick, buyer, seller, price,currency, size, inSymbol, inSector, bid, ask, @buyerId, @sellerId, @symbolId, @sectorId);
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
      INSERT INTO Node (label) VALUES (inSector);
      INSERT INTO Sector (id, sector)
      VALUES (@sectorId := LAST_INSERT_ID(), inSector);


      OPEN cur;

      traderLoop: LOOP
        FETCH cur INTO tId, tEmail;

        IF done THEN
          LEAVE traderLoop;
        END IF;

        INSERT INTO Edge (source, target) VALUES (tId, @sectorId);
        INSERT INTO TraderSector (id, email, sector)
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
      INSERT INTO Node (label) VALUES (inSymbol);
      INSERT INTO Symbol (id, symbol, sector)
      VALUES (@symbolId := LAST_INSERT_ID(), inSymbol, inSector);



      OPEN cur;

      traderLoop: LOOP
        FETCH cur INTO tId, tEmail;

        IF done THEN
          LEAVE traderLoop;
        END IF;

        INSERT INTO Edge (source, target) VALUES (tId, @symbolId);
        INSERT INTO TraderSymbol (id, traderId, symbolId, email, symbol)
        VALUES (LAST_INSERT_ID(), tId, @symbolId, tEmail, inSymbol);

      END LOOP;

      CLOSE cur;


    END IF;
  END //
DELIMITER ;



DELIMITER //
DROP PROCEDURE IF EXISTS InsertComm;
CREATE PROCEDURE InsertComm(
  time     BIGINT,
  tick    INT,
  sender    VARCHAR(50),
  recipient   VARCHAR(50)
)
  BEGIN
    SELECT @senderId := id FROM Trader WHERE `email` = sender;
    SELECT @recipientId := id FROM Trader WHERE `email` = recipient;

    INSERT INTO Comm
    (time, tick, sender, recipient, senderId, recipientId)
    VALUES
      (time, tick, sender, recipient, senderId, recipientId);
  END //
DELIMITER ;