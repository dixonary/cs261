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

/*

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
*/









var viewModel;

function ViewModel() {
    var self = this;

    self.filters = {
        "daterange" : ko.observable("")
    }


    self.showFilterOptions = ko.observable(true);
    self.filterClassName = ko.computed(function () {
        if (self.showFilterOptions()) {
            return "glyphicon glyphicon-chevron-up";
        } else {
            return "glyphicon glyphicon-chevron-down";
        }
    });
    self.toggleFilterOptions = function () {
        self.showFilterOptions(!self.showFilterOptions());
    };
    self.applyFilters = function () {
        console.log("Applying filters")

        var catalog = $('#clusters-table').DataTable();
        //catalog.fnFilter(self.emailFilter(), 0);



        i = 0
        for (var f in self.filters) {
            console.log("Filter: " + f)

            //catalog.fnFilter(self.filters[f](), i);
            // Apply the search
            catalog.column( i).search( self.filters[f]()).draw()

            /*table.columns().eq( 0 ).each( function ( colIdx ) {
                $( 'input', table.column( colIdx ).footer() ).on( 'keyup change', function () {
                    table
                        .column( colIdx )
                        .search( this.value )
                        .draw();
                } );
            } );*/

            i++
        }

        console.log('filters called');
    };



    //Date range picker with time picker
    $('#reservationtime').daterangepicker(
        {timePicker: true, timePickerIncrement: 30, format: 'MM/DD/YYYY h:mm A'},
        function(start, end, label) {
            //alert('A date range was chosen: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
            //var dateRangeText = start.toString('MM/d/yy') + ' - ' + end.toString('MM/d/yy');
            //var dateRangeText = start.toString('MM/DD/YYYY h:mm A') + ',' + end.toString('MM/DD/YYYY h:mm A');
            //console.log(dateRangeText)

            var daterange = start + ',' + end

            self.filters.daterange(daterange)
            //self.filters.daterange(dateRangeText);
        }
    );




    //for (i = 0; i < self.filters.length; i++) {
    /*for (var f in self.filters) {
        console.log("Filter2: " + f)
        self.filters[f].extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
    }*/
    //self.filters.daterange.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
    //self.filters.daterange.start.subscribe(self.applyFilters);
    self.filters.daterange.subscribe(self.applyFilters);

    /*self.filters.email.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
     self.filters.type.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
     self.filters.status.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
     self.filters.level.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);*/
    //self.regionFilter.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
    //self.nameFilter.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);

    self.loadRows = function () {
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
    };


}


$(document).ready(function () {

    console.log("b");

    viewModel = new ViewModel();
    viewModel.loadRows();

    ko.applyBindings(viewModel);
});