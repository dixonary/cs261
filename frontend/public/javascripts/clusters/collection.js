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
    };

    self.columnFilters = [
        {
            column: 1,
            observable: self.filters.daterange
        }
    ];

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
        var catalog = $('#clusters-table').DataTable();

        $.each(self.columnFilters, function (index, filter) {
            catalog.column(filter.column).search(filter.observable()).draw()
        });
    };



    //Date range picker with time picker

    var drp =$('#clusters-daterange')

    //var startDate = moment().startOf('day')
    //var endDate = moment().endOf('day')
    var startDate = moment().startOf('year')
    var endDate = moment().startOf('day').add('month', 1)

    function onApply(start, end, label) {
        var daterange = start + ',' + end

        drp.find('span').html(start.format(timeFormat) + ' - ' + end.format(timeFormat));

        self.filters.daterange(daterange)
    }

    drp.daterangepicker(
        {
            startDate: startDate,
            endDate: endDate,
            showDropdowns: true,
            timePicker: true,
            timePickerIncrement: 1,
            timePicker12Hour: false,
            ranges: {
                'Today': [ moment().startOf('day'),  moment().endOf('day') ],
                'Yesterday': [ moment().startOf('day').subtract('days', 1),  moment().endOf('day').subtract('days', 1) ],
                'This Week': [moment().startOf('week'), moment().endOf('week')],
                'This Month': [moment().startOf('month'), moment().endOf('month')]
            },

            format: timeFormat,
            locale: 'en'
        },
        onApply
    );

    var drp_data = drp.data('daterangepicker')

    drp_data.setStartDate(startDate)
    drp_data.setEndDate(endDate)
    onApply(startDate, endDate)
    //$('#clusters-daterange span').html(start.format(timeFormat) + ' - ' + end.format(timeFormat));


    //for (i = 0; i < self.filters.length; i++) {
    /*for (var f in self.filters) {
        console.log("Filter2: " + f)
        self.filters[f].extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
    }*/
    self.filters.daterange.subscribe(self.applyFilters);

    self.loadRows = function () {
        var table = $('#clusters-table').DataTable({
            //"sDom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
            //"dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
            "dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
            //"dom": "t",
            //"dom": "lrtip",
            "ordering": true,
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