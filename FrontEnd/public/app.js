'use strict';

/**
 * @ngdoc overview
 * @name sampleApp
 * @description
 * # sampleApp
 *
 * Main module of the application.
 */
(function(){

  var app = angular.module('SOSECommunity', [
    'ngRoute',
    'mobile-angular-ui',

    // touch/drag feature: this is from 'mobile-angular-ui.gestures.js'.
    // This is intended to provide a flexible, integrated and and
    // easy to use alternative to other 3rd party libs like hammer.js, with the
    // final pourpose to integrate gestures into default ui interactions like
    // opening sidebars, turning switches on/off ..

    'mobile-angular-ui.gestures', 'angular-md5', 'nvd3', 'googlechart'

  ]);
  app.config(function($routeProvider) {
    $routeProvider.when('/', {templateUrl: 'views/login.html', reloadOnSearch: false});
    $routeProvider.when('/settings', {templateUrl: 'views/settings.html', reloadOnSearch: false});
    $routeProvider.when('/authorinfo', {templateUrl: 'views/authorinfo.html', reloadOnSearch: false});
    $routeProvider.when('/paperinfo', {templateUrl: 'views/paperinfo.html', reloadOnSearch: false});
    $routeProvider.when('/welcome', {templateUrl: 'views/welcome.html', reloadOnSearch: false});
    $routeProvider.when('/yearlypublication', {templateUrl: 'views/yearlypublication.html', reloadOnSearch: false});
    $routeProvider.when('/journal', {templateUrl: 'views/journal.html', reloadOnSearch: false});
    $routeProvider.when('/collaboration', {templateUrl: 'views/collaboration.html', reloadOnSearch: false});
    $routeProvider.when('/follow', {templateUrl: 'views/follow.html', reloadOnSearch: false});
    $routeProvider.when('/myfollowers', {templateUrl: 'views/myfollowers.html', reloadOnSearch: false});
    $routeProvider.when('/search', {templateUrl: 'views/search.html', reloadOnSearch: false});
    $routeProvider.when('/publication', {templateUrl: 'views/publication.html', reloadOnSearch: false});
    $routeProvider.when('/publish', {templateUrl: 'views/publish.html', reloadOnSearch: false});
    $routeProvider.when('/map', {templateUrl: 'views/map.html', reloadOnSearch: false});
    $routeProvider.when('/collaborationNetwork', {templateUrl: 'views/multiauthornetwork.html', reloadOnSearch: false});
    $routeProvider.when('/papertopaper', {templateUrl: 'views/papertopaper.html', reloadOnSearch: false});
    $routeProvider.when('/persontoperson', {templateUrl: 'views/persontoperson.html', reloadOnSearch: false});
    $routeProvider.when('/persontopaper', {templateUrl: 'views/persontopaper.html', reloadOnSearch: false});
    $routeProvider.when('/topKrelatedPaper', {templateUrl: 'views/topKrelatedPaper.html', reloadOnSearch: false});
    $routeProvider.when('/interestedpaper', {templateUrl: 'views/interestedpaper.html', reloadOnSearch: false});
    $routeProvider.when('/topcited', {templateUrl: 'views/topcited.html', reloadOnSearch: false});
    $routeProvider.when('/categorize', {templateUrl: 'views/categorize.html', reloadOnSearch: false});
    $routeProvider.when('/authorspath', {templateUrl: 'views/shortestpath.html', reloadOnSearch: false});    
  });


})();
