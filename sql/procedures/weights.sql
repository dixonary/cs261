DELIMITER //
DROP PROCEDURE IF EXISTS UpdateCounts;
CREATE PROCEDURE UpdateCounts(inTick INT, window INT)
  BEGIN








#CALL AgeWeights();

    #SET @tick=4736750; SET @ma=5; SELECT count(*) from Comm where @tick-@ma < tick AND tick <= @tick group by tick;



#reset counts
    UPDATE TraderPair
    SET comms = 0,
      symbols = 0,
      sectors = 0;


    CALL UpdateCommCounts(inTick);
    #CALL UpdateTradeCounts(inTick, time1, time2);


    DELETE FROM TickEdge
    WHERE tick = inTick;
    INSERT INTO TickEdge (tick, edge, weight) SELECT
                                                inTick,
                                                id,
                                                weight
                                              FROM Edge;






  END //
DELIMITER ;


DELIMITER //
DROP PROCEDURE IF EXISTS UpdateWeights;
CREATE PROCEDURE UpdateWeights(inTick INT/*, time1 BIGINT, time2 BIGINT*/)
  BEGIN

/*    SELECT
      @commsAvg := AVG(comms),
      @commsSum := SUM(comms),
      @commWgtAvg := AVG(commWgt),
      @symbolsAvg := AVG(symbols),
      @symbolsSum := SUM(symbols),
      @symbolWgtAvg := AVG(symbolWgt)
    FROM TraderTraderEdge;*/

    UPDATE TraderPair
    SET
      commWgt   = (SELECT cdf FROM Poisson WHERE x = comms),
      #commWgt   = comms,
      symbolWgt = (SELECT cdf FROM Poisson WHERE x = symbols);
#commWgt   = 0.25 * commWgt + 0.75 * (1 - POW(0.5, (comms / @commsAvg))),
#symbolWgt = 0.25 * symbolWgt + 0.75 * (1 - POW(0.5, (symbols / @symbolsAvg)));
/*      commWgt   = 0.5 * commWgt + 0.5 * (comms / @commsAvg),
      symbolWgt = 0.5 * symbolWgt + 0.5 * (symbols / @symbolsAvg);*/
/*      commWgt   = 0.5 * commWgt + 0.5 / (1 + EXP(  -(1 - 1))),
      symbolWgt = 0.5 * symbolWgt + 0.5 / (1 + EXP( -(1 - 1)));*/
/*      commWgt   = 0.5 * commWgt + 0.5 / (1 + EXP(  -(comms/@commsAvg - 1))),
      symbolWgt = 0.5 * symbolWgt + 0.5 / (1 + EXP( -(symbols/@symbolsAvg - 1)));*/
/*      commWgt   = 0.5 * commWgt + 0.5 / (1 + EXP(  -(comms - @commsAvg))),
      symbolWgt = 0.5 * symbolWgt + 0.5 / (1 + EXP( -(symbols - @symbolsAvg)));*/
#commWgt   = 0.75 * (commWgt / @commWgtAvg) + 0.25 * (comms / @commsAvg),
#symbolWgt = 0.75 * (symbolWgt / @symbolWgtAvg) + 0.25 * (symbols / @symbolsAvg);

    UPDATE TraderPair TTE NATURAL JOIN Edge E
    SET E.weight = TTE.commWgt * TTE.symbolWgt;


/*    UPDATE TraderSymbolEdge
    SET tradeWgt = 0.75 * tradeWgt;
    UPDATE TraderSymbolEdge
    SET tradeWgt = 1 - ((1 - tradeWgt) * POW(0.5, trades));*/


/*    UPDATE TraderSectorEdge
    SET tradeWgt = 0.75 * tradeWgt;
    UPDATE TraderSectorEdge
    SET tradeWgt = 1 - ((1 - tradeWgt) * POW(0.5, trades));*/


  END //
DELIMITER ;


DELIMITER //
DROP PROCEDURE IF EXISTS ResetWeights;
CREATE PROCEDURE ResetWeights()
  BEGIN

#UPDATE Tick
#SET analysed = 0;

    DELETE FROM TickEdge;
    DELETE FROM Tick;

    UPDATE TraderPair
    SET
      commWgt   = 1.00,
      symbolWgt = 1.00,
      sectorWgt = 0;

    UPDATE TraderSymbol
    SET tradeWgt = 0;
    UPDATE TraderSector
    SET tradeWgt = 0;


  END //
DELIMITER ;


DELIMITER //
DROP PROCEDURE IF EXISTS UpdateCommCounts;
CREATE PROCEDURE UpdateCommCounts(inTick INT)
  BEGIN
    DECLARE cSender VARCHAR(50);
    DECLARE cRecipient VARCHAR(50);
    DECLARE cCount INT;
    DECLARE done INT DEFAULT FALSE;
    DECLARE tpCursor CURSOR FOR
#SELECT sender, recipient FROM Comm WHERE time1 <= time AND time < time2;
      SELECT
        sender,
        recipient,
        count(*) AS count
      FROM Comm WHERE tick = inTick GROUP BY sender, recipient;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN tpCursor;

    tpLoop: LOOP
      FETCH tpCursor
      INTO cSender, cRecipient, cCount;

      IF done
      THEN
        LEAVE tpLoop;
      END IF;

      UPDATE TraderPair
      SET comms = comms+ cCount
      WHERE trader1 = IF(cSender < cRecipient, cSender, cRecipient) AND
            trader2 = IF(cSender < cRecipient, cRecipient, cSender);

    END LOOP;

    CLOSE tpCursor;
  END //
DELIMITER ;


DELIMITER //
DROP PROCEDURE IF EXISTS UpdateTradeCounts;
CREATE PROCEDURE UpdateTradeCounts(inTick INT, time1 BIGINT, time2 BIGINT)
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
#GROUP_CONCAT(id) as ids,
        count(*) AS count
      FROM Trade
#WHERE time1 <= time AND time < time2
      WHERE tick = inTick
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

      UPDATE TraderPair
      SET
        symbols = symbols + tCount,
        sectors = sectors + tCount
      WHERE trader1 = IF(tBuyer < tSeller, tBuyer, tSeller) AND trader2 = IF(tBuyer < tSeller, tSeller, tBuyer);

      UPDATE TraderSymbol
#SET tradeWgt = tradeWgt * 1.1
      SET trades = trades + tCount
      WHERE (email = tBuyer OR email = tSeller) AND symbol = tSymbol;

      UPDATE TraderSector
      SET trades = trades + tCount
#SET tradeWgt = tradeWgt * 1.1
      WHERE (email = tBuyer OR email = tSeller) AND sector = tSector;

    END LOOP;

    CLOSE tpCursor;
  END //
DELIMITER ;




