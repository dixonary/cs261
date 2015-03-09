/**
 * Created by martin on 03/09/14.
 */



var factorTable;


$ ( function ( ) {

    //tradeTable = new TradeTable ( ) ;

    //tradeTable.getMeta()

    console.log("clusters.js");

    var data = $('#data')



    var show = false;

    options = {
        "ajax" :  {
            url: '/data/clusters',
            meta: '/data/clusters/meta',
            data: function(data) {

            }
        },
        "columns": [
            {
                "className": 'details-control',
                "orderable": false,
                "data": null,
                "defaultContent": ''
            },
            {
                "data": "id",
                "render": function (data) {
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
                "data": "time",
                "render": function (data) {
                    return moment(data).format(timeFormat)
                }
            }
        ]
    }

    //$('.selectpicker2').selectpicker()

    var id = '#clusters-table'

    factorTable = new DTModel(id, options)

    factorTable.loadMeta();
    factorTable.loadRows();
    factorTable.observables ( )
    ko.applyBindings ( factorTable, $(id)[0] ) ;
    factorTable.subscribe ( )


    console.log("postload: "+  factorTable.columns[2].filter())



    //tradeTable.loadRows ( ) ;





} ) ;