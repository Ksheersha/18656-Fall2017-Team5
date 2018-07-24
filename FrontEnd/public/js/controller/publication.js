'use strict';

var app = angular.module('SOSECommunity');

app.controller('PublicationCtrl', ['$scope','$http', '$rootScope', '$location', 'socketioService','usernameService',
  function($scope, $http, $rootScope, $location, socketioService,usernameService) {

        var mySocket = socketioService.getSocket();
        $scope.allPapers = [];
        $scope.papers = [];
        $scope.pageLimit = 5;
        $scope.current = 0;

        $scope.getPapers = function(){
            $scope.allPapers.push({title : "A New Method for Measuring the Bottleneck Bandwidth."});
            $scope.allPapers.push({title : "Ontology-Based Universal Knowledge Grid: Enabling Knowledge Discovery and Integration on the Grid."});
            $scope.allPapers.push({title : "SEMAPLAN: Combining Planning with Semantic Matching to Achieve Web Service Composition."});
            $scope.allPapers.push({title : "Introduction to Data Mining"});
            $scope.allPapers.push({title : "Java Technology and Industrial Applications."});
            $scope.allPapers.push({title : "Intelligent Space and Mobile Robots."});
            $scope.allPapers.push({title : "Intelligent Sensors: Analysis and Design."});
            $scope.allPapers.push({title : "Introduction to Multisensor Data Fusion."});
            $scope.allPapers.push({title : "Hardware-level Design Languages."});
            $scope.allPapers.push({title : "Telemedicine in Healthcare Organisations."});
            $scope.allPapers.push({title : "Interface Design Issues for Mobile Commerce."});
            $scope.allPapers.push({title : "One-to-One Video-Conferencing Education."});
            $scope.allPapers.push({title : "Graph Encoding and Recursion Computation."});
            $scope.allPapers.push({title : "Data Mining in Practice."});
            $scope.allPapers.push({title : "Content Description for Face Animation."});
            $scope.allPapers.push({title : "Project Management for IT Projects."});
            $scope.allPapers.push({title : "Portable Portals for M-Commerce."});
            $scope.allPapers.push({title : "Mobile Transaction Models Framework."});
            $scope.allPapers.push({title : "A XML-based Description Language and Execution Environment for Orchestrating Grid Jobs."});
            $scope.allPapers.push({title : "Service-based Collaborative Workflow for DAME."});
            $scope.allPapers.push({title : "Service-Oriented Agility: An initial analysis for the use of Agile methods for SOA development."});
            $scope.allPapers.push({title : "Modeling and Monitoring Dynamic Dependency Environments."});
            $scope.allPapers.push({title : "Reckoning Legislative Compliances with Service Oriented Architecture A Proposed Approach."});
            $scope.allPapers.push({title : "Constraint Driven Web Service Composition in METEOR-S."});
            $scope.allPapers.push({title : "A Comparison of Alternative Web Service Allocation and Scheduling Policies."});
            $scope.allPapers.push({title : "A Generalized Framework for Providing QoS Based Registry in Service Oriented Architecture."});
            $scope.allPapers.push({title : "Perspectives on Service Oriented Architecture."});
            $scope.allPapers.push({title : "Progressive Auction Based Resource Allocation in Service-Oriented Architecture."});
            $scope.allPapers.push({title : "An Advanced Accounting Service for AIX Systems."});
            $scope.allPapers.push({title : "Pyros - an environment for building and orchestrating open services."});
            $scope.allPapers.push({title : "Toward Intention Aware Semantic Web Service Systems."});
            $scope.allPapers.push({title : "A Fine-Grained Access Control Model for Web Services."});
            $scope.allPapers.push({title : "Contextual Collaboration: Platform and Applications."});
            $scope.allPapers.push({title : "A Transaction-Oriented Framework for Composing Transactional Web Services."});
            $scope.allPapers.push({title : "Identity Based Proxy-Signcryption Scheme from Pairings."});
            $scope.allPapers.push({title : "Similarity-based Web Service Matchmaking."});
            $scope.allPapers.push({title : "Fair BPEL Processes Transaction using Non-Repudiation Protocols."});
            $scope.allPapers.push({title : "Decentralized Resilient Grid Resource Management Overlay Networks."});
            $scope.allPapers.push({title : "Case Study: Using Web Services for the Management of Environmental Data."});
            $scope.allPapers.push({title : "Designing Privacy Policies for Adopting RFID in the Retail Industry."});
            $scope.allPapers.push({title : "PEM: A Framework Enabling Continual Optimization of Workflow Process Executions Based upon Business Value Metrics."});
            $scope.allPapers.push({title : "GeneGrid: A Commercial Grid Service Oriented Virtual Bioinformatics Laboratory."});
            $scope.allPapers.push({title : "AWS-Net Traveler: Autonomic Web Services Framework for Autonomic Business Processes."});

            $scope.allPapers.sort(function(a, b) {
                if (a.title < b.title) {
                    return -1;
                } else if (a.title > b.title) {
                    return 1;
                } else {
                    return 0;
                }
            });

        }
        $scope.getPapers();

        for (var i = 0; i < $scope.allPapers.length && i < 5; i++) {
            $scope.papers.push($scope.allPapers[i]);
        }
        $scope.enablePrev = false;
        $scope.enableNext = true;

        $scope.display = function(paper) {
            $http({
                method : 'GET',
                url : 'http://localhost:9000/searchPaper/' + paper.title
            }).success(function(res, status, headers, config) {
                // console.log(paper.title);
                alert("Title: " + res.title + "\n" +
                      "Author: " + res.authors + "\n" +
                      "Citations: " + res.citations + "\n" +
                      "Journal: " + res.journal + "\n" +
                      "Keywords: " + res.keywords + "\n" +
                      "Year: " + res.year);
            }
            ).error(function (res, status, headers, config) {
                console.log(res);
            });
        }

        $scope.prev = function() {
            $scope.papers = [];
            $scope.current -= $scope.pageLimit;
            for (var i = 0; $scope.current + i < $scope.allPapers.length && i < 5; i++) {
                $scope.papers.push($scope.allPapers[i + $scope.current]);
            }
            if ($scope.current - $scope.pageLimit < 0) {
                $scope.enablePrev = false;
            }
            $scope.enableNext = true;
        }

        $scope.next = function() {
            $scope.papers = [];
            $scope.current += $scope.pageLimit;
            for (var i = 0; $scope.current + i < $scope.allPapers.length && i < 5; i++) {
                $scope.papers.push($scope.allPapers[i + $scope.current]);
            }
            if ($scope.current + $scope.pageLimit >= $scope.allPapers.length) {
                $scope.enableNext = false;
            }
            $scope.enablePrev = true;
        }

        $scope.search = function() {
            $scope.papers = [];
            for (var i = 0; i < $scope.allPapers.length; i++) {
                if ($scope.allPapers[i].title.match('.*' + $scope.keywords + '.*')) {
                    $scope.papers.push($scope.allPapers[i]);
                }
            }
            $scope.enablePrev = false;
            $scope.enableNext = false;
            $scope.keywords = '';
        }
    }
]);
