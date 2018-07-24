/**
 * Created by wangziming on 11/26/16.
 */
/**
 * Created by wangziming on 11/25/16.
 */
'use strict';

var app = angular.module('SOSECommunity');
app.controller('topKRelatedCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService',
    function($scope, $http, md5, usernameService, $rootScope, $location, socketioService) {

        var mySocket = socketioService.getSocket();
        var color = d3.scale.category20();
        $scope.generate = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/topKrelatedPaper/' + $scope.keywords
            }).success(function(res, status, headers, config) {
                    console.log(res)

                    var nodes = new Array();
                    var nodeIndex = new Array();

                    var i = 0;
                    for (var j = 0; j < res["paperNode"].length; j++){
                        nodes[i] = {"name":res["paperNode"][j],"group":4};
                        nodeIndex[i] = res["paperNode"][j];
                        i++;
                    }
                    for (var k = 0; k < res["authorNode"].length; k++){
                        nodes[i] = {"name":res["authorNode"][k],"group":5};
                        nodeIndex[i] = res["authorNode"][k];
                        i++;
                    }

                    for (var el = 0; el < nodes.length; el++){
                        console.log(nodes[el]);
                    }

                    var links = new Array();
                    var count = 0;
                    console.log(res["paperAuthorLinks"]+"\t"+"ssssnpm ");
                    for (var m = 0; m < res["paperAuthorLinks"].length; m++){
                        var index1 = nodeIndex.indexOf(res["paperAuthorLinks"][m][0]);
                        for (var l = 1; l < res["paperAuthorLinks"][m].length; l++){
                            var index2 = nodeIndex.indexOf(res["paperAuthorLinks"][m][l]);
                            console.log(index1 + "\t" + index2);
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
                            margin:{top: 50, right: 50, bottom: 50, left: 50},
                            radius: 10,
                            gravity: 0.08,
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

    }
]);
