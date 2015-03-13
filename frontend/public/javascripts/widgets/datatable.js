/**
 * Created by martin on 03/09/14.
 */



function DTModel(id, options) {
    var self = this;

    self.start = ko.observable(0);
    self.length = ko.observable(20);

    self.options = options;
    self.columns = [];

    self.getMetaData = function () {
        $.ajax({
            "url": options.ajax.meta,
            "async": false,
            "dataType": "json",
            "success": self.loadMetaData
        });
    };

    self.loadMetaData = function (data) {
        var query = new URI().search(true);

        self.source = data.source;
        self.columns = []


        $.each(data.columns, function (index, columnDef) {
            self.columns[index] = {
                title: columnDef.title,
                name: columnDef.name
            };

            var col = self.columns[index]

            if (columnDef.filter) {
                var filterValue = columnDef.filter.value;

                if (query[col.name]) {
                    filterValue = query[col.name];
                }

                col.multi = columnDef.filter.multi;
                col.domain = ko.mapping.fromJS(columnDef.filter.domain)

                if (col.multi) {
                    var values = [];
                    if (filterValue)
                        values = filterValue.split(",")

                    col.filter = ko.observableArray(values)
                } else {
                    col.filter = ko.observable(filterValue)
                }
            } else {
                col.filter = ko.observable()
            }
        });


    };


    self.openExportUrl = function () {
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
            /*            "stateLoadCallback": function () {
             return null;
             },*/
            "stateLoadParams": function (settings, data) {
                console.log("stateLoadParams")

                var query = new URI().search(true);
                //console.log("query: " + JSON.stringify(query))

                if (query.start)
                    data.start = query.start;
                if (query.length)
                    data.length = query.length;

                //data.order = []

                $.each(self.columns, function (index, column) {
                    //data.columns[index].search.search = column.filter();

                    console.log("col: " + index + " name: " + column.name + " filter: " + column.filter());
                });


                return data;
            },

            "stateSaveCallback": function (settings, data) {
                console.log("stateSaveCallback")

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
                    //var search = data.columns[index].search.search;

                    var search = self.columns[index].filter();

                    if (self.columns[index].multi) {
                        console.log("index: " + index);
                        search = search.join(",");
                    }

                    //console.log("col: " + index + " name: " + column.name + " filter: " + colSearch);
                    //console.log("type: " + typeof colSearch);

                    /*                    if (!search) {
                     u.removeSearch(column.name)
                     } else
                     u.setSearch(column.name, search)*/


                    if (!search) {
                        delete params[column.name];
                    } else
                        params[column.name] = search;


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

        self.applyFilters();
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



    self.subscribe = function () {
        $.each(self.columns, function (index, column) {
            column.filter.subscribe(self.applyFilters);
        });
    };

    self.applyFilters = function () {
        console.log("applying filters");

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
