/**
 * Created by martin on 03/09/14.
 */

function format ( d ) {
    // `d` is the original data object for the row



    return '<table class="table table-bordered" cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
        '<th>'+
        '<td>Id</td>'+
        '<td>Factor</td>'+
        '</th>'+
        '<tr>'+
        '<td>Extension number:</td>'+
        '<td>'+'b'+'</td>'+
        '</tr>'+
        '<tr>'+
        '<td>Extra info:</td>'+
        '<td>And any further details here (images etc)...</td>'+
        '</tr>'+
        '</table>';
}


//$(function () {
$(document).ready(function() {
    var table = $('#clusters-table').DataTable({
        //"sDom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        //"dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        "dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        //"dom": "t",
        //"dom": "lrtip",
        "ordering": false,
        //"autoWidth": false,
        //"bPaginate": true,
        //"bProcessing": true,
        "lengthMenu": [ 10, 25, 50, 100 ],
        serverSide: true,
        ajax: '/clusters/query',
        "columns": [
            {
                "className": 'details-control',
                "orderable": false,
                "data": null,
                "defaultContent": ''
            },
            {
                "data": "cluster.clusterId",
                "render": function (data) {
                    return '<a href="/clusters/'+data+'">'+data+'</a>'
                }
            },
            {
                "data": "cluster.time",
                "render": function (data) {
                    var d = new Date(data)
                    return d.toLocaleString()
                }
            }
        ]
    });

    // Add event listener for opening and closing details
    $('#clusters-table tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = table.row(tr);

        if (row.child.isShown()) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
        }
        else {
            // Open this row
            row.child(format(row.data())).show();
            tr.addClass('shown');
        }
    });

    $("#clusters-table_length").find("> label > select").removeClass("input-sm")

});