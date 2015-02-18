/**
 * Created by martin on 03/09/14.
 */




$(function () {
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
        "lengthMenu": [ 25, 50, 100, 200 ],
        serverSide: true,
        ajax: '/trades/query',
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


var viewModel;

function ViewModel() {
    var self = this;

    // enum representations
    self.accountType = ko.observableArray()
    self.accountStatus = ko.observableArray()
    self.accountLevel = ko.observableArray()

    self.filters = {
        "email": ko.observable(""),
        "type": ko.observable(""),
        "status": ko.observableArray(),
        "level": ko.observableArray(),
        "firstName": ko.observable(""),
        "lastName": ko.observable("")
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
    self.applyFilters = function () {
        var catalog = $('#accounts-table').dataTable();
        //catalog.fnFilter(self.emailFilter(), 0);

        i = 0
        for (var f in self.filters) {
            console.log("Filter: " + f)
            catalog.fnFilter(self.filters[f](), i);
            i++
        }

        console.log('filters called');
    };
    //self.emailFilter.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);

    //for (i = 0; i < self.filters.length; i++) {
    for (var f in self.filters) {
        console.log("Filter: " + f)
        self.filters[f].extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
    }

    /*self.filters.email.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
     self.filters.type.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
     self.filters.status.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
     self.filters.level.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);*/
    //self.regionFilter.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);
    //self.nameFilter.extend({ rateLimit: 1000 }).subscribe(self.applyFilters);

    self.loadRows = function () {
        $('#accounts-table').dataTable({
            "sDom": "<'row'<'col-xs-6'l><'col-xs-6'>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>",
            "bPaginate": true,
            "bProcessing": true,
            "bServerSide": true,
            "sAjaxSource": "/accounts/query",
            "aoColumns": [
                {"mDataProp": "id"},
                {"mDataProp": "email"},
                {"mDataProp": "type"},
                {"mDataProp": "status"},
                {"mDataProp": "level"},
                {"mDataProp": "firstName"},
                {"mDataProp": "middleName"},
                {"mDataProp": "lastName"},
                {"mDataProp": "dateOfBirth"},
                {"mDataProp": "city"},
                {"mDataProp": "gender"}
            ],
            "aoColumnDefs": [
                {
                    "aTargets": [1],
                    "mData": "id",
                    "mRender": function (data, type, full) {
                        return "<a href=\"/accounts/" + full.id + "\">" + data + "</a>"
                    }
                },
                {
                    "aTargets": [2],
                    "mData": "id",
                    "mRender": function (data, type, full) {
                        return data
                    }
                },
                {
                    "aTargets": [3],
                    "mData": "id",
                    "mRender": function (data, type, full) {
                        return data
                    }
                },
                {
                    "aTargets": [4],
                    "mData": "id",
                    "mRender": function (data, type, full) {
                        return data
                    }
                }
            ]
        });
    };

    self.disableVenue = function (venueId) {

    }
}


$(document).ready(function () {

    console.log("b");

    viewModel = new ViewModel();
    viewModel.loadRows();
    populateFilters(viewModel);
    ko.applyBindings(viewModel);
});