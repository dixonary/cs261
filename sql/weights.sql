DELIMITER //
DROP PROCEDURE IF EXISTS UpdateWeights;
CREATE PROCEDURE UpdateWeights(time1 BIGINT, time2 BIGINT)
  BEGIN

#CALL AgeWeights();
    CALL UpdateCommCounts(time1, time2);
    CALL UpdateTradeCounts(time1, time2);


    SELECT
      @avg := AVG(comms),
      @std := STD(comms)
    FROM TraderTraderEdge;

    UPDATE TraderTraderEdge
    SET commWgt = (comms - @avg) / @std;

    UPDATE TraderSectorEdge
    SET tradeWgt = tradeWgt * 0.9;

    UPDATE TraderSymbolEdge
    SET tradeWgt = tradeWgt * 0.9;


  END //
DELIMITER ;


DELIMITER //
DROP PROCEDURE IF EXISTS AgeWeights;
CREATE PROCEDURE AgeWeights()
  BEGIN

    UPDATE TraderTraderEdge
    SET commWgt = commWgt * 0.9,
      tradeWgt  = tradeWgt * 0.9,
      stockWgt  = stockWgt * 0.9;

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
    DECLARE tSender VARCHAR(50);
    DECLARE tRecipient VARCHAR(50);
    DECLARE done INT DEFAULT FALSE;
    DECLARE tpCursor CURSOR FOR SELECT
                                  sender,
                                  recipient
                                FROM Comm
                                WHERE time1 <= time AND time < time2;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN tpCursor;

    tpLoop: LOOP
      FETCH tpCursor
      INTO tSender, tRecipient;

      IF done
      THEN
        LEAVE tpLoop;
      END IF;

      IF tSender < tRecipient
      THEN
        UPDATE TraderTraderEdge
        SET comms = comms + 1,
          commWgt = commWgt * 1.1
        WHERE trader1 = tSender AND trader2 = tRecipient;

      ELSEIF tRecipient < tSender
        THEN
          UPDATE TraderTraderEdge
          SET comms = comms + 1,
            commWgt = commWgt * 1.1
          WHERE trader1 = tRecipient AND trader2 = tSender;
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




