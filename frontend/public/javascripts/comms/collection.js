/**
 * Created by martin on 03/09/14.
 */




$(function () {
    $('#comms-table').dataTable({
        //"sDom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        //"dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        "dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        //"dom": "t",
        //"dom": "lrtip",
        "ordering": false,
        //"autoWidth": false,
        //"bPaginate": true,
        //"bProcessing": true,
        "lengthMenu": [ 25, 50, 100, 200 ],
        serverSide: true,
        ajax: '/comms/query',
        "columns": [
            { "data": "id" },
            { "data": "time",
                "render": function (data) {
                    console.log("data: " + data)
                    return moment(data).format(timeFormat)
                }
            },
            {
                "data": "sender",
                "render": function (data) {
                    return '<a href="/traders/' + data + '">' + data + '</a>'
                }
            },
            {
                "data": "recipient",
                "render": function (data) {
                    return '<a href="/traders/' + data + '">' + data + '</a>'
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

    $("#comms-table_length").find("> label > select").removeClass("input-sm")

});