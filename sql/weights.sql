DELIMITER //
DROP PROCEDURE IF EXISTS UpdateWeights;
CREATE PROCEDURE UpdateWeights(time1 BIGINT, time2 BIGINT)
  BEGIN

#CALL AgeWeights();
    CALL UpdateCommCounts(time1, time2);
    CALL UpdateTradeCounts(time1, time2);


    SELECT
      @avg := AVG(comms),
      @std := STD(comms),
      @min := MIN(comms),
      @max := MAX(comms)
    FROM TraderTraderEdge;

    #UPDATE TraderTraderEdge
      #SET commWgt = 0.75 * commWgt + 0.25 / POW(2, comms);
      #SET commWgt = 0.5 * commWgt + 0.5 * (comms/(@max-@min));
      #SET commWgt = 0.3 * commWgt + 0.7 * ((comms - @avg) / @std);
     #SET commWgt = (comms - @avg) / @std;
     #SET commWgt = /*0.3 * commWgt +*/ 1 * ((comms - @avg) / @std);

    UPDATE TraderTraderEdge
      SET commWgt = 0.75 * commWgt;

    UPDATE TraderTraderEdge
      SET commWgt = 1 - ((1-commWgt) * POW(0.5, comms));




    UPDATE TraderSectorEdge
    SET tradeWgt = tradeWgt * 0.9;

    UPDATE TraderSymbolEdge
    SET tradeWgt = tradeWgt * 0.9;


  END //
DELIMITER ;



DELIMITER //
DROP PROCEDURE IF EXISTS UpdateCommCounts;
CREATE PROCEDURE UpdateCommCounts(time1 BIGINT, time2 BIGINT)
  BEGIN
    DECLARE cSender VARCHAR(50);
    DECLARE cRecipient VARCHAR(50);
    DECLARE cCount INT;
    DECLARE done INT DEFAULT FALSE;
    DECLARE tpCursor CURSOR FOR
      #SELECT sender, recipient FROM Comm WHERE time1 <= time AND time < time2;
      SELECT sender, recipient, count(*) FROM Comm WHERE time1 <= time AND time < time2 GROUP BY sender, recipient;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN tpCursor;

    tpLoop: LOOP
      FETCH tpCursor
      INTO cSender, cRecipient, cCount;

      IF done
      THEN
        LEAVE tpLoop;
      END IF;

      IF cSender < cRecipient
      THEN
        UPDATE TraderTraderEdge
        SET comms = cCount
        WHERE trader1 = cSender AND trader2 = cRecipient;

      ELSEIF cRecipient < cSender
        THEN
          UPDATE TraderTraderEdge
          SET comms = cCount
          WHERE trader1 = cRecipient AND trader2 = cSender;
      END IF;

    END LOOP;

    CLOSE tpCursor;
  END //
DELIMITER ;


DELIMITER //
DROP PROCEDURE IF EXISTS UpdateTradeCounts;
CREATE PROCEDURE UpdateTradeCounts(time1 BIGINT, time2 BIGINT)
  BEGIN
    DECLARE tBuyer VARCHAR(50);
    DECLARE tSeller VARCHAR(50);
    DECLARE tSymbol VARCHAR(10);
    DECLARE tSector VARCHAR(40);
    DECLARE tCount INTEGER;
    DECLARE done INT DEFAULT FALSE;
#DECLARE tpCursor CURSOR FOR SELECT buyer, seller, symbol, sector FROM Trade WHERE time1 <= time AND time < time2;
    DECLARE tpCursor CURSOR FOR
      SELECT
        buyer,
        seller,
        symbol,
        sector,
        count(*) AS count
      FROM Trade
      WHERE time1 <= time AND time < time2
      GROUP BY buyer, seller, symbol, sector;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN tpCursor;

    tpLoop: LOOP
      FETCH tpCursor
      INTO tBuyer, tSeller, tSymbol, tSector, tCount;

      IF done
      THEN
        LEAVE tpLoop;
      END IF;

      IF tBuyer < tSeller
      THEN
        UPDATE TraderTraderEdge
        SET tradeWgt = tradeWgt * 1.1
        WHERE trader1 = tBuyer AND trader2 = tSeller;

      ELSEIF tSeller < tBuyer
        THEN
          UPDATE TraderTraderEdge
          SET tradeWgt = tradeWgt * 1.1
          WHERE trader1 = tSeller AND trader2 = tBuyer;
      END IF;

      UPDATE TraderSymbolEdge
      SET tradeWgt = tradeWgt * 1.1
      WHERE (email = tBuyer OR email = tSeller) AND symbol = tSymbol;

      UPDATE TraderSectorEdge
      SET tradeWgt = tradeWgt * 1.1
      WHERE (email = tBuyer OR email = tSeller) AND sector = tSector;

    END LOOP;

    CLOSE tpCursor;
  END //
DELIMITER ;




