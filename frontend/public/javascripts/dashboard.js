var rd;

var ClusterVM = function () {
    var self = this;

    self.id = ko.observable()
    self.status = ko.observable()

    self.discovered = ko.computed(function () {
        return moment(self.time).format(timeFormat)
    }, this);

    self.unseen = ko.computed(function () {
        return this.status() == "UNSEEN";
    }, self);

    self.link = ko.computed(function () {
        var url = jsRoutes.controllers.Clusters.element(self.id()).url

        return genIconButton(url, '', 'fa-cubes')
    }, self);

    return self;
}

var TickVM = function (tick, start, end, clusterCount) {
    var self = this;

    self.tick = ko.observable(tick);
    self.start = ko.observable(start);
    self.end = ko.observable(end);
    self.clusterCount = ko.observable(clusterCount);

    self.date = ko.computed(function () {
        return moment(self.start()).format(format.date)

    }, this);

    self.time = ko.computed(function () {
        //return moment(self.start()).format(format.time)
        var s = moment(self.start()).format(format.time) + "&nbsp;-&nbsp;" +
            moment(self.end()).format(format.time);

        return s

    }, this);

    self.interval = ko.computed(function () {
        //return moment(self.start()).format(format.time)
        var s = moment(self.start()).format(format.date) + " " +
            moment(self.start()).format(format.time) + "&nbsp;-&nbsp;" +
            moment(self.end()).format(format.time);

        return s

    }, this);

    self.link = ko.computed(function () {
        //return jsRoutes.controllers.Clusters.element(self.tick()).url
        var time = self.start() + "," + self.end()
        //return jsRoutes.controllers.Application.clustersBy(time).url

        var url = jsRoutes.controllers.Application.clustersBy(time).url;

        return genIconButton(url, '', 'fa-cubes')
    }, self);

    return self;
}




var Activity = function (options) {
    var self = this;



    self.toSeries = function (data) {
        return [
                {label: "Trades/second", color: "#3c8dbc", data: data[0]},
                {label: "Comms/second", color: "#FDEE00", data: data[1]}
            ];
    };

    var $el = $(options.id);
    self.activity = $.plot($el,
        [],
        {
            series: {
                shadowSize: 4,
                lines: {
                    show: true
                }
                /*                points: {
                 radius: 3,
                 fill: true,
                 show: true
                 }*/
            },
            xaxis: {
                mode: "time", timeformat: "%H:%M",
                tickSize: [5, "minute"]
            },
            yaxis: {

            },
            /*yaxes: [
             { position: "left"  },
             { position: "right" }
             ],*/
            grid: {
                borderColor: "#f3f3f3",
                borderWidth: 1,
                tickColor: "#f3f3f3"
            },
            legend: {
                show: true,
                position: "sw"
                //container: "#activity-legend"
            }
        });

    var updateInterval = 1000; //Fetch data ever x milliseconds
    var realtime = "on"; //If == to on then fetch data every x seconds. else stop fetching

    self.updateActivity = function () {

        $.ajax(
            //'/dashboard/activity.json',
            options.source,
            {
                success: function (data) {

                    self.activity.setData(data);
                    self.activity.setupGrid();


                    // Since the axes don't change, we don't need to call plot.setupGrid()
                    self.activity.draw();
                    if (realtime === "on")
                        setTimeout(self.updateActivity, updateInterval);
                }
            }
        )


    }

    //INITIALIZE REALTIME DATA FETCHING
    if (realtime === "on") {
        self.updateActivity();
    }
    //REALTIME TOGGLE
    $("#realtime .btn").click(function () {
        if ($(self).data("toggle") === "on") {
            realtime = "on";
        }
        else {
            realtime = "off";
        }
        self.updateActivity();
    });
    /*
     * END INTERACTIVE CHART
     */

}


var clusterLFVM;
var tickLFVM;

$(function () {


    clusterLFVM = new LiveFeedVM({
        source: '/dashboard/clusters.json',
        itemVM: ClusterVM
    });
    clusterLFVM.update();
    ko.applyBindings(clusterLFVM, $('#clusters')[0]);


    tickLFVM = new LiveFeedVM({
        source: '/dashboard/ticks.json',
        itemVM: TickVM
    });
    tickLFVM.update();
    ko.applyBindings(tickLFVM, $('#ticks')[0]);


    var cr = new Activity({
        id: '#cluster-rate',
        source: '/dashboard/clusters/rate.json'
    });

    var ac = new Activity({
        id: '#activity',
        source: '/dashboard/activity.json'
    });


    if (true) return;


    /*
     * Flot Interactive Chart
     * -----------------------
     */
    // We use an inline data source in the example, usually data would
    // be fetched from a server
    var data = [], totalPoints = 100;

    rd = getRandomData();

    function getRandomData() {

        if (data.length > 0)
            data = data.slice(1);

        // Do a random walk
        while (data.length < totalPoints) {

            var prev = data.length > 0 ? data[data.length - 1] : 50,
                y = prev + Math.random() * 10 - 5;

            if (y < 0) {
                y = 0;
            } else if (y > 100) {
                y = 100;
            }

            data.push(y);
        }

// Zip the generated y values with the x values
        var res = [];
        var res2 = [];
        for (var i = 0; i < data.length; ++i) {
            res.push([i, data[i]]);
            res2.push([i, data[i] + Math.random() * 5]);
        }

        return [res];
    }

    var interactive_plot = $.plot("#interactive", getRandomData(), {
        grid: {
            borderColor: "#f3f3f3",
            borderWidth: 1,
            tickColor: "#f3f3f3"
        },
        series: {
            shadowSize: 0, // Drawing is faster without shadows
            color: "#3c8dbc"
        },
        lines: {
            fill: true, //Converts the line chart to area chart
            color: "#3c8dbc"
        },
        yaxis: {
            min: 0,
            max: 100,
            show: true
        },
        xaxis: {
            show: true
        }
    });

    var updateInterval = 500; //Fetch data ever x milliseconds
    var realtime = "on"; //If == to on then fetch data every x seconds. else stop fetching
    function update() {

        //interactive_plot.setData([getRandomData()]);
        interactive_plot.setData(getRandomData());

        // Since the axes don't change, we don't need to call plot.setupGrid()
        interactive_plot.draw();
        if (realtime === "on")
            setTimeout(update, updateInterval);
    }

    //INITIALIZE REALTIME DATA FETCHING
    if (realtime === "on") {
        update();
    }
    //REALTIME TOGGLE
    $("#realtime .btn").click(function () {
        if ($(this).data("toggle") === "on") {
            realtime = "on";
        }
        else {
            realtime = "off";
        }
        update();
    });
    /*
     * END INTERACTIVE CHART
     */


    /*
     * BAR CHART
     * ---------
     */


    $.ajax({
        url: '/dashboard/commons.json',
        success: function (data) {

            console.log("DATS: " + JSON.stringify(data))

            $.plot("#bar-chart", [data], {
                grid: {
                    borderWidth: 1,
                    borderColor: "#f3f3f3",
                    tickColor: "#f3f3f3"
                },
                series: {
                    bars: {
                        show: true,
                        barWidth: 0.8,
                        align: "center"
                    }
                },
                xaxis: {
                    mode: "categories",
                    tickLength: 0
                }
            });
        }
    });


    var bar_data = {
        data: [
            ["January", 10],
            ["February", 8],
            ["March", 4],
            ["April", 13],
            ["May", 17],
            ["June", 9]
        ],
        color: "#3c8dbc"
    };


    /* END BAR CHART */

});





var LiveFeedVM = function (options) {
    var self = this;

    self.since = -1;
    self.maxItems = 8;
    self.items = ko.observableArray();


    var updateInterval = 500; //Fetch data ever x milliseconds
    var realtime = "on"; //If == to on then fetch data every x seconds. else stop fetching

    self.update = function () {
        //interactive_plot.setData([getRandomData()]);

        $.ajax(
            //'/dashboard/clusters',
            options.source,
            {
                data: {
                    since: self.since,
                    maxItems: self.maxItems
                },
                success: function (data) {
                    self.since = data.latest;


                    //var old = self.clusters();

                    $.each(data.items.reverse(), function (index, row) {

                        var itemCount = data.items.length;

                        var delay = 10 * (self.maxItems / itemCount)

                        $('#clusters').delay(delay).queue(function (next) {
                            //console.log("row: " + JSON.stringify(row))


                            var cl = ko.mapping.fromJS(row, {}, new options.itemVM());

                            //console.log(JSON.stringify(ko.mapping.toJS(cl)))

                            self.items.unshift(cl);

                            if (self.items().length > self.maxItems) {
                                self.items.pop();
                            }


                            next();
                        })


                    })


                    setTimeout(self.update, updateInterval);
                }
            }
        )
    }
    self.newRow = function (element, index, data) {
        $el = $(element);

        $el.find("td").hide().slideDown();
        $el.hide().fadeIn();
    };


}




/*
 * Custom Label formatter
 * ----------------------
 */
function labelFormatter(label, series) {
    return "<div style='font-size:13px; text-align:center; padding:2px; color: #fff; font-weight: 600;'>"
        + label
        + "<br/>"
        + Math.round(series.percent) + "%</div>";
}
