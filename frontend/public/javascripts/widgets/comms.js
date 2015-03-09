var options;
var commTable;


$(function () {

    //tradeTable = new TradeTable ( ) ;

    //tradeTable.getMeta()

    console.log("comms.js");


    options = {
        "ajax": {
            "url": "/data/comms",
            "meta": "/data/comms/meta"
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
                "data": "sender",
                "render": function (data, type, row) {
                    //return '<a href="/traders/' + data + '">' + data + '</a>'
                    var url = jsRoutes.controllers.Application.commsBy(row.senderId, null).url;
                    return '<a href="' + url + '">' + data + '</a>'
                }
            },
            {
                "data": "recipient",
                "render": function (data, type, row) {
                    var url = jsRoutes.controllers.Application.commsBy(null, row.recipientId).url;
                    return '<a href="' + url + '">' + data + '</a>'
                }
            }
        ]
    }

    //$('.selectpicker2').selectpicker()

    commTable = new DTModel('#comms-table', options)

    commTable.loadMeta();
    commTable.loadRows();
    commTable.observables();
    ko.applyBindings(commTable);
    commTable.subscribe()





    //tradeTable.loadRows ( ) ;


});