'use strict';

var app = angular.module('SOSECommunity');

app.controller('LoginCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService','timeService',
  function($scope, $http, md5, usernameService, $rootScope, $location, socketioService,timeService) {
  $scope.login={};
  $scope.login.username="";
  $scope.login.password="";

  var mySocket = socketioService.getSocket();
  // check if the username already exists
  $scope.checkUserName = function(){
    if($scope.loginpanel.username.$invalid){
      alert("Please provide another username.")
    } else if($scope.loginpanel.password.$invalid){
      alert("Please provide another password.")
    } else{
      $http({
          method : 'GET',
          url : 'http://localhost:9000/authenticate/' + $scope.login.username + '/' + md5.createHash($scope.login.password)
      }).success(function(res, status, headers, config) {
          console.log(res);
          if (res == 'No') {
            alert("Not a Researcher Name in Database")
          } else if (res == 'Fail') {
            alert("Wrong password")
          } else if (res == 'New') {
            if (confirm("Please confirm creating a new user") == true) {
              $http({
                  method : 'POST',
                  url : 'http://localhost:9000/register/' + $scope.login.username + '/' + md5.createHash($scope.login.password)
              }).success(function(data, status, headers, config) {
                  timeService.setVisitedTime(res);
                  $scope.onlogin();
              }).error(function(data, status, headers, config) {
                  console.log(status);
              });
            }
          } else {
            $http({
                method : 'POST',
                url : 'http://localhost:9000/login/' + $scope.login.username
            }).success(function(data, status, headers, config) {
                timeService.setVisitedTime(res);
                $scope.onlogin();
            }).error(function(data, status, headers, config) {
                console.log(status);
            });
          }
      }
      ).error(function (res, status, headers, config) {
          console.log('fail');
      });
    }
  }

  // actions after login
  $scope.onlogin = function(){
    usernameService.setUsername($scope.login.username);
    $rootScope.$broadcast('refreshDirectory', {});
    $rootScope.$broadcast('status:login', {status: true}); 
    $location.path('welcome');
  }
}
]);

app.directive('usernameDirective', function (){ 
  return {
    require: 'ngModel',
    link: function(scope, elem, attr, ngModel) {
      var blacklist = attr.blacklist.split(' ');
      ngModel.$parsers.unshift(function(value) {
       var valid = blacklist.indexOf(value) === -1 &&(!value  || value.length >2);
       ngModel.$setValidity('ddd', valid);
       return valid ? value : undefined;
     });
    }
  };
});

// the rule of the password
app.directive('passwordDirective', function (){ 
  return {
    require: 'ngModel',
      link: function(scope, elem, attr, ngModel) {
        ngModel.$parsers.unshift(function(value) {
         var valid = !value  || value.length >3;
         ngModel.$setValidity('length', valid);
         return valid ? value : undefined;
       });
      }
  };
});