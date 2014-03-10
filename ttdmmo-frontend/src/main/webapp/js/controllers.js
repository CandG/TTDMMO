'use strict';

/* Controllers */

angular.module('myApp.controllers', [])
        .controller('HomeCtrl', ['$scope', 'syncData', function($scope, syncData) {
                //syncData('syncedValue').$bind($scope, 'syncedValue');
            }])
        .controller('myUnlogCntrl', ['$scope', 'loginService', 'syncData', '$location', function($scope, loginService, syncData, $location) {
                $scope.email = null;
                $scope.pass = null;
                $scope.err = null;
                $scope.login = function(cb) {
                    $scope.err = null;
                    if (!$scope.email) {
                        $scope.err = 'Please enter an email address';
                        bootbox.alert($scope.err);
                    }
                    else if (!$scope.pass) {
                        $scope.err = 'Please enter a password';
                        bootbox.alert($scope.err);
                    }
                    else {
                        loginService.login($scope.email, $scope.pass, function(err, user) {
                            $scope.err = err ? err + '' : null;
                            if (!err) {
                                cb && cb(user);
                                $location.path('/map');
                            }
                            else {
                                bootbox.alert($scope.err);
                            }
                        });
                    }
                };
            }])

        .controller('myLogCntrl', ['$scope', 'loginService', 'syncData', '$location', function($scope, loginService, syncData, $location) {
                $scope.logout = function() {
                    loginService.logout();
                };


            }])

        .controller('MapCtrl', ['$scope', '$firebase', function($scope, $firebase) {
                $scope.cities = [];
                $scope.cityMap = false;

                var game = new Game();
                $scope.station = {x: 0, y: 1};
                $scope.warehouse = {x: 0, y: -1};
                $scope.warehouseRoad = {x: 0, y: 0};

                var messRef = new Firebase(FbRef.ref + 'messages/' + $scope.auth.user.uid);
                messRef.on('child_added', function(snapshot) {
                    var userName = snapshot.name(), userData = snapshot.val();
                    bootbox.alert(userData.text);
                    if (userData.xy) {
                        sheetengine.dirty = 1;
                    }
                    messRef.child(snapshot.name()).remove();
                });

                $scope.changeFunction = function(user_id) {
                    if (user_id != "") {
                        var ref2 = new Firebase(FbRef.ref + 'users/' + user_id);
                        $scope.authUser = $firebase(ref2);
                        $scope.userBuses = [];
                        /*$scope.authUser.$on("loaded", function(value) {
                         console.debug(value);
                         console.log("ehehe");
                         if (value) {
                         for (var bus in value.buses) {
                         var ref3 = new Firebase(FbRef.ref + 'buses/' + bus);
                         $scope.userBuses.push($firebase(ref3));
                         }
                         //game.init(XYFromIndex(value.position));
                         }
                         });*/
                        $scope.authUser.$on('change', function() {
                            var authUser = $scope.authUser;
                            console.debug(authUser);
                            if (authUser) {
                                $scope.userBuses = [];
                                for (var bus in authUser.buses) {
                                    var ref3 = new Firebase(FbRef.ref + 'buses/' + bus);
                                    $scope.userBuses.push($firebase(ref3));
                                }
                                if (!game.initialized && authUser.position) {
                                    game.init(XYFromIndex(authUser.position));

                                    var cityRef = new Firebase(FbRef.ref + 'cities/');
                                    cityRef.on('child_added', function(snapshot) {
                                        var userName = snapshot.name(), userData = snapshot.val();
                                        if (userData) {
                                            $scope.cities.push(userData);
                                            if (userData.xy === $scope.authUser.position)
                                                game.initCityMap(userData, true);
                                            else
                                                game.initCityMap(userData);
                                            sheetengine2.dirty = 1;
                                        }
                                    });
                                }
                            }

                        });
                    }
                };
                $scope.fbRef = {
                    build: new Firebase(FbRef.refQ + 'build'),
                    pathVehicle: new Firebase(FbRef.refQ + 'pathVehicle'),
                    newVehicle: new Firebase(FbRef.refQ + 'newVehicle'),
                    sellVehicle: new Firebase(FbRef.refQ + 'sellVehicle')
                };
                $scope.dragmousedown = function($event) {
                    if (game.initialized) {
                        var pxy = game.clickPosition($event.clientX, $event.clientY);
                        $scope.draganddrop.mousePressed = true;
                        $scope.draganddrop.start = pxy;
                    }
                };
                $scope.goToCity = function(vPosition) {
                    var position = {};
                    if (vPosition)
                        position = vPosition;
                    else
                        position = XYFromIndex($scope.authUser.position);
                    var camera = {x: position.x * game.w, y: position.y * game.w, z: position.z * game.w};
                    game.moveViewPoint(camera);
                };
                $scope.dragdrop = function($event) {
                    if (game.initialized) {
                        var pxy = game.clickPosition($event.clientX, $event.clientY);
                        if ($scope.draganddrop.mousePressed) {
                            var self = game;
                            var targetp = {
                                x: self.camera.x + ($scope.draganddrop.start.x - pxy.x),
                                y: self.camera.y + ($scope.draganddrop.start.y - pxy.y),
                                z: 0};
                            self.moveViewPoint(targetp);
                        }
                    }
                };

                $scope.cityMapClick = function($event) {
                    var pxy = game.clickPosition($event.clientX, $event.clientY, sheetengine2);
                    var targetp = {
                        x: (pxy.x / game.cityMap.size),
                        y: (pxy.y / game.cityMap.size),
                        z: 0};
                    $scope.goToCity(targetp);
                };
                $scope.checkCanvasClick = function($event) {
                    var pxy = game.clickPosition($event.clientX, $event.clientY);
                    console.debug(pxy);
                    console.debug(game.objToInsert);
                    var clickpos = sheetengine.scene.getYardFromPos(pxy);
                    console.debug(clickpos.relyardx + ',' + clickpos.relyardy);
                    var index = indexFromXY(clickpos.relyardx, clickpos.relyardy);
                    if (game.firebaseMap.map[index].sheet !== undefined) {
                        //console.debug(game.firebaseMap.map[index].sheet);

                        /*var ctx = sheetengine.context;
                         ctx.save();
                         ctx.lineWidth = 1;
                         ctx.globalAlpha = 0.8;
                         ctx.strokeStyle = '#FFF';
                         var ouv = sheetengine.drawing.getPointuv(pxy);
                         console.debug(ouv);
                         ctx.strokeRect(Math.round(ouv.u) - 20, Math.round(ouv.v) - 30, 40, 40);
                         ctx.restore();*/
                    }
                    if (game.objToInsert === 'path') {

                        if (game.firebaseMap.map[index].type === "A" || game.firebaseMap.map[index].type === "A2" || game.firebaseMap.map[index].type === "A3") {
                            var stops = index + "x";
                            $scope.stops.name = $scope.stops.name + stops;
                        }
                        else if (game.firebaseMap.map[index].type === "BF") {
                            var stops = index + "x" + XYPlusIndex($scope.warehouse.x, $scope.warehouse.y, $scope.authUser.position);
                            $scope.stops.name = stops;
                        }
                    }
                    if (game.objToInsert === 'stop') {
                        if (game.firebaseMap.map[index].type === "BF") {
                            var stops = index + "x" + XYPlusIndex($scope.warehouse.x, $scope.warehouse.y, $scope.authUser.position);
                            $scope.stops.name = stops;
                        }
                    }
                    else if ($scope.authUser.money > 0 && (game.objToInsert === 'A' || game.objToInsert === 'A2' || game.objToInsert === 'A3')) {
                        if (existField(game, index) && isFieldFree(game, index)) {
                            var sh = game.firebaseMap.map[index].sheet;
                            sh.img = imgBuild;
                            sheetengine.dirty = 1;
                            $scope.fbRef.build.push({xy: indexFromXY(clickpos.relyardx, clickpos.relyardy), user_id: $scope.authUser.user_id, type: game.objToInsert})
                        } else if ($scope.authUser.money <= 0) {
                            bootbox.alert("You need more money.");

                        } else {
                            bootbox.alert("You can build road only on free field. You can use bomb to make field free.");
                        }
                    }
                    else if ($scope.authUser.money > 0 && game.objToInsert === 'B') {
                        if (existField(game, index) && (isRoad(game, index) || isCrossRoad(game, index))) {
                            var sh = game.firebaseMap.map[index].sheet;
                            sh.img = imgBuild;
                            sheetengine.dirty = 1;
                            $scope.fbRef.build.push({xy: indexFromXY(clickpos.relyardx, clickpos.relyardy), user_id: $scope.authUser.user_id, type: game.objToInsert})
                        } else if ($scope.authUser.money <= 0) {
                            bootbox.alert("You need more money.");
                        } else
                            bootbox.alert("You can use bomb only on field with road.");
                    }
                };

                $scope.newVehicle = function(name) {
                    $scope.fbRef.newVehicle.push({name: name, xy: XYPlusIndex($scope.station.x, $scope.station.y, $scope.authUser.position), user_id: $scope.authUser.user_id});
                    $scope.vehicle.selection = "default";
                };
                $scope.newPath = function(bus_id) {
                    $scope.vehicle.selection = "2";
                    $scope.setBuilt("path");
                    $scope.vehicle.bus_id = bus_id;

                    $scope.path.message = "Which way should vehicle go?";
                    $scope.path.left = false;
                    $scope.path.right = false;
                    $scope.path.up = true;
                    $scope.path.down = true;
                    $scope.path.back = true;
                    $scope.path.marks = [];
                    $scope.path.stops = [];
                    $scope.stops.name = '';
                    var index = XYPlusIndex($scope.warehouse.x, $scope.warehouse.y, $scope.authUser.position);
                    $scope.path.stops.push(index);
                    index = XYPlusIndex($scope.warehouseRoad.x, $scope.warehouseRoad.y, $scope.authUser.position);
                    $scope.path.stops.push(index);
                    $scope.path.position = index;
                    if (existField(game, index)) {
                        /* console.debug(game.firebaseMap.map[index].sheet);
                         var sh = game.firebaseMap.map[index].sheet;
                         sh.img = imgDepo;*/
                        $scope.path.marks.push(markField(index));
                        sheetengine.dirty = 1;
                    }
                    else {
                        $scope.path.message = "Find warehouse - starting point.";
                        $scope.path.left = false;
                        $scope.path.right = false;
                        $scope.path.up = false;
                        $scope.path.down = false;
                    }
                };
                $scope.cancelPath = function() {
                    var len = $scope.path.marks.length;
                    while (len--) {
                        $scope.path.marks[len].destroy();
                        //$scope.path.marks[len].splice(len, 1)
                    }
                    sheetengine.dirty = 1;
                    $scope.vehicle.selection = 0;
                };
                $scope.setPathDirection = function(direction) {
                    var posun = {x: 0, y: 0};
                    if (direction === 'left') {
                        posun.x = -1;
                    }
                    if (direction === 'right') {
                        posun.x = 1;
                    }
                    if (direction === 'up') {
                        posun.y = -1;
                    }
                    if (direction === 'down') {
                        posun.y = 1;
                    }
                    if (direction === 'back') {

                    }
                    if (direction === 'back') {
                        var x = $scope.path.marks.pop();
                        if (x !== undefined) {
                            x.destroy();
                            sheetengine.dirty = 1;
                            if ($scope.stops.name) {
                                $scope.path.stops.pop();
                                $scope.stops.name = "";
                            }
                            $scope.path.stops.pop();
                            $scope.path.position = $scope.path.stops.pop();
                            $scope.path.stops.push($scope.path.position);
                            $scope.path.left = false;
                            $scope.path.right = false;
                            $scope.path.up = false;
                            $scope.path.down = false;
                            $scope.path.back = false;
                        }
                        else
                            $scope.path.back = true;
                    } else {
                        $scope.path.back = false;

                        var lastIndex = $scope.path.position;
                        var index = XYPlusIndex(posun.x, posun.y, $scope.path.position);

                        console.debug(index);
                        while (existField(game, index) && isRoad(game, index)) {
                            console.debug(game.firebaseMap.map[index]);
                            /*var sh = game.firebaseMap.map[index].sheet;
                             sh.img = imgDepo;
                             sheetengine.dirty = 1;*/

                            $scope.path.marks.push(markField(index));
                            sheetengine.dirty = 1;
                            $scope.path.stops.push(index);

                            lastIndex = index;
                            index = XYPlusIndex(posun.x, posun.y, index);
                            console.debug(index);
                        }

                        $scope.path.left = true;
                        $scope.path.right = true;
                        $scope.path.up = true;
                        $scope.path.down = true;


                        if (existField(game, index)) {
                            if (isCrossRoad(game, index)) {
                                $scope.path.position = index;

                                $scope.path.marks.push(markField(index));
                                sheetengine.dirty = 1;
                                $scope.path.stops.push(index);

                                $scope.path.message = "Which way should vehicle go now?";

                                console.debug(index);
                                var indexL = XYPlusIndex(-1, 0, index);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === 'A' || game.firebaseMap.map[indexL].type === 'A3' || game.firebaseMap.map[indexL].type === 'BF')) {
                                    $scope.path.left = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.left = false;
                                indexL = XYPlusIndex(1, 0, index);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === 'A' || game.firebaseMap.map[indexL].type === 'A3' || game.firebaseMap.map[indexL].type === 'BF')) {
                                    $scope.path.right = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.right = false;
                                indexL = XYPlusIndex(0, -1, index);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === 'A2' || game.firebaseMap.map[indexL].type === 'A3' || game.firebaseMap.map[indexL].type === 'BF')) {
                                    $scope.path.up = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.up = false;
                                indexL = XYPlusIndex(0, 1, index);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === 'A2' || game.firebaseMap.map[indexL].type === 'A3' || game.firebaseMap.map[indexL].type === 'BF')) {
                                    $scope.path.down = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.down = false;
                            }
                            else if (game.firebaseMap.map[index].type === 'BF') {
                                $scope.path.stops.push(index);
                                console.debug($scope.path.stops);
                                $scope.stops.name = "Farma";
                            }
                            else {
                                $scope.path.message = "There is no road!";
                                var indexL = XYPlusIndex(-1, 0, lastIndex);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === 'A' || game.firebaseMap.map[indexL].type === 'A3')) {
                                    $scope.path.left = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.left = false;
                                indexL = XYPlusIndex(1, 0, lastIndex);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === 'A' || game.firebaseMap.map[indexL].type === 'A3')) {
                                    $scope.path.right = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.right = false;
                                indexL = XYPlusIndex(0, -1, lastIndex);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === 'A2' || game.firebaseMap.map[indexL].type === 'A3')) {
                                    $scope.path.up = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.up = false;
                                indexL = XYPlusIndex(0, 1, lastIndex);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === 'A2' || game.firebaseMap.map[indexL].type === 'A3')) {
                                    $scope.path.down = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.down = false;
                            }
                        }
                        else {
                            $scope.path.position = lastIndex;
                            $scope.path.message = "Please move camere to the path.";
                            if (direction === 'left') {
                                $scope.path.left = false;
                            }
                            if (direction === 'right') {
                                $scope.path.right = false;
                            }
                            if (direction === 'up') {
                                $scope.path.up = false;
                            }
                            if (direction === 'down') {
                                $scope.path.down = false;
                            }
                        }
                    }
                    console.debug($scope.path.stops);
                };
                $scope.setPath = function(userBus) {
                    var stops = $scope.path.stops;
                    $scope.fbRef.pathVehicle.push({stops: stops, bus_id: $scope.vehicle.bus_id});

                    $scope.cancelPath();
                };
                $scope.sellVehicle = function() {
                    $scope.fbRef.sellVehicle.push({bus_id: $scope.vehicle.bus_id, user_id: $scope.authUser.user_id});
                    $scope.cancelPath();
                };
                $scope.setBuilt = function(built) {
                    $scope.built = built;
                    game.objToInsert = built;
                };
                $scope.changeFunction($scope.auth.user.uid);
                $scope.built = '';
                $scope.stops = {};
                $scope.path = {};
                $scope.vehicle = {selection: "default", name: "myVehicle"};
                $scope.draganddrop = {mousePressed: false, start: {x: 0, y: 0}};

            }])

        .controller('LoginCtrl', ['$scope', 'loginService', '$location', function($scope, loginService, $location) {
                $scope.email = null;
                $scope.pass = null;
                $scope.confirm = null;
                $scope.createMode = false;

                $scope.login = function(cb) {
                    $scope.err = null;
                    if (!$scope.email) {
                        $scope.err = 'Please enter an email address';
                    }
                    else if (!$scope.pass) {
                        $scope.err = 'Please enter a password';
                    }
                    else {
                        loginService.login($scope.email, $scope.pass, function(err, user) {
                            $scope.err = err ? err + '' : null;
                            if (!err) {
                                cb && cb(user);
                            }
                        });
                    }
                };
            }])
        .controller('RegisterCtrl', ['$scope', 'loginService', '$location', function($scope, loginService, $location) {
                $scope.email = null;
                $scope.pass = null;
                $scope.confirm = null;
                $scope.createMode = true;

                $scope.login = function(cb) {
                    $scope.err = null;
                    if (!$scope.email) {
                        $scope.err = 'Please enter an email address';
                    }
                    else if (!$scope.pass) {
                        $scope.err = 'Please enter a password';
                    }
                    else {
                        loginService.login($scope.email, $scope.pass, function(err, user) {
                            $scope.err = err ? err + '' : null;
                            if (!err) {
                                cb && cb(user);
                            }
                        });
                    }
                };

                $scope.createAccount = function() {
                    $scope.err = null;
                    if (assertValidLoginAttempt()) {
                        loginService.createAccount($scope.email, $scope.pass, function(err, user) {
                            if (err) {
                                $scope.err = err ? err + '' : null;
                            }
                            else {
                                // must be logged in before I can write to my profile
                                $scope.login(function() {
                                    loginService.createProfile(user.uid, user.email);
                                    $location.path('/');
                                });
                            }
                        });
                    }
                };

                function assertValidLoginAttempt() {
                    if (!$scope.email) {
                        $scope.err = 'Please enter an email address';
                    }
                    else if (!$scope.pass) {
                        $scope.err = 'Please enter a password';
                    }
                    else if ($scope.pass !== $scope.confirm) {
                        $scope.err = 'Passwords do not match';
                    }
                    return !$scope.err;
                }
            }])
        .controller('AccountCtrl', ['$scope', 'loginService', 'syncData', '$location', function($scope, loginService, syncData, $location) {
                syncData(['users', $scope.auth.user.uid]).$bind($scope, 'user');

                $scope.fbRef = {
                    changeColor: new Firebase(FbRef.refQ + 'changeColor'),
                };

                $scope.changeColor = function() {
                    var color = $('#color').val();
                    $scope.fbRef.changeColor.push({color: color, user_id: $scope.auth.user.uid});
                };

                $scope.oldpass = null;
                $scope.newpass = null;
                $scope.confirm = null;

                $scope.reset = function() {
                    $scope.err = null;
                    $scope.msg = null;
                };

                $scope.updatePassword = function() {
                    $scope.reset();
                    loginService.changePassword(buildPwdParms());
                };

                function buildPwdParms() {
                    return {
                        email: $scope.auth.user.email,
                        oldpass: $scope.oldpass,
                        newpass: $scope.newpass,
                        confirm: $scope.confirm,
                        callback: function(err) {
                            if (err) {
                                $scope.err = err;
                            }
                            else {
                                $scope.oldpass = null;
                                $scope.newpass = null;
                                $scope.confirm = null;
                                $scope.msg = 'Password updated!';
                            }
                        }
                    }
                }

            }]);