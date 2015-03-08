

var graphWidget = function(id, source) {

    //var tick = $('#tick').attr('data-tick')

    //var path = "/data/graph/" + tick ;
    //var path = $('#data-feeds').attr('data-graph')




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
    var container = document.getElementById(id);

    $.get(source, function (data) {
        $(".result").html(data);

        var network = new vis.Network(container, data, {groups: groups});
    });

    /*                   var data = {
     nodes: nodes,
     edges: edges
     };*/


}