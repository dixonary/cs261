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

                    var time = row.start+","+row.end
                    var traders = row.source.id + "," + row.target.id

                    values = {
                        "_traders": traders
                    };

                    var link, icon;
                    switch(data.factor) {
                        case "COMMON":
                            link = jsRoutes.controllers.Application.tradesBy(time, traders, traders)
                            icon = "fa-exchange"
                            break;
                        case "COMMON_BUYS":
                            link = jsRoutes.controllers.Application.tradesBy(time, traders, null)
                            icon = "fa-exchange"
                            break;
                        case "COMMON_SELLS":
                            link = jsRoutes.controllers.Application.tradesBy(time, null, traders)
                            icon = "fa-exchange"
                            break;
                        case "COMMS":
                            link = jsRoutes.controllers.Application.commsBy(time, traders, traders)
                            icon = "fa-envelope"
                            break;
                    }



                    console.log("values: " + JSON.stringify(values))

                    q = fillString(data.query, values)

                    console.log("q: " + q)

                    var href = '<a href="' + link.url + '"' +
                                'class="btn btn-xs btn-primary btn-flat pull-right">' +
                                'View' +
                                '</a>'

                    //return data.label + " <a href='"+q+"'>(view events)</a>"
                    //return data.label + " <a href='"+link.url+"'>(view events)</a>"

                    //return data.label + " " + href

                    return data.label + " " + genIconButton(link.url, '', icon)
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
                    //return label + " <a href='"+q+"'>(view factors)</a>"

                    var edges = row.source.id + "," + row.target.id;

                    var link = jsRoutes.controllers.Application.factorsBy(edges);

                    var href = '<a href="' + link.url + '"' +
                        'class="btn btn-xs btn-primary btn-flat pull-right">' +
                        'View Factors&nbsp;<i class="fa fa-cube"></i>' +
                        '</a>'

                    return label + " " + genIconButton(link.url, '', 'fa-cube')

                    //return data.label + " <a href='"+q+"'>(view events)</a>"
                    //return data.label + " <a href='"+link.url+"'>(view events)</a>"

                    return label + " " + href
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

    factorTable.getMetaData();
    factorTable.loadRows();
    ko.applyBindings ( factorTable, $(id)[0] ) ;
    factorTable.subscribe ( )


    console.log("postload: "+  factorTable.columns[2].filter())



    //tradeTable.loadRows ( ) ;





} ) ;