/**
* Created by wangziming on 11/25/16.
*/
'use strict';

var app = angular.module('SOSECommunity');
app.controller('paperToPaperNetCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService','recommendService',
function($scope, $http, md5, usernameService, $rootScope, $location, socketioService,recommendService) {

  var mySocket = socketioService.getSocket();
  var color = d3.scale.category20();

  $http({
    method : 'GET',
    url : 'http://localhost:9000/network/papertopaper'
  }).success(function(res, status, headers, config) {

    $scope.result = res;

    console.log(res.graphs[0]);

    var idMap = {}
    var nodes = res.graphs[0].nodes.map(function(d,idx) {
      idMap[d.id] = idx
      if (d.type === 'Paper') {
        var name = d.metadata.title
        var group = 0
      }
      return {
        name: name,
        group: group
      }
    })

    var links = res.graphs[0].edges.map(function(d,idx) {

      return {
        source: idMap[d.source_id],
        target: idMap[d.target_id],
        value: 1
      }
    })

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
