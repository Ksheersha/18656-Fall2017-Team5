    /**
     * Created by sheersha on 11/1/17.
     */
    'use strict';

    var app = angular.module('SOSECommunity');

    app.controller('MyFollowCtrl', ['$scope','$http', '$rootScope', '$location', 'socketioService','usernameService','recommendService',
        function($scope, $http, $rootScope, $location, socketioService,usernameService,recommendService) {

            var mySocket = socketioService.getSocket();
            $scope.follower = '';

                var username = usernameService.getUsername();
                $scope.username =usernameService.getUsername();
            $scope.topics=[];

            var topics = [];
            $scope.search = function(){
                $http({
                    method : 'GET',
                    url : 'http://localhost:9000/getMyFollowers/' +username
                }).success(function(res, status, headers, config) {
                        var num = 0;
                        var name=new Array();
                        if (res != '') {
                            name=res.split(',');
                            num = res.split(',').length;
                        }
                        $scope.follower = 'Follower Count ' + num + '. Details :' ;
                        var len = name.length;
                    $scope.length=0;
                        while (len>0) {
                            $scope.length=len;
                            console.log("Current index" +$scope.length);
                            var auth = name[len-1];
                            console.log(auth);
                            $scope.getAuthorinfo(auth);
                            len--;
                        }
                        $scope.topics = topics;
                    }
                ).error(function (res, status, headers, config) {
                    console.log(res);
                });
            }

            $scope.getAuthorinfo = function(name) {
                $http({
                    method : 'GET',
                    url : 'http://localhost:9000/getAuthorInfo/' + name.replace(' ', '_')
                }).success(function(res, status, headers, config) {
                    $scope.name = res.name;
                    //
                    // // console.log($scope.name);
                        $scope.homepage = res.homepage;
                    // // console.log($scope.homepage);
                    $scope.picture = res.picture;
                    // // console.log($scope.picture);
                        $scope.interests = res.interests;
                    // // console.log($scope.interests);
                    $scope.email = res.email;
                    // // console.log($scope.email);
                    $scope.affiliation = res.affiliation;
                    // console.log($scope.affiliation);
                    var topic = {
                        name: $scope.name,
                        homepage: $scope.homepage,
                        picture: $scope.picture,
                        interests: $scope.interests,
                        email: $scope.email,
                        affiliation: $scope.affiliation
                    };
                    topics.push(topic)
                       $scope.authorname = '';
                    }
                ).error(function (res, status, headers, config) {
                    console.log(res);
                });

            };

        }
    ]);


