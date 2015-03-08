/**
 * Created by martin on 03/09/14.
 */



function DTModel(id, options) {
    var self = this;

    self.options = options;

    self.columns = [];

    self.loadMeta = function () {
        $.ajax({
            "url": options.ajax.meta,
            "async": false,
            "dataType": "json",
            "success": function (data) {
                self.source = data.source;
                self.columns = data.columns;

                $.each(self.columns, function (index, column) {


                    //if ('canFilter' in column) {
                    /*                    if(column.canFilter) {
                     column.domain = ko.mapping.fromJS(column.domain)
                     if (column.multi) {
                     column.filter = ko.observableArray()
                     } else {
                     column.filter = ko.observable()
                     }
                     }*/
                    column.domain = ko.mapping.fromJS(column.domain)
                    if (column.multi) {
                        column.filter = ko.observableArray()
                    } else {
                        column.filter = ko.observable()
                    }
                });
            }
        });
    };

    self.openExportUrl = function() {
        var url = self.dt.ajax.url() + ".csv?" + $.param(self.dt.ajax.params())
        window.open(url, '_blank');

    }

    self.loadRows = function () {
        self.dt = $(id).DataTable({
            "dom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
            "ordering": true,

            "lengthMenu": [ 20, 50, 100 ],
            serverSide: true,
            "ajax": options.ajax,
            //ajax: self.source,
            /*"ajax": {
                "url": self.source,
                "data": function (d) {
                    d.myKey = "myValue";
                    // d.custom = $('#myInput').val();
                    // etc
                }
            },*/
            "columns": options.columns,
            "stateSave": true,
            "_stateLoadCallback": function () {
                //console.log(JSON.stringify(state()))

                //return null
            },
            "stateLoadParams": function (settings, data) {
                console.log("stateLoadParams")

                var query = new URI().search(true);
                //console.log("query: " + JSON.stringify(query))

                if(query.start)
                    data.start = query.start;
                if(query.length)
                    data.length = query.length;

                $.each(self.columns, function (index, column) {
                    //if (!column.canFilter) return;

                    var querySearch = query[column.name]

                    console.log("col: " + index + " name: " + column.name + " filter: " + querySearch);

                    /*                    if(!filterString) return;

                     var values = filterString.split(',').map(function(item) {
                     return parseInt(item, 10);
                     });*/
                    var values = querySearch;

                    //console.log("vals: " + values)

                    //var filterString = query[filter.name];
                    //column.filter(filterString)
                    //data.columns[filter.column].search.search = query[filter.name]

                    data.columns[index].search.search = values
                });


                return data;
            },

            "stateSaveParams": function (settings, data) {
                console.log("stateSaveParams")


                var u = new URI();
                u.setSearch({
                    start: data.start,
                    length: data.length
                });


                var params = {
                    start: data.start,
                    length: data.length
                };


                $.each(self.columns, function (index, column) {
                    var search = data.columns[index].search.search;

                    //console.log("col: " + index + " name: " + column.name + " filter: " + colSearch);
                    //console.log("type: " + typeof colSearch);

                    if (!search) {
                        u.removeSearch(column.name)
                    } else
                        u.setSearch(column.name, search)


                    if (!search) {
                        delete params[column.name];
                    } else
                        params[column.name]=search;


                    //u.setSearch(filter.name, filter.serialize()).toString()

//                    var colSearch = data.columns[filter.column].search.search


                    //console.log(JSON.stringify(search));

                });

                console.log("params: " + JSON.stringify(params));


                var u2 = URI().search("").setSearch(params);


                // could end badly
                //var query = u.toString().replace(/%2C/g, ',')
                var query = u2.toString().replace(/%2C/g, ',')
                history.replaceState(null, null, query)
            }
        });


        $(id + '_length').find("> label > select").removeClass("input-sm")

        $(id + '_export').click(self.openExportUrl)
    };


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


    //Date range picker with time picker

    /*   var drp = $('#clusters-daterange')

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
     onApply(startDate, endDate)*/


    //for (i = 0; i < self.filters.length; i++) {
    /*for (var f in self.filters) {
     console.log("Filter2: " + f)
     self.filters[f].extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
     }*/


    //self.filters.daterange.subscribe(self.applyFilters);
    //self.filters.classes.subscribe(self.applyFilters);


    self.subscribe = function () {
        console.log("subscribe to filter fields!")

        $.each(self.columns, function (index, column) {
            var tabE = $(id).DataTable()

            var colSearch = tabE.column(index).search()
            console.log("search: " + colSearch)

            if (!colSearch) return;

            var values = colSearch;

            var values = colSearch.split(',').map(function (item) {
                return parseInt(item, 10);
            });

            /*            var values = [];
             if(colSearch){
             console.log("parseout");
             values = JSON.parse("[" + colSearch.split(",") + "]")
             }*/

            console.log("values: " + values)

            //filter.apply(colSearch)

            //if (column.canFilter)
            column.filter(values);

            console.log("now: " + column.filter())
        });

        $.each(self.columns, function (index, column) {
            column.filter.subscribe(self.applyFilters);
        });
    };

    self.applyFilters = function () {
        var catalog = $(id).DataTable();
        //catalog.fnFilter(self.emailFilter(), 0);

        //console.log('filters called');
        $.each(self.columns, function (index, column) {
            //console.log(JSON.stringify(column));

            var search = column.filter()
            if (!search) search = "";

            //catalog.column(index).search(search).draw()
            catalog.column(index).search(search)
        });
        catalog.draw()
    };

}
