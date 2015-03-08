/**
 * Created by martin on 03/09/14.
 */

function format(d) {
    // `d` is the original data object for the row


    return '<table class="table table-bordered" cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
        '<th>' +
        '<td>Id</td>' +
        '<td>Factor</td>' +
        '</th>' +
        '<tr>' +
        '<td>Extension number:</td>' +
        '<td>' + 'b' + '</td>' +
        '</tr>' +
        '<tr>' +
        '<td>Extra info:</td>' +
        '<td>And any further details here (images etc)...</td>' +
        '</tr>' +
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

    self.factorSelect = ko.observableArray();

    self.filters = {
        "daterange": ko.observable(""),
        "classes": ko.observable("nada"),
        "x": ko.observable(0),
        "centile": ko.observable(0),
        "sig": ko.observable(0)
    };

    self.columnFilters = [
        {
            column: 1,
            name: "timerange",
            observable: self.filters.daterange
        },
        {
            column: 2,
            name: "types",
            observable: self.filters.classes
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
        var u = new URI();


        var catalog = $('#factors-table').DataTable();
        //catalog.fnFilter(self.emailFilter(), 0);

        console.log('filters called');
        $.each(self.columnFilters, function (index, filter) {

            //window.location = u.setSearch(filter.name, filter.observable()).toString()

            console.log(JSON.stringify(filter));

            catalog.column(filter.column).search(filter.observable()).draw()
        });


    };


    //Date range picker with time picker

    var drp = $('#clusters-daterange')

    //var startDate = moment().startOf('day')
    //var endDate = moment().endOf('day')
    var startDate = moment().startOf('year')
    var endDate = moment().startOf('day').add('month', 1)

    function onApply(start, end, label) {
        var daterange = start + ',' + end

        console.log("on apply");

        drp.find('span').html(start.format(timeFormat) + ' - ' + end.format(timeFormat));

        self.filters.daterange(daterange)
    }

    drp.daterangepicker(
        {
            //startDate: startDate,
            //endDate: endDate,
            showDropdowns: true,
            timePicker: true,
            timePickerIncrement: 1,
            timePicker12Hour: false,
            ranges: {
                'Today': [ moment().startOf('day'), moment().endOf('day') ],
                'Yesterday': [ moment().startOf('day').subtract('days', 1), moment().endOf('day').subtract('days', 1) ],
                'This Week': [moment().startOf('week'), moment().endOf('week')],
                'This Month': [moment().startOf('month'), moment().endOf('month')]
            },

            format: timeFormat,
            locale: { cancelLabel: 'Clear' }
        },
        onApply
    );

    drp.on('cancel.daterangepicker', function (ev, picker) {
        console.log("cancel");
        //do something, like clearing an input
        $('#clusters-daterange').val('');
    });


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


    //self.filters.daterange.subscribe(self.applyFilters);
    //self.filters.classes.subscribe(self.applyFilters);

    self.state = null;

    self.loadRows = function () {
        var table = $('#factors-table').DataTable({
            //"sDom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
            //"dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
            "dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
            //"dom": "t",
            //"dom": "lrtip",
            "ordering": true,

            //"autoWidth": false,
            //"bPaginate": true,
            //"bProcessing": true,
            "lengthMenu": [ 20, 50, 100 ],
            serverSide: true,
            //ajax: '/factors/query',
            //ajax: '/data/factors',
            ajax :  {
                url: '/data/factors',
                data: function(data) {
                    //data.clusterId = 1572;
                }
            },


            "stateSave": true,
            "stateLoadParams": function (settings, data) {
                self.state = settings;

                console.log("stateLoadParams")
                //console.log("stateLoadParams: " + JSON.stringify(data));

                var query = new URI().search(true);
                //console.log("query: " + JSON.stringify(query));

                //if('start' in query)
                //  data.start = query.start


                //o.columns = []
                $.each(self.columnFilters, function (index, filter) {
                    //console.log("applying " + filter.name + " value: " + query[filter.name])
                    //filter.observable(filter.parse(query[filter.name]));

                    data.columns[filter.column].search.search = query[filter.name]

                    //console.log("APplying: " + query[filter.name])
                    //filter.apply(query[filter.name])


                    /*o.columns[filter.column] = {
                     search: {
                     search:
                     }
                     }*/
                });

                //console.log("o: " + JSON.stringify(data))

                return data;
            },

            "stateSaveParams": function (settings, data) {
                console.log("stateSaveParams")
                //console.log("stateSaveCallback: " + JSON.stringify(data))

                var u = new URI();

                u.setSearch({
                    start: data.start,
                    length: data.length
                });

                $.each(self.columnFilters, function (index, filter) {
                    var colSearch = data.columns[filter.column].search.search

                    u.setSearch(filter.name, colSearch).toString()

                    //u.setSearch(filter.name, filter.serialize()).toString()

//                    var colSearch = data.columns[filter.column].search.search


                    console.log(JSON.stringify(filter));

                    //catalog.column(filter.column).search(filter.observable()).draw()

                });

                history.replaceState(null, null, u.toString())
            },


            "columns": [
                /*                {
                 "className": 'details-control',
                 "orderable": false,
                 "data": null,
                 "defaultContent": ''
                 },*/
                {
                    "data": "id",
                    "render": function (data) {
                        return '<a href="/factors/' + data + '">' + data + '</a>'
                    }
                },
                {
                    "data": "time",
                    "render": function (data, type, row) {
                        //return moment(data).format(timeFormat)
                        return '<a href="/ticks/' + row.tick + '">' + moment(data).format(timeFormat) + '</a>'
                    }
                },
                {
                    "data": "factorLabel",
                    "render": function (data) {
                        return data;
                    }
                },
                {
                    "width": "25%",
                    "data": "edge",
                    "render": function (data, index, row) {
                        //return moment(data).format(timeFormat)
                        //return JSON.stringify(row)
                        return row.source.label + " -> " + row.target.label
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
        });

        var tabE = $('#factors-table')
        tabE.on('search.dt', function () {
            //var api = this.api();

            console.log("searching")

        });

        table.on('stateLoaded.dt', function (e, settings, data) {

            console.log("init.dt")
            //console.log("init.dt: "+ JSON.stringify(data))


            $.each(self.columnFilters, function (index, filter) {


                //console.log(JSON.stringify(filter.serialize()));

                //var colSearch = data.columns[filter.column].search.search

                //u.setSearch(filter.name, colSearch).toString()

                //filter.apply(query[filter.name])
                //catalog.column(filter.column).search(filter.observable()).draw()

            });
            //$('#myInput').val( data.myCustomValue );
        });


        // Add event listener for opening and closing details
        $('#factors-table tbody').on('click', 'td.details-control', function () {
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

        $("#factors-table_length").find("> label > select").removeClass("input-sm")
    };


    self.loadFilters = function () {
        console.log("load filters!")


        //$.get("/factors/meta", );

        $.ajax({
            "url": "/data/factors/meta",
            "async": false,
            "dataType": "json",
            "success": function (data) {
                //console.log(JSON.stringify(data));
                $.each(data.filters.factors, function (index, item) {
                    //console.log(JSON.stringify(item));
                    self.factorSelect.push(new Item(item.value, item.label, item.group))
                });
            }
        });


        $.each(self.columnFilters, function (index, filter) {
            //var colSearch = data.columns[filter.column].search.search

            var tabE = $('#factors-table').DataTable()

            var colSearch = tabE.column(filter.column).search()
            console.log("search: " + colSearch)

            //filter.apply(colSearch)
            filter.observable(colSearch)

            //u.setSearch(filter.name, filter.serialize()).toString()

//                    var colSearch = data.columns[filter.column].search.search


            //console.log(JSON.stringify(filter));

            //catalog.column(filter.column).search(filter.observable()).draw()

        });

        $.each(self.columnFilters, function (index, filter) {
            filter.observable.subscribe(self.applyFilters);
        });

    }

}


$(document).ready(function () {

    console.log("b");

    viewModel = new ViewModel();

    viewModel.loadRows();

    viewModel.loadFilters()

    ko.applyBindings(viewModel);


    //console.log("f: " + viewModel.filters.classes())


    //viewModel.applyUrlFilters();


});