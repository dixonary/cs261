$(function () {
    var self = this;

    var dists = '.factor-dist';


    $(".factor-dist").each(function (index, element) {
        var $el = $(this)

        var fc = $el.attr("data-factor-class");
        console.log(fc)

        $.ajax({
            url: '/dashboard/factors/freqs.json',
            data: {factor_class: fc},
            success: function (data) {

                $el.addClass("monkey")

                console.log("E: " + $el.attr("data-factor-class"))
                console.log("DATS: " + JSON.stringify(data))

                //$.plot($el, [data], {
                $el.plot(
                    [data],
                    {
                        title: "a",
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
                        },
                        legend: {
                            show: true,
                            position: "ne"
                        }
                    });
            }
        });




    })


    /*
     * BAR CHART
     * ---------
     */


/*    $.ajax({
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
    });*/


})