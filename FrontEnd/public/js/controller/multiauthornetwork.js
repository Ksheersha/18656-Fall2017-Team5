/**
 * Created by wangziming on 11/25/16.
 */
'use strict';

var app = angular.module('SOSECommunity');
app.controller('multiauthornetCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService','recommendService',
    function($scope, $http, md5, usernameService, $rootScope, $location, socketioService,recommendService) {

        var mySocket = socketioService.getSocket();
        var color = d3.scale.category20();
        $scope.generate = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/collaborationNetwork/' + $scope.authorname
            }).success(function(res, status, headers, config) {

                    $scope.recommend($scope.authorname);
                    
                    $scope.result = res;

                    var nodeIndex = new Array();
                    var nodes = new Array();

                    var central = {"name":res["res2"][0][0],"group":0};
                    nodes[0] = central;
                    nodeIndex[0] = res["res2"][0][0];
                    var i = 1;
                    for (var j = 0; j < res["res2"][1].length; j++){
                        nodes[i] = {"name":res["res2"][1][j],"group":1};
                        nodeIndex[i] = res["res2"][1][j];
                        i++;
                    }
                    for (var k = 0; k < res["res2"][2].length; k++){
                        nodes[i] = {"name":res["res2"][2][k],"group":2};
                        nodeIndex[i] = res["res2"][2][k];
                        i++;
                    }
                    var links = new Array();
                    var count = 0;
                    for (var m = 0; m < res["res1"].length; m++){
                        for (var l = 0; l < res["res1"][m]["adjacentList"].length; l++){
                            var index1 = nodeIndex.indexOf(res["res1"][m]["name"]);
                            var index2 = nodeIndex.indexOf(res["res1"][m]["adjacentList"][l]);
                            if (index1 == 4 || index2 == 4){console.log(index1+"\t"+index2)};
                            var link = {"source":index1,"target":index2,"value":1};
                            links[count] = link;
                            count++;
                        }
                    }


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
