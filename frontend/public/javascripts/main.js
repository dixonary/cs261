/**
 * Created by martin on 15/02/15.
 */

var timeFormat = 'DD/MM/YYYY HH:mm:ss'

var Item = function ( a, b, g ) {
    this.value = a ;
    this.label = b ;
    this.group = typeof g !== 'undefined' ? g : false;
}


function fillString(template, values) {
    //return template.replace(/%\w+%/g, function(all) {
    return template.replace(/_\w+/g, function(all) {
        return values[all] || all;
    });
}

/*
source: https://stackoverflow.com/questions/21998531/knockout-js-with-bootstrap-selectpicker
 */
ko.bindingHandlers.selectPicker = {
    after: ['options'],   /* KO 3.0 feature to ensure binding execution order */
    init: function (element, valueAccessor, allBindingsAccessor) {
        var $element = $(element);
        $element.addClass('selectpicker').selectpicker();

        var doRefresh = function() {
            $element.selectpicker('refresh');
        },  subscriptions = [];

        // KO 3 requires subscriptions instead of relying on this binding's update
        // function firing when any other binding on the element is updated.

        // Add them to a subscription array so we can remove them when KO
        // tears down the element.  Otherwise you will have a resource leak.
        var addSubscription = function(bindingKey) {
            var targetObs = allBindingsAccessor.get(bindingKey);

            if ( targetObs && ko.isObservable(targetObs )) {
                subscriptions.push( targetObs.subscribe(doRefresh) );
            }
        };

        addSubscription('options');
        addSubscription('value');           // Single
        addSubscription('selectedOptions'); // Multiple

        ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
            while( subscriptions.length ) {
                subscriptions.pop().dispose();
            }
        } );
    },
    update: function (element, valueAccessor, allBindingsAccessor) {
    }
};