var options;
var tradeTable;


$(function () {

    //tradeTable = new TradeTable ( ) ;

    //tradeTable.getMeta()

    console.log("trades.js");


    options = {
        "ajax": {
            "url": "/data/trades",
            "meta": "/data/trades/meta"
        },
        "columns": [
            {
                "data": "id"
            },
            {
                "data": "time",
                "render": function (data) {
                    return moment(data).format(timeFormat)
                }
            },
            {
                "data": "buyer",
                "render": function (data, type, row) {
                    //return '<a href="/traders/' + data + '">' + data + '</a>'
                    var url = jsRoutes.controllers.Application.tradesBy(null, row.buyerId, null, null, null).url;
                    return '<a href="' + url + '">' + data + '</a>'
                }
            },
            {
                "data": "seller",
                "render": function (data, type, row) {
                    //return '<a href="/traders/' + data + '">' + data + '</a>'
                    var url = jsRoutes.controllers.Application.tradesBy(null, null, row.sellerId, null, null).url;
                    return '<a href="' + url + '">' + data + '</a>'
                }
            },
            {
                "data": "price",
                "render": function (data) {
                    return Math.round(data * 100) / 100
                }
            },
            { "data": "size" },
            { "data": "currency" },
            {
                "data": "symbol",
                "render": function (data, type, row) {
                    //return '<a href="/traders/' + data + '">' + data + '</a>'
                    var url = jsRoutes.controllers.Application.tradesBy(null, null, null, row.symbolId, null).url;
                    return '<a href="' + url + '">' + data + '</a>'
                }
            },
            {
                "data": "sector",
                "render": function (data, type, row) {
                    //return '<a href="/traders/' + data + '">' + data + '</a>'
                    var url = jsRoutes.controllers.Application.tradesBy(null, null, null, null, row.sectorId).url;
                    return '<a href="' + url + '">' + data + '</a>'
                }
            },
            {
                "data": "bid",
                "render": function (data) {
                    return Math.round(data * 100) / 100
                }
            },
            {
                "data": "ask",
                "render": function (data) {
                    return Math.round(data * 100) / 100
                }
            }
        ]
    }

    //$('.selectpicker2').selectpicker()

    tradeTable = new DTModel('#trades-table', options)

    tradeTable.daterangepickerOptions = defaultDrpOptions()

    tradeTable.getMetaData();
    tradeTable.loadRows();
    ko.applyBindings(tradeTable);
    tradeTable.subscribe()

    //tradeTable.loadRows ( ) ;


});