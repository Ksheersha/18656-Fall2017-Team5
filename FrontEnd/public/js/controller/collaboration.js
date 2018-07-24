'use strict';

var app = angular.module('SOSECommunity');
app.controller('CollaborationCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService','recommendService',
  function($scope, $http, md5, usernameService, $rootScope, $location, socketioService, recommendService) {

        var mySocket = socketioService.getSocket();
        var color = d3.scale.category20();
        $scope.generate = function(){
               $http({
                  method : 'GET',
                  url : 'http://localhost:9000/getAuthorDirNetwork/' + $scope.authorname
              }).success(function(res, status, headers, config) {

                  $scope.recommend($scope.authorname);

                  $scope.result = res;
                  console.log(res[0]);

                  var nodes = new Array();
                  var links = new Array();

                  var central = {"name":res[0]["name"],"group":0};
                  nodes[0] = central;
                  for (var i = 1; i < res.length; i++){
                      var node = {"name":res[i]["name"],"group":1};
                      nodes[i] = node;
                  }
                  for (var j = 1; j < res.length; j++){
                      var link = {"source":j,"target":0,"value":1};
                      links[j - 1] = link;
                  }
                       $scope.options = {
                           chart: {
                               type: 'forceDirectedGraph',
                               height: 700,
                               width: 900,
                               radius: 10,
                               gravity: 0.02,
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
        }


        $scope.recommend = function(name) {
            $http({
                method : 'GET',
                url : 'http://localhost:9000/getRecommendation/' + name + "/author"
            }).success(function(res, status, headers, config) {
                var prevName = recommendService.getAuthor();
                if (prevName != '' && prevName != name) {
                    $scope.connect(prevName, name);
                }
                recommendService.setAuthor(name);
                if (res.length > 0) {
                    $scope.visit = "People also search " + res[0].name;
                } else {
                    $scope.visit = '';
                }
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        };

        $scope.connect = function(prevName, currName){
            $http({
                method : 'POST',
                url : 'http://localhost:9000/addRelatedRelationship/' + prevName + ',' + currName + '/author'
            }).success(function(res, status, headers, config) {
                //console.log(res);
            }).error(function (res, status, headers, config) {
                console.log(res);
            });
        };
  }
]);
