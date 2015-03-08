/**
 * Created by martin on 03/09/14.
 */



var factorTable;


$ ( function ( ) {

    //tradeTable = new TradeTable ( ) ;

    //tradeTable.getMeta()

    console.log("factors.js");

    var data = $('#data')



    var clusterId = $('#data').attr('data-cluster-id');

    var show = false;

    options = {
        "ajax" :  {
            url: '/data/factors',
            meta: '/data/factors/meta',
            data: function(data) {
                data.clusterId = clusterId;
            }
        },
        "columns": [
            {
                "data": "id",
                "render": function (data) {
                    return '<a href="/factors/' + data + '">' + data + '</a>'
                },
                "visible": show
            },
            {
                "data": "time",
                "render": function (data, type, row) {
                    //return moment(data).format(timeFormat)
                    return '<a href="/ticks/' + row.tick + '">' + moment(data).format(timeFormat) + '</a>'
                },
                "visible": show
            },
            {
                "data": "factor",
                "render": function (data, index, row) {
                    //return data.render;

                    console.log("template: " + data.query)

                    values = {
                        "_traders": row.source.id + "," + row.target.id
                    };

                    console.log("values: " + JSON.stringify(values))

                    q = fillString(data.query, values)

                    console.log("q: " + q)

                    return data.label + " <a href='"+q+"'>(view events)</a>"
                }
            },
            {
                /*"width": "25%",*/
                "data": "edge",
                "render": function (data, index, row) {
                    //return moment(data).format(timeFormat)
                    //return JSON.stringify(row)
                    //return row.source.label + " -> " + row.target.label

                    var label =row.source.label + " -> " + row.target.label


                    return label + " <a href='"+q+"'>(view factors)</a>"
                },
                "orderable": false
            },
            {
                "data": "value",
                "render": function (data) {
                    return data;
                }
            },
            {
                "data": "centile",
                "render": function (data) {
                    return parseFloat(data * 100).toFixed(2) + "%";
                }
            },
            {
                "data": "sig",
                "render": function (data) {
                    //return data;
                    return parseFloat(data * 100).toFixed(2) + "%";
                }
            }


            /*                {
             "data": "cluster.clusterId",
             "render": function (data) {
             return '<a href="/factors/'+data+'">'+data+'</a>'
             }
             },*/

        ]
    }

    //$('.selectpicker2').selectpicker()

    var id = '#factors-table'

    factorTable = new DTModel('#factors-table', options)

    factorTable.loadMeta();
    factorTable.loadRows();

    ko.applyBindings ( factorTable, $(id)[0] ) ;

    factorTable.subscribe ( )

    console.log("postload: "+  factorTable.columns[2].filter())



    //tradeTable.loadRows ( ) ;





} ) ;