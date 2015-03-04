$(function () {

    var tick = $('#tick').attr('data-tick')

    var path = "/ticks/" + tick + "/graph";

    var groups = {
        def : {

        },
        trader: {
            shape: 'box',
            mass: 1
        },
        symbol: {
            shape: 'ellipse',
            mass: 1.1
        },
        sector: {
            shape: 'ellipse',
            mass: 1.2
        }
        // add more groups here
    };

    // create a network
    var container = document.getElementById('factor-graph');

    $.get(path, function (data) {
        $(".result").html(data);

        var network = new vis.Network(container, data, {groups: groups});
    });

    /*                   var data = {
     nodes: nodes,
     edges: edges
     };*/


});