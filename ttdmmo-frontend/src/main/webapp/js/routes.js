"use strict";

angular.module('myApp.routes', ['ngRoute'])

        // configure views; the authRequired parameter is used for specifying pages
        // which should only be available while logged in
        .config(['$routeProvider', function($routeProvider) {
                $routeProvider.when('/home', {
                    templateUrl: 'partials/home.html',
                    controller: 'HomeCtrl'
                });

                $routeProvider.when('/map', {
                    authRequired: true, // must authenticate before viewing this page
                    templateUrl: 'partials/map.html',
                    controller: 'MapCtrl'
                });

                $routeProvider.when('/account', {
                    authRequired: true, // must authenticate before viewing this page
                    templateUrl: 'partials/account.html',
                    controller: 'AccountCtrl'
                });
                $routeProvider.when('/login', {
                    templateUrl: 'partials/login.html',
                    controller: 'LoginCtrl'
                });
                $routeProvider.when('/register', {
                    templateUrl: 'partials/register.html',
                    controller: 'RegisterCtrl'
                });
                $routeProvider.when('/contact', {
                    templateUrl: 'partials/contact.html',
                    controller: 'HomeCtrl'
                });

                $routeProvider.otherwise({redirectTo: '/home'});
            }]);