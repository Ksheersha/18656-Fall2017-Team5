var app = angular.module('SOSECommunity');

// share the username among the controllers
app.service('recommendService', function () {
    var author = '';
    var paper = '';
    var keyword = '';

    return {
        getAuthor: function () {
            return author;
        },
        setAuthor: function(value) {
            author = value;
        },
        getPaper: function () {
            return paper;
        },
        setPaper: function(value) {
            paper = value;
        },
        getKeyword: function () {
            return keyword;
        },
        setKeyword: function(value) {
            keyword = value;
        }
    };
});