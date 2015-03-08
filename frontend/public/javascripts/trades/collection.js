/**
 * Created by martin on 03/09/14.
 */




$(function () {
    $('#trades-table').dataTable({
        "dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        "ordering": true,
        "lengthMenu": [ 25, 50, 100, 200 ],
        serverSide: true,
        ajax: '/data/trades',
        "columns": [
            { "data": "id" },
            { "data": "time",
                "render": function (data) {
                    return moment(data).format(timeFormat)
                }
            },
            {
                "data": "buyer",
                "render": function (data) {
                    return '<a href="/traders/' + data + '">' + data + '</a>'
                }
            },
            {
                "data": "seller",
                "render": function (data) {
                    return '<a href="/traders/' + data + '">' + data + '</a>'
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
            { "data": "symbol" },
            { "data": "sector" },
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
        //"bServerSide": true,
        //"sAjaxSource": "/trades/query"


        //"bFilter" : false,
        //"bSort" : true,
        //"bInfo" : true,
        //"bAutoWidth" : false
    });

    $("#trades-table_length").find("> label > select").removeClass("input-sm")

});