DELIMITER //
DROP PROCEDURE IF EXISTS Aggr;
CREATE PROCEDURE Aggr(inTick INT)
  BEGIN

    CALL AggrTrades(inTick);
    CALL AggrComms(inTick);
    CALL Prop(inTick);





  END //
DELIMITER ;



DELIMITER //
DROP PROCEDURE IF EXISTS Prop;
CREATE PROCEDURE Prop(inTick INT)
  BEGIN






# count up nodes, edges and the tick's events
    SELECT @traders := COUNT(*) FROM Trader;
    SELECT @symbols := COUNT(*) FROM Symbol;
    SELECT @sectors := COUNT(*) FROM Sector;

    SELECT @traderPairs := COUNT(*) FROM TraderPair;
    SELECT @traderSymbols := COUNT(*) FROM TraderSymbol;
    SELECT @traderSectors := COUNT(*) FROM TraderSector;

    SELECT @trades := COUNT(*) FROM Trade WHERE tick = inTick;
    SELECT @comms := COUNT(*) FROM Comm WHERE tick = inTick;


#propagate aggregate data to graph tables

    UPDATE TraderPair TP, AggrTraderCommon AC
    SET
      TP.common = AC.common,
      TP.commonBuys = AC.commonBuys,
      TP.commonSells = AC.commonSells
    WHERE
      TP.id = AC.edgeId;

    UPDATE TraderPair TP, AggrTraderComm ATC
    SET
      TP.comms = ATC.comms
    WHERE
      TP.id = ATC.edgeId;

    UPDATE Symbol S, AggrSymbol A
    SET
      S.trades = A.trades,
      S.buys = A.buys,
      S.sells = A.sells,
      S.tradesPerTrader = A.trades / @traders,
      S.buysPerTrader = A.buys / @traders,
      S.sellsPerTrader = A.trades / @traders
      WHERE S.id = A.symbolId;


    UPDATE TraderSymbol TS, AggrTrade AT
    SET
      TS.trades = AT.trades,
      TS.buys = AT.buys,
      TS.sells = AT.sells
    WHERE
      TS.id = AT.edgeId;




    SELECT @commonAvg := AVG(common) FROM AggrTraderCommon;
    SELECT @commonBuysAvg := AVG(commonBuys) FROM AggrTraderCommon;
    SELECT @commonSellsAvg := AVG(commonSells) FROM AggrTraderCommon;



    UPDATE Tick SET
      comms = @comms,
      trades = @trades,

      commonPerPair = @commonAvg,
      commonBuysPerPair = @commonBuysAvg,
      commonSellsPerPair = @commonSellsAvg,

      commsPerTrader = @comms / @traders,
      commsPerPair = @comms / @traderPairs
    WHERE `tick` = inTick;






  END //
DELIMITER ;




DELIMITER //
DROP PROCEDURE IF EXISTS AggrTrades;
CREATE PROCEDURE AggrTrades(inTick INT)
  BEGIN

    DELETE FROM AggrTrade;
    INSERT INTO
      AggrTrade
      (edgeId, traderId, symbolId, trades, buys, sells)
      SELECT
        TSE.id, TSE.traderId, TSE.symbolId, ATR.trades, ATR.buys, ATR.sells
      FROM
        TraderSymbol TSE
        JOIN (
               SELECT buyer as email, BUY.symbol, BUY.trades+SELL.trades as trades, BUY.trades as buys, SELL.trades as sells
               FROM (
                      SELECT TS.email as buyer, TS.symbol, IFNULL(TR.trades, 0) as trades FROM
                        (SELECT email, symbol FROM Trader JOIN Symbol) TS
                        LEFT OUTER JOIN
                        (SELECT buyer, symbol, count(*) as trades FROM Trade WHERE tick = inTick GROUP BY buyer, symbol) TR
                          ON email = buyer AND TS.symbol = TR.symbol
                    ) BUY
                 JOIN (
                        SELECT TS.email as seller, TS.symbol, IFNULL(TR.trades, 0) as trades FROM
                          (SELECT email, symbol FROM Trader JOIN Symbol) TS
                          LEFT OUTER JOIN
                          (SELECT seller, symbol, count(*) as trades FROM Trade WHERE tick = inTick GROUP BY seller, symbol) TR
                            ON email = seller AND TS.symbol = TR.symbol
                      ) SELL
                   ON buyer = seller AND BUY.symbol = SELL.symbol
             ) ATR
          ON TSE.email = ATR.email AND TSE.symbol = ATR.symbol;




    #select symbol, sum(x*fx)/sum(fx) from (select symbol, trades as x, count(*) as fx from TraderSymbolEdge group by symbol, trades) d group by symbol;
    #select symbol, sum(x*fx)/sum(fx) from (select symbol, trades as x, count(*) as fx from AggrTrades group by symbolId, trades) d group by symbol;

    DELETE FROM AggrSymbol;
    INSERT INTO AggrSymbol
    (symbolId, trades, buys, sells)
      SELECT
        symbolId, sum(trades), sum(buys), sum(sells)
      FROM AggrTrade GROUP BY symbolId;


/*    DELETE FROM AggrSector;
    INSERT INTO AggrSector
    (sectorId, trades, buys, sells)
      SELECT
        sectorId, sum(trades), sum(buys), sum(sells)
      FROM AggrTrade GROUP BY sectorId;*/



    DELETE FROM AggrTraderCommon;
    INSERT INTO AggrTraderCommon
    (edgeId, trader1Id, trader2Id, common, commonBuys, commonSells)
      SELECT
        TTE.id, TTE.trader1Id, TTE.trader2Id,
        IFNULL(TTC.symbols, 0),
        IFNULL(TTC.symbolBuys, 0),
        IFNULL(TTC.symbolSells, 0)
      FROM
        TraderPair TTE
        LEFT OUTER JOIN (
                          SELECT
                            A.traderId AS trader1Id, B.traderId AS trader2Id,
                            COUNT(*) as symbols,
                            SUM(IF(A.buys > 0 AND B.buys > 0, 1, 0)) as symbolBuys,
                            SUM(IF(A.sells > 0 AND B.sells > 0, 1, 0 )) as symbolSells
                          FROM AggrTrade A
                            JOIN AggrTrade B
                              ON A.traderId != B.traderId AND A.symbolId = B.symbolId
                          WHERE
                            A.trades > 0 AND B.trades > 0
                          GROUP BY A.traderId, B.traderId
                          ORDER BY symbols ASC
                        ) TTC
#ON TTE.trader1 = TTC.trader1 AND TTE.trader2 = TTC.trader2;
          ON TTE.trader1Id = TTC.trader1Id AND TTE.trader2Id = TTC.trader2Id;








  END //
DELIMITER ;







DELIMITER //
DROP PROCEDURE IF EXISTS AggrComms;
CREATE PROCEDURE AggrComms(inTick INT)
  BEGIN


    DELETE FROM AggrComm;
    INSERT INTO AggrComm
    (traderId, comms, sends, recs)
      SELECT
        T.id,
        sum(sent),
        sum(IF(T.email = S.sender, sent, 0)) AS sent,
        sum(IF(T.email = S.recipient, sent, 0)) AS rec
      FROM Trader T
        LEFT OUTER JOIN
        (          SELECT sender, recipient, count(*) as sent from Comm where tick = inTick group by sender, recipient        )
        S ON
                    (T.email = sender) OR (T.email = recipient)
      GROUP BY T.email;




    DELETE FROM AggrTraderComm;
    INSERT INTO AggrTraderComm
    (edgeId, trader1Id, trader2Id, comms, sent1, sent2)
      SELECT
        TTE.id, TTE.trader1Id, TTE.trader2Id,
        sum(sent),
        sum(IF(D.sender = TTE.trader1, sent, 0)) AS sent1,
        sum(IF(D.sender = TTE.trader2, sent, 0)) AS sent2
      FROM TraderPair TTE
        LEFT OUTER JOIN
        (
          SELECT sender, recipient, count(*) as sent from Comm where tick = inTick group by sender, recipient
        )
        D ON
            (TTE.trader1 = sender and TTE.trader2 = recipient) OR (TTE.trader1 = recipient AND TTE.trader2 = sender)
      group by TTE.trader1, TTE.trader2;






  END //
DELIMITER ;

