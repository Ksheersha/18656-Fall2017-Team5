'use strict';

/**
 * @ngdoc function
 * @name sampleApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sampleApp
 */
(function(){
  var app = angular.module('SOSECommunity');
  app.controller('LogoutController', ['$http', 'usernameService','socketioService', '$location','$rootScope',
     function ($http, usernameService, socketioService, $location, $rootScope) {
    var mySocket = socketioService.getSocket();
    this.click = function(){
      usernameService.setUsername('');
      $rootScope.$broadcast('status:login', {status: false}); 
      $location.path('');
    };
  }]);
})();
