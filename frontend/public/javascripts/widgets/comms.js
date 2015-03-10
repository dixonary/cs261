var options;
var tableVM;


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

    tableVM = new DTModel('#comms-table', options)

    tableVM.getMetaData();
    tableVM.loadRows();
    tableVM.observables();
    ko.applyBindings(tableVM);
    tableVM.subscribe()





    //tradeTable.loadRows ( ) ;


});