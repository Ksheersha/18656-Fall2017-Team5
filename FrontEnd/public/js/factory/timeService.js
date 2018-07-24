var app = angular.module('SOSECommunity');

app.service('timeService', function () {
    var time = '';
    return {
        getVisitedTime: function () {
            return time;
        },
        setVisitedTime: function(value) {
            time = value;
        }
    };
});