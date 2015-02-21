$(function () {

    var email = $('#trader-email').attr('data-trader-email')

    $('#trades-table').dataTable({
        //"sDom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        //"dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        "dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
        //"dom": "t",
        //"dom": "lrtip",
        "ordering": false,
        //"autoWidth": false,
        //"bPaginate": true,
        //"bProcessing": true,
        "lengthMenu": [ 10,  25, 50, 100 ],
        serverSide: true,
        ajax: '/trades/query/'+email,
        "columns": [
            { "data": "id" },
        { "data": "time",
            "render": function (data) {
            var d = new Date(data)
            return d.toLocaleString()
            }},
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

$( "#trades-table_length").find("> label > select" ).removeClass( "input-sm" )

});