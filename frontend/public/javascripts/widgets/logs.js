/**
 * Created by martin on 03/09/14.
 */



var tableVM;


$ ( function ( ) {

    //tradeTable = new TradeTable ( ) ;

    //tradeTable.getMeta()

    console.log("clusters.js");

    var data = $('#data')



    var show = false;

    options = {
        "ajax" :  {
            url: '/data/logs',
            meta: '/data/logs/meta',
            data: function(data) {

            }
        },
        "columns": [
            {
                "data": "id",
                "render": function (data, type, row) {
                    return '<a href="/clusters/'+data+'">'+data+'</a>'
                }
            },
            {
                "data": "time",
                "render": function (data) {
                    return moment(data).format(timeFormat)
                }
            },
            {
                "data": "type",
                "render": function (data) {
                    return data
                }
            },
            {
                "data": "message",
                "render": function (data) {
                    return data
                }
            },
/*            {
                "className": 'details-control',
                "orderable": false,
                "data": null,
                "defaultContent": ''
            },*/
        ]
    }

    //$('.selectpicker2').selectpicker()

    var id = '#clusters-table'

    tableVM = new DTModel(id, options)

    tableVM.getMetaData();
    tableVM.loadRows();
    ko.applyBindings ( tableVM, $(id)[0] ) ;
    tableVM.subscribe ( )


    console.log("postload: "+  tableVM.columns[2].filter())



    //tradeTable.loadRows ( ) ;





} ) ;