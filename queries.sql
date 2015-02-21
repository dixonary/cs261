#Queries#

#TRADER TABLE#
INSERT INTO Trader VALUES(?email,?domain,?avg1,?avg2,?avg3);

SELECT * FROM Trader WHERE email="?";
SELECT * FROM Trader WHERE domain="?";

UPDATE Trader SET avg1="?", avg2="?", avg3="?" WHERE domain="?";

#SYMBOL TABLE#
INSERT INTO Symbol VALUES(?symbol,?totalTrades,?avg1,?avg2,?avg3);

SELECT * FROM Symbol WHERE symbol="?";

UPDATE Symbol SET avg1="?", avg2="?", avg3="?" WHERE symbol="?";

#SECTOR TABLE#
INSERT INTO Sector VALUES(?symbol,?totalTrades,?avg1,?avg2,?avg3);

SELECT * FROM Sector WHERE symbol="?";

UPDATE Sector SET avg1="?", avg2="?", avg3="?" WHERE symbol="?";

#TRADE TABLE#
INSERT INTO Trade VALUES(?id,?time,?buyer,?seller,?price,?currency,?size,?symbol,?sector,?bid,?ask);

SELECT * FROM Trade WHERE id="?";
SELECT * FROM Trade WHERE buyer="?";
SELECT * FROM Trade WHERE seller="?";
SELECT * FROM Trade WHERE symbol="?";
SELECT * FROM Trade WHERE sector="?";
SELECT * FROM Trade WHERE time BETWEEN #?# AND #?#;

#COMMLINK TABLE#
INSERT INTO CommLink VALUES(?trader1,?trader2,?totalTrades,?avg1,?avg2,?avg3);

SELECT * FROM CommLink WHERE trader1="?" OR trader2="?";

UPDATE CommLink SET avg1="?", avg2="?", avg3="?" WHERE trader1="?" OR trader2="?";

#COMMUNICATION TABLE#
INSERT INTO Communication VALUES(?id,?time,?sender,?recipient);

SELECT * FROM Communication WHERE id="?";
SELECT * FROM Communication WHERE sender="?";
SELECT * FROM Communication WHERE recipient="?";
SELECT * FROM Trade WHERE time BETWEEN #?# AND #?#;

#FACTOR TABLE#
INSERT INTO Factor DEFAULT VALUES;

SELECT LAST_INSERT_ID(); #Apparently this is per connection to the database so shouldn't have an issue with insert before it is called

#TRADEFACTOR TABLE#
INSERT INTO TradeFactor VALUES(?tradeId,?factorId);

SELECT * FROM TradeFactor WHERE tradeId="?";
SELECT * FROM TradeFactor WHERE factorId="?";

#COMMFACTOR TABLE#
INSERT INTO CommFactor VALUES(?commId,?factorId);

SELECT * FROM CommFactor WHERE commId="?";
SELECT * FROM CommFactor WHERE factorId="?";

#EMAILSFACTOR TABLE#
INSERT INTO EmailsFactor VALUES(?factorId,?email1,?email2,?timeFrom,?timeTo);

SELECT * FROM EmailsFactor WHERE factorId="?";
SELECT * FROM EmailsFactor WHERE email1="?";
SELECT * FROM EmailsFactor WHERE email2="?";
SELECT * FROM EmailsFactor WHERE timeFrom<=#?# AND timeTo>=#?#;

