DELIMITER //
DROP PROCEDURE IF EXISTS UpdateSymbolTradeTotal;
CREATE PROCEDURE UpdateSymbolTradeTotal(inSymbol TEXT)
  BEGIN
    SELECT @counterId := tradeCnt FROM Symbol WHERE symbol = inSymbol;
    SELECT @trades := COUNT(*) FROM Trade WHERE symbol = inSymbol;

    UPDATE Counter SET val = @trades WHERE id = @counterId;
  END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS UpdateSymbolTradeRate1;
CREATE PROCEDURE UpdateSymbolTradeRate1(inSymbol TEXT, t1 LONG, t2 LONG)
  BEGIN
    SELECT @counterId := tradeCnt FROM Symbol WHERE symbol = inSymbol;
    SELECT @trades := COUNT(*) FROM Trade WHERE symbol = inSymbol AND time BETWEEN t1 AND t2;

    UPDATE Counter SET avg1 = (@trades / ((t2-t1)/(1000))) WHERE id = @counterId;
  END //
DELIMITER ;

DELIMITER //
DROP PROCEDURE IF EXISTS UpdateSymbolTradeRate2;
CREATE PROCEDURE UpdateSymbolTradeRate2(inSymbol TEXT, t1 LONG, t2 LONG)
  BEGIN
    SELECT @counterId := tradeCnt FROM Symbol WHERE symbol = inSymbol;
    SELECT @trades := COUNT(*) FROM Trade WHERE symbol = inSymbol AND time BETWEEN t1 AND t2;

    UPDATE Counter SET avg2 = (@trades / ((t2-t1)/(1000))) WHERE id = @counterId;
  END //
DELIMITER ;











DELIMITER //
DROP PROCEDURE IF EXISTS UpdateSymbols;
CREATE PROCEDURE UpdateSymbols( from1 BIGINT, from2 BIGINT, end BIGINT)
  BEGIN
    DECLARE done INT DEFAULT FALSE;
    DECLARE sSymbol VARCHAR(50);
    DECLARE tTradeCnt INT;
    DECLARE tpCursor CURSOR FOR SELECT symbol, tradeCnt FROM Symbol;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    OPEN tpCursor;

    tpLoop: LOOP
    FETCH tpCursor INTO sSymbol, tTradeCnt;

    IF done THEN
      LEAVE tpLoop;
    END IF;
      SELECT @tradesA := COUNT(*) FROM Trade WHERE symbol = sSymbol AND time BETWEEN from1 AND end;
      SELECT @tradesB := COUNT(*) FROM Trade WHERE symbol = sSymbol AND time BETWEEN from2 AND end;

      UPDATE Counter
      SET avg1 = (@tradesA / ((end-from1)/(1000))), avg2 = (@tradesB / ((end-from2)/(1000)))
      WHERE id = tTradeCnt;
    END LOOP;

    CLOSE tpCursor;
  END //
DELIMITER ;







DELIMITER //
DROP PROCEDURE IF EXISTS UpdateTraderPairs;
CREATE PROCEDURE UpdateTraderPairs( wStart1 BIGINT, wStart2 BIGINT, wEnd BIGINT)
  BEGIN


    DECLARE done INT DEFAULT FALSE;
    DECLARE tEmail1 VARCHAR(50);
    DECLARE tEmail2 VARCHAR(50);
    DECLARE tStockCnt INT;
    DECLARE tTradeCnt INT;
    DECLARE tCommCnt INT;
    DECLARE tpCursor CURSOR FOR SELECT trader1, trader2, stockCnt, tradeCnt, commCnt FROM TraderPair;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;



    OPEN tpCursor;

    tpLoop: LOOP
      FETCH tpCursor INTO tEmail1, tEmail2, tStockCnt, tTradeCnt, tCommCnt;

      IF done THEN
        LEAVE tpLoop;
      END IF;

      #SELECT @trades := COUNT(*) FROM Trade WHERE symbol = inSymbol AND time BETWEEN t1 AND t2;

      #stocks
      SELECT @stocksA := COUNT(*) FROM Trade
      WHERE time BETWEEN wStart1 and wEnd GROUP BY symbol, buyer, seller
      HAVING (buyer = tEmail1 OR seller = tEmail1) AND (buyer = tEmail2 AND seller = tEmail2);

      SELECT @stocksB := COUNT(*) FROM Trade
      WHERE  time BETWEEN wStart2 and wEnd GROUP BY symbol, buyer, seller
      HAVING (buyer = tEmail1 OR seller = tEmail1) AND (buyer = tEmail2 AND seller = tEmail2);

      UPDATE Counter
      SET avg1 = (@stocksA / ((wEnd-wStart1)/(1000))), avg2 = (@stocksB / ((wEnd-wStart2)/(1000)))
#SET val = 5 #, avg1 = (@commsA), avg2 = (@commsB)
      WHERE id = tStockCnt;




      #trades
    SELECT @tradesA := COUNT(*) FROM Trade
    WHERE (buyer = tEmail1 OR seller = tEmail1) AND (buyer = tEmail2 AND seller = tEmail2)
          AND time BETWEEN wStart1 and wEnd;

    SELECT @tradesB := COUNT(*) FROM Trade
    WHERE (buyer = tEmail1 OR seller = tEmail1) AND (buyer = tEmail2 AND seller = tEmail2)
          AND time BETWEEN wStart2 and wEnd;

    UPDATE Counter
    SET avg1 = (@tradesA / ((wEnd-wStart1)/(1000))), avg2 = (@tradesB / ((wEnd-wStart2)/(1000)))
#SET val = 5 #, avg1 = (@commsA), avg2 = (@commsB)
    WHERE id = tTradeCnt;


      SELECT @commsA := COUNT(*) FROM Comm
      WHERE ((sender = tEmail1 AND recipient = tEmail2) OR (sender = tEmail2 AND recipient = tEmail1))
            AND time BETWEEN wStart1 and wEnd;

      SELECT @commsB := COUNT(*) FROM Comm
      WHERE ((sender = tEmail1 AND recipient = tEmail2) OR (sender = tEmail2 AND recipient = tEmail1))
            AND time BETWEEN wStart2 and wEnd;

      #UPDATE Counter SET val = commCnt;

      UPDATE Counter
      SET avg1 = (@commsA / ((wEnd-wStart1)/(1000))), avg2 = (@commsB / ((wEnd-wStart2)/(1000)))
      #SET val = 5 #, avg1 = (@commsA), avg2 = (@commsB)
      WHERE id = tCommCnt;

    END LOOP;

    CLOSE tpCursor;




  END //
DELIMITER ;