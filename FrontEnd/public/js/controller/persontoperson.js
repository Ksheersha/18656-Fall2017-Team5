/**
* Created by wangziming on 11/25/16.
*/
'use strict';

var app = angular.module('SOSECommunity');
app.controller('personToPersonNetCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService','recommendService',
function($scope, $http, md5, usernameService, $rootScope, $location, socketioService,recommendService) {

  var mySocket = socketioService.getSocket();
  var color = d3.scale.category20();

  $http({
    method : 'GET',
    url : 'http://localhost:9000/network/persontoperson'
  }).success(function(res, status, headers, config) {

      $scope.result = res;

      var nodes = res['authors'].map(function(name) {
        return {
          name: name,
          group: 0
        }
      });
      var links = res['paths'].map(function(data) {
        return {
          source: data['start_idx'],
          target: data['end_idx'],
          value: 1
        }
      })

      console.log(nodes)
      console.log(links)

      $scope.options = {
        chart: {
          type: 'forceDirectedGraph',
          height: 700,
          width: 900,
          radius: 10,
          gravity: 0.08,
          margin:{top: 50, right: 50, bottom: 50, left: 50},
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
}]);
