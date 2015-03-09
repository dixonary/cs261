/**
 * Created by martin on 15/02/15.
 */

var timeFormat = 'DD/MM/YYYY HH:mm:ss'

var Item = function (a, b, g) {
    this.value = a;
    this.label = b;
    this.group = typeof g !== 'undefined' ? g : false;
}


function fillString(template, values) {
    //return template.replace(/%\w+%/g, function(all) {
    return template.replace(/_\w+/g, function (all) {
        return values[all] || all;
    });
}

var startDate = moment().startOf('year')
var endDate = moment().endOf('year')

function defaultDrpOptions() {
    return {
        //startDate: startDate,
        //endDate: endDate,
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
    }
}

// derrived from https://stackoverflow.com/questions/6612705/jquery-ui-datepicker-change-event-not-caught-by-knockoutjs
ko.bindingHandlers.daterangepicker = {
    init: function(element, valueAccessor, allBindingsAccessor) {
        var $el = $(element);
        var options = defaultDrpOptions();
        //var options = allBindingsAccessor().datepickerOptions || defaultDrpOptions()

        var obs = valueAccessor();
        var range = obs();

        options.startDate = moment(range[0]);
        options.endDate = moment(range[1]);

        console.log("default: " + JSON.stringify(options))

        //initialize datepicker with some optional options
        $el.daterangepicker(
            options,
            function (start, end, label) {
                var daterange = start + ',' + end

                console.log("daterange: " + daterange);

                $el.find('span').html(start.format(timeFormat) + ' - ' + end.format(timeFormat));

                //self.filters.daterange(daterange)

                //var observable = valueAccessor();
                //observable(12345);
            }
        );


        //handle the field changing
        console.log("handle field changing")
        ko.utils.registerEventHandler(element, "apply.daterangepicker", function() {
            var $el = $(element)
            var observable = valueAccessor();

            var data = $el.data('daterangepicker')
            var val = data.startDate+","+data.endDate

            console.log("val: "+ val + " obs: " + JSON.stringify(observable));

            //valueAccessor("mayo");
            observable(val);
        });

        //handle disposal (if KO removes by the template binding)
/*        ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
            $el.daterangepicker("destroy");
        });*/

    },
    update: function(element, valueAccessor) {
        var $el = $(element)
        var value = ko.utils.unwrapObservable(valueAccessor())

        var drp_data = $el.data('daterangepicker');

        var start = parseInt(value[0]);
        var end=  parseInt(value[1]);

        drp_data.setStartDate(start)
        drp_data.setEndDate(end)

        $el.find('span').html(drp_data.startDate.format(timeFormat) + ' - ' + drp_data.endDate.format(timeFormat));
    }
};




/*
 source: https://stackoverflow.com/questions/21998531/knockout-js-with-bootstrap-selectpicker
 */
ko.bindingHandlers.selectPicker = {
    after: ['options'], /* KO 3.0 feature to ensure binding execution order */
    init: function (element, valueAccessor, allBindingsAccessor) {
        var $element = $(element);
        $element.addClass('selectpicker').selectpicker();

        var doRefresh = function () {
            $element.selectpicker('refresh');
        }, subscriptions = [];

        // KO 3 requires subscriptions instead of relying on this binding's update
        // function firing when any other binding on the element is updated.

        // Add them to a subscription array so we can remove them when KO
        // tears down the element.  Otherwise you will have a resource leak.
        var addSubscription = function (bindingKey) {
            var targetObs = allBindingsAccessor.get(bindingKey);

            if (targetObs && ko.isObservable(targetObs)) {
                subscriptions.push(targetObs.subscribe(doRefresh));
            }
        };

        addSubscription('options');
        addSubscription('value');           // Single
        addSubscription('selectedOptions'); // Multiple

        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            while (subscriptions.length) {
                subscriptions.pop().dispose();
            }
        });
    },
    update: function (element, valueAccessor, allBindingsAccessor) {
    }
};