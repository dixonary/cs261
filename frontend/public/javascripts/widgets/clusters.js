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
            url: '/data/clusters',
            meta: '/data/clusters/meta',
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
                "data": "status",
                "className": "text-center",
                "render": function (data, type, row) {
                    var c;
                    var t;

                    switch(row.status) {
                        case "UNSEEN":
                            c = "label-default"
                            t = "New"
                            break;
                        case "SEEN":
                            c = "label-info"
                            t = "Pending"
                            break;
                        case "INVESTIGATED":
                            c = "label-success"
                            t = "Investigated"
                            break;
                    }

                    return '<span class="label '+c+'" >'+t+'</span>'
                }
            },
            {
                "data": "time",
                "render": function (data) {
                    return moment(data).format(timeFormat)
                }
            },
            {
                "data": "nodes",
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