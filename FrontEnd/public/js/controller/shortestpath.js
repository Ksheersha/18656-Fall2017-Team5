/**
* Created by wangziming on 11/25/16.
*/
'use strict';

var app = angular.module('SOSECommunity');
app.controller('authorsPathCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService','recommendService',
function($scope, $http, md5, usernameService, $rootScope, $location, socketioService,recommendService) {

  var mySocket = socketioService.getSocket();
  var color = d3.scale.category20();


  $scope.generate = function() {
    var firstAuthor = $scope.firstAuthor
    var secondAuthor = $scope.secondAuthor
    $http({
      method : 'GET',
      url : 'http://localhost:9000/network/authorShortestPath?firstAuthor='+firstAuthor+'&secondAuthor='+secondAuthor
    }).success(function(res, status, headers, config) {

        $scope.result = res;

        var idMap = {}
        var nodes = res.path.map(function(name,idx) {
          idMap[name] = idx
          return {
            name: name,
            group: 0
          }
        })

        var links = []
        for (var i=0; i<res.path.length - 1; i++) {
            links.push({
              source: i,
              target: i+1,
              value: 1
            })
        }

        console.log(res.path)
        $scope.options = {
          chart: {
            type: 'forceDirectedGraph',
            height: 700,
            width: 1200,
            radius: 10,
            gravity: 0.08,
            margin:{top: 0, right: 50, bottom: 50, left: 50},
            color: function(d){
              return color(d.group)
            },
            nodeExtras: function(node) {
              node && node
              .append("text")
              .text(function(d) { return d.name })
              .style('font-size', '10px');
            }
          }
        };

        $scope.data = {
          "nodes":nodes,
          "links":links
        }
      }
    ).error(function (res, status, headers, config) {
      console.log(res);
    });
  }
}]);
