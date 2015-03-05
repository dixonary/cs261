/**
 * Created by martin on 15/02/15.
 */

var timeFormat = 'DD/MM/YYYY HH:mm:ss'



var Item = function ( a, b, g ) {
    this.value = a ;
    this.label = b ;
    this.group = typeof g !== 'undefined' ? g : false;
}