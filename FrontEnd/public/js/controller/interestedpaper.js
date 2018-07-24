/**
 * Created by wangziming on 11/26/16.
 */

'use strict';

var app = angular.module('SOSECommunity');
app.controller('interestedpaperCtrl', ['$scope','$http', 'md5', 'usernameService', '$rootScope', '$location', 'socketioService',
    function($scope, $http, md5, usernameService, $rootScope, $location, socketioService) {

        var mySocket = socketioService.getSocket();
        var color = d3.scale.category20();
        var username = usernameService.getUsername();
        //console.log(username);
        $scope.generate = function(){
            $http({
                method : 'GET',
                url : 'http://localhost:9000/InterestedPapers/' + $scope.keywords + "/" + username
            }).success(function(res, status, headers, config) {
                    //console.log(res);
                    var nodes = new Array();
                    var nodeIndex = new Array();
                    // containedwords
                    // linksRe1
                    // linksRe2
                    // part1ReaderPaperNodes
                    // part1NewPaperNodes


                    var i = 0;
                    for (var j = 0; j < res["containedwords"].length; j++){
                        nodes[i] = {"name":res["containedwords"][j],"group":0};
                        nodeIndex[i] = res["containedwords"][j];
                        i++;
                    }

                    for (var k = 0; k < res["part1ReaderPaperNodes"].length; k++){
                        nodes[i] = {"name":res["part1ReaderPaperNodes"][k],"group":1};
                        nodeIndex[i] = res["part1ReaderPaperNodes"][k];
                        i++;
                    }

                    for (var k = 0; k < res["part1NewPaperNodes"].length; k++){
                        nodes[i] = {"name":res["part1NewPaperNodes"][k],"group":2};
                        nodeIndex[i] = res["part1NewPaperNodes"][k];
                        i++;
                    }

                    var links = new Array();
                    var count = 0;
                    //console.log(res["paperAuthorLinks"]+"\t"+"ssssnpm ");
                    for (var m = 0; m < res["linksRe1"].length; m++){
                        var index1 = nodeIndex.indexOf(res["linksRe1"][m][0]);
                        for (var l = 1; l < res["linksRe1"][m].length; l++){
                            var index2 = nodeIndex.indexOf(res["linksRe1"][m][l]);
                            console.log(index1 + "\t" + index2);
                            var link = {"source":index1,"target":index2,"value":1};
                            links[count] = link;
                            count++;
                        }
                    }


                    for (var m = 0; m < res["linksRe2"].length; m++){
                        var index1 = nodeIndex.indexOf(res["linksRe2"][m][0]);
                        for (var l = 1; l < res["linksRe2"][m].length; l++){
                            var index2 = nodeIndex.indexOf(res["linksRe2"][m][l]);
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

    }
]);
