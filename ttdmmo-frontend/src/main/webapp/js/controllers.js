'use strict';
/* Controllers */

angular.module('myApp.controllers', ['ui.bootstrap'])
        .controller('HomeCtrl', ['$scope', '$firebase', 'syncData', function($scope, $firebase, syncData) {
                //syncData('syncedValue').$bind($scope, 'syncedValue');
                $scope.rank = $firebase(new Firebase(FbRef.refD + 'ranking').limit(5));
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

        .controller('MapCtrl', ['$scope', '$firebase', 'syncData', '$timeout', function($scope, $firebase, syncData, $timeout) {
                var user_id = $scope.auth.user.uid;
                var fbRef = {
                    build: new Firebase(FbRef.refQ + 'build'),
                    pathVehicle: new Firebase(FbRef.refQ + 'pathVehicle'),
                    newVehicle: new Firebase(FbRef.refQ + 'newVehicle'),
                    sellVehicle: new Firebase(FbRef.refQ + 'sellVehicle'),
                    user: new Firebase(FbRef.refD + 'users/' + user_id),
                    manual: new Firebase(FbRef.refD + 'manual/' + user_id),
                    vehicles: new Firebase(FbRef.refD + 'vehicles'),
                    cities: new Firebase(FbRef.refD + 'cities'),
                    rank: new Firebase(FbRef.refD + 'ranking'),
                    messages: new Firebase(FbRef.refD + 'messages/' + user_id)
                };
                $scope.build = '';
                $scope.stops = {};
                $scope.path = {};
                $scope.vehicleTypes = VehicleTypes;
                $scope.vehicle = {type: 0, selection: "default", name: "Vehicle 1", count: 1};
                syncData(['manual', user_id]).$bind($scope, 'manualStep').then(function() {
                    $timeout(function() {
                        $scope.manual = Manual[$scope.manualStep];
                    });
                });

                $scope.nextStep = function() {
                    if ($scope.manualStep === 3) {
                        $scope.setBuild('RDL');
                    }
                    else if ($scope.manualStep === 4) {
                        var index = XYPlusIndex(2, 0, $scope.authUser.position);
                        $scope.mouseUP(null, index);
                    }
                    else if ($scope.manualStep === 5) {
                        $scope.setBuild('RC');
                    }
                    else if ($scope.manualStep === 6) {
                        var index = XYPlusIndex(3, 0, $scope.authUser.position);
                        $scope.mouseUP(null, index);
                    }
                    else if ($scope.manualStep === 7) {
                        $scope.setBuild('RDR');
                    }
                    else if ($scope.manualStep === 8) {
                        var index = XYPlusIndex(3, 1, $scope.authUser.position);
                        $scope.mouseUP(null, index);
                    }
                    else if ($scope.manualStep === 10) {
                        $scope.vehicle.selection = 1;
                    }
                    else if ($scope.manualStep === 11) {
                        $scope.newVehicle($scope.vehicle.name, $scope.vehicle.type);
                    }
                    else if ($scope.manualStep === 12) {
                        var keys = $scope.userBuses.$getIndex();
                        keys.forEach(function(key, i) {
                            if (i === 0)
                                $scope.newPath($scope.userBuses[key].vehicle_id, $scope.userBuses[key].type);
                        });
                    }
                    else if ($scope.manualStep === 13) {
                        $scope.setPathDirection('right');
                    }
                    else if ($scope.manualStep === 14) {
                        $scope.setPathDirection('down');
                    }
                    else if ($scope.manualStep === 15) {
                        $scope.setPath();
                    }
                    $scope.manualStep++;
                    $scope.manual = Manual[$scope.manualStep];
                };

                $scope.draganddrop = {mousePressed: false, move: false, start: {x: 0, y: 0}};
                $scope.draganddropcity = {mousePressed: false, move: false, start: {x: 0, y: 0}};
                $scope.cities = {};
                $scope.showCityMap = false;
                var game = traman;
                $scope.station = {x: 0, y: 1};
                $scope.warehouse = {x: 0, y: -1};
                $scope.warehouseRoad = {x: 0, y: 0};
                var messRef = fbRef.messages;
                messRef.on('child_added', function(snapshot) {
                    var userName = snapshot.name(), userData = snapshot.val();
                    bootbox.alert(userData.text);
                    if (userData.xy) {
                        if (game.firebaseMap.map[userData.xy] !== undefined) {
                            game.firebaseMap.fieldFactory.setFieldCanvas(game.firebaseMap.map[userData.xy].type, game.firebaseMap.map[userData.xy].sheet);
                        }
                        sheetengine.dirty = 1;
                    }
                    messRef.child(snapshot.name()).remove();
                });

                $scope.city = {};
                $scope.city.people = 50;
                $scope.city.food = 0;
                $scope.city.wood = 0;
                $scope.authUser = $firebase(fbRef.user);
                $scope.authUser.$on("loaded", function() {
                    console.debug($scope.authUser.position);
                    if ($scope.authUser.position) {
                        game.init(XYFromIndex($scope.authUser.position));
                        $scope.city = $firebase(fbRef.cities.child($scope.authUser.position));
                    }
                });
                var ref = new Firebase.util.intersection(fbRef.user.child('vehicles'), fbRef.vehicles);

                $scope.userBuses = $firebase(ref);
                $scope.rank = $firebase(fbRef.rank.limit(10));


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
                    console.log(camera);
                    game.moveViewPoint(camera);
                };
                $scope.dragdrop = function($event) {
                    if (game.initialized) {
                        var pxy = game.clickPosition($event.clientX, $event.clientY);
                        if ($scope.draganddrop.mousePressed) {
                            $scope.draganddrop.move = true;
                            var self = game;
                            var targetp = {
                                x: self.camera.x + ($scope.draganddrop.start.x - pxy.x),
                                y: self.camera.y + ($scope.draganddrop.start.y - pxy.y),
                                z: 0};
                            self.moveViewPoint(targetp);
                        }
                    }
                };
                $scope.mouseUP = function($event, vIndex) {
                    if ($scope.draganddrop.move === false) {
                        var index = vIndex;
                        if ($event !== null) {
                            var pxy = game.clickPosition($event.clientX, $event.clientY);
                            console.debug(game.objToInsert);
                            var clickpos = sheetengine.scene.getYardFromPos(pxy);
                            console.debug(clickpos.relyardx + ',' + clickpos.relyardy);
                            index = indexFromXY(clickpos.relyardx, clickpos.relyardy);
                        }
                        if ($scope.authUser.money > 0 && (game.objToInsert.substring(0, 1) === MapTypes.road.road)) {
                            if (existField(game, index) && isFieldFree(game, index)) {
                                var sh = game.firebaseMap.map[index].sheet;
                                sh.img = imgBuild;
                                sheetengine.dirty = 1;
                                fbRef.build.push().setWithPriority({xy: index, user_id: user_id, type: game.objToInsert}, Math.floor((Math.random() * 1000) + 1));
                            } else {
                                bootbox.alert("You can build road only on free field. You can use bomb to make field free.");
                            }
                        }
                        else if ($scope.authUser.money > 0 && game.objToInsert === MapTypes.nothing) {
                            if (existField(game, index) && (isRoad(game, index) || isCrossRoad(game, index)) && !isPathOnField(game, index) && game.firebaseMap.map[index].owner === user_id) {
                                var sh = game.firebaseMap.map[index].sheet;
                                sh.img = imgBuild;
                                sheetengine.dirty = 1;
                                fbRef.build.push().setWithPriority({xy: index, user_id: user_id, type: game.objToInsert}, Math.floor((Math.random() * 1000) + 1));
                            } else if (isPathOnField(game, index)) {
                                bootbox.alert("There is a vehicle path at this road.");
                            } else
                                bootbox.alert("You can use bomb only on your own field with road.");
                        } else if ($scope.authUser.money <= 0 && game.objToInsert != '') {
                            bootbox.alert("You need more money.");
                        }
                    }
                    $scope.draganddrop.move = false;
                    $scope.draganddrop.mousePressed = false;
                };
                $scope.dragmousedowncity = function($event) {
                    if (game.initialized) {
                        var pxy = game.clickPosition($event.clientX, $event.clientY, sheetengine2);
                        $scope.draganddropcity.mousePressed = true;
                        $scope.draganddropcity.start = pxy;
                    }
                };
                $scope.dragdropcity = function($event) {
                    if (game.initialized) {
                        var pxy = game.clickPosition($event.clientX, $event.clientY, sheetengine2);
                        if ($scope.draganddropcity.mousePressed) {
                            $scope.draganddropcity.move = true;
                            var self = game;
                            var targetp = {
                                x: self.cityCamera.x + ($scope.draganddropcity.start.x - pxy.x),
                                y: self.cityCamera.y + ($scope.draganddropcity.start.y - pxy.y),
                                z: 0};
                            //console.log(($scope.draganddropcity.start.y - pxy.y));
                            self.moveViewPointCity(targetp);
                        }
                    }
                };
                $scope.mouseUPcity = function($event) {
                    $scope.draganddropcity.mousePressed = false;
                    if ($scope.draganddropcity.move === false) {
                        var pxy = game.clickPosition($event.clientX, $event.clientY, sheetengine2);
                        var targetp = {
                            x: (pxy.x / game.cityMapOpts.size),
                            y: (pxy.y / game.cityMapOpts.size),
                            z: 0};
                        $scope.goToCity(targetp);
                    }
                    $scope.draganddropcity.move = false;
                };
                $scope.newVehicle = function(name, type) {
                    fbRef.newVehicle.push().setWithPriority({name: name, type: type, xy: XYPlusIndex($scope.station.x, $scope.station.y, $scope.authUser.position), user_id: user_id}, Math.floor((Math.random() * 1000) + 1));
                    $scope.vehicle.selection = "default";
                    $scope.vehicle.count++;
                    $scope.vehicle.name = "Vehicle " + $scope.vehicle.count;
                };
                $scope.newPath = function(vehicle_id, vehicle_type) {
                    $scope.vehicle.selection = "2";
                    $scope.setBuild("path");
                    $scope.vehicle.vehicle_id = vehicle_id;
                    $scope.vehicle.vehicle_type = vehicle_type;
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
                    } else {
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
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === MapTypes.road.left || game.firebaseMap.map[indexL].type === MapTypes.road.cross || isStation(game, indexL))) {
                                    $scope.path.left = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.left = false;
                                indexL = XYPlusIndex(1, 0, index);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === MapTypes.road.left || game.firebaseMap.map[indexL].type === MapTypes.road.cross || isStation(game, indexL))) {
                                    $scope.path.right = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.right = false;
                                indexL = XYPlusIndex(0, -1, index);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === MapTypes.road.right || game.firebaseMap.map[indexL].type === MapTypes.road.cross || isStation(game, indexL))) {
                                    $scope.path.up = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.up = false;
                                indexL = XYPlusIndex(0, 1, index);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === MapTypes.road.right || game.firebaseMap.map[indexL].type === MapTypes.road.cross || isStation(game, indexL))) {
                                    $scope.path.down = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.down = false;
                            }
                            else if (isStation(game, index)) {
                                if (VehNum[game.firebaseMap.map[index].type] === $scope.vehicle.vehicle_type) {
                                    $scope.path.stops.push(index);
                                    console.debug($scope.path.stops);
                                    $scope.stops.name = FieldName[game.firebaseMap.map[index].type];
                                } else
                                    $scope.path.message = "Wrong vehicle type!";

                            }
                            else {
                                $scope.path.message = "There is no road!";
                                var indexL = XYPlusIndex(-1, 0, lastIndex);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === MapTypes.road.left || game.firebaseMap.map[indexL].type === MapTypes.road.cross)) {
                                    $scope.path.left = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.left = false;
                                indexL = XYPlusIndex(1, 0, lastIndex);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === MapTypes.road.left || game.firebaseMap.map[indexL].type === MapTypes.road.cross)) {
                                    $scope.path.right = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.right = false;
                                indexL = XYPlusIndex(0, -1, lastIndex);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === MapTypes.road.right || game.firebaseMap.map[indexL].type === MapTypes.road.cross)) {
                                    $scope.path.up = false;
                                }
                                else if (game.firebaseMap.map[indexL] === undefined)
                                    $scope.path.up = false;
                                indexL = XYPlusIndex(0, 1, lastIndex);
                                if (lastIndex !== indexL && existField(game, indexL) && (game.firebaseMap.map[indexL].type === MapTypes.road.right || game.firebaseMap.map[indexL].type === MapTypes.road.cross)) {
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
                $scope.setPath = function() {
                    var stops = $scope.path.stops;
                    fbRef.pathVehicle.push().setWithPriority({stops: stops, vehicle_id: $scope.vehicle.vehicle_id}, Math.floor((Math.random() * 1000) + 1));
                    $scope.cancelPath();
                };
                $scope.sellVehicle = function() {
                    fbRef.sellVehicle.push().setWithPriority({vehicle_id: $scope.vehicle.vehicle_id, user_id: user_id}, Math.floor((Math.random() * 1000) + 1));
                    $scope.cancelPath();
                };
                $scope.setBuild = function(build) {
                    $scope.build = build;
                    game.objToInsert = build;
                };
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
                    } else if (!$scope.pass) {
                        $scope.err = 'Please enter a password';
                    } else {
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
                $scope.city = null;
                $scope.createMode = true;
                $scope.err = null;
                $scope.login = function(cb) {
                    $scope.err = null;
                    if (!$scope.email) {
                        $scope.err = 'Please enter an email address';
                        bootbox.alert($scope.err);
                    } else if (!$scope.pass) {
                        $scope.err = 'Please enter a password';
                        bootbox.alert($scope.err);
                    } else {
                        loginService.login($scope.email, $scope.pass, function(err, user) {
                            $scope.err = err ? err + '' : null;
                            if (!err) {
                                cb && cb(user);
                            } else {
                                bootbox.alert($scope.err);
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
                                bootbox.alert($scope.err);
                            }
                            else {
                                // must be logged in before I can write to my profile
                                $scope.login(function() {
                                    loginService.createProfile(user.uid, user.email, $scope.city);
                                    $location.path('/');
                                });
                            }
                        });
                    }
                };
                function assertValidLoginAttempt() {
                    if (!$scope.email) {
                        $scope.err = 'Please enter an email address';
                        bootbox.alert($scope.err);
                    } else if (!$scope.pass) {
                        $scope.err = 'Please enter a password';
                        bootbox.alert($scope.err);
                    } else if ($scope.pass !== $scope.confirm) {
                        $scope.err = 'Passwords do not match';
                        bootbox.alert($scope.err);
                    } else if (!$scope.city || $scope.city.length > 50 || $scope.city.length < 4) {
                        $scope.err = 'Please enter a city name (min 3  and max 50 characters)';
                        bootbox.alert($scope.err);
                    }
                    return !$scope.err;
                }
            }])
        .controller('AccountCtrl', ['$scope', 'loginService', 'syncData', '$location', function($scope, loginService, syncData, $location) {
                syncData(['users', $scope.auth.user.uid]).$bind($scope, 'user');
                var fbRef = {
                    changeColor: new Firebase(FbRef.refQ + 'changeColor'),
                };
                $scope.changeColor = function() {
                    var color = $('#color').val();
                    fbRef.changeColor.push().setWithPriority({color: color, user_id: $scope.auth.user.uid}, Math.floor((Math.random() * 1000) + 1));
                    bootbox.alert("Color was changed.");
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