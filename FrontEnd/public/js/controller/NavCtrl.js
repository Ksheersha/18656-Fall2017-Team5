'use strict';
(function(){
  var app = angular.module('SOSECommunity');
  app.controller('navCtrl', function ($scope) {
    $scope.loggedin = false;
    $scope.$on('status:login',function(obj, data){
      $scope.loggedin = data.status;
    });
  });
})();
