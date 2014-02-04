var userApp = angular.module('userApp', ['firebase']);

userApp.controller('userListCtrl', ['$scope', '$firebase', function($scope, $firebase) {
        var ref = new Firebase('https://ttdmmo1.firebaseio-demo.com/users/');
        $scope.users = $firebase(ref);
        $scope.station = {x: 0, y: 1};
        $scope.warehouse = {x: 0, y: -2};
        $scope.changeFunction = function(user_id) {
            if (user_id != "") {
                var ref2 = new Firebase('https://ttdmmo1.firebaseio-demo.com/users/' + user_id);
                $scope.authUser = $firebase(ref2);
                $scope.userBuses = [];
                $scope.authUser.$on("loaded", function(value) {
                    for (var bus in value.buses) {
                        var ref3 = new Firebase('https://ttdmmo1.firebaseio-demo.com/buses/' + bus);
                        $scope.userBuses.push($firebase(ref3));
                    }

                });
            }
        };
        $scope.fbRef = {
            fronta: new Firebase('https://ttdmmoq1.firebaseio-demo.com/fronta'),
            fronta3: new Firebase('https://ttdmmoq1.firebaseio-demo.com/fronta3')
        };
        $scope.checkCanvasClick = function($event) {
            var puv = {
                u: $event.clientX - sheetengine.canvas.offsetLeft + pageXOffset,
                v: $event.clientY - sheetengine.canvas.offsetTop + pageYOffset
            };
            var w = sheetengine.canvas.width / 2;
            var h = sheetengine.canvas.height / 2;
            puv.u = (puv.u - w) / demo.zoom + w;
            puv.v = (puv.v - h) / demo.zoom + h;
            var pxy = sheetengine.transforms.inverseTransformPoint({
                u: puv.u + sheetengine.scene.center.u,
                v: puv.v + sheetengine.scene.center.v
            });
            console.debug(pageXOffset);
            console.debug(pxy);
            console.debug(demo.objToInsert);
            var clickpos = sheetengine.scene.getYardFromPos(pxy);
            console.debug(clickpos.relyardx + ',' + clickpos.relyardy);
            var index = indexFromXY(clickpos.relyardx, clickpos.relyardy);
            if (demo.objToInsert === 'stop') {
                if (demo.firebaseMap.map[index].type === "BF") {
                    var stops = index + "x" + indexFromXY($scope.warehouse.x, $scope.warehouse.y);
                    $scope.stops.name = stops;
                }
            }
            else if (demo.objToInsert != '')
                $scope.fbRef.fronta.push({x: clickpos.relyardx, y: clickpos.relyardy, user_id: "1", type: demo.objToInsert})
        };
        $scope.newVehicle = function(name) {
            var ref2 = new Firebase('https://ttdmmoq1.firebaseio-demo.com/fronta2/');
            $scope.fronta2 = $firebase(ref2);
            $scope.fronta2.$add({name: name, x: $scope.station.x, y: $scope.station.y, user_id: $scope.authUser.user_id});
            $scope.vehicle.selection = "default";
        };
        $scope.newPath = function(bus_id) {
            $scope.vehicle.selection = "2";
            $scope.setBuilt("stop");
            $scope.vehicle.bus_id = bus_id;
        };
        $scope.setPath = function(userBus) {
            var stops = $scope.stops.name;
            $scope.fbRef.fronta3.push({stops: stops, bus_id: $scope.vehicle.bus_id});
            $scope.vehicle.selection = "default";
        };
        $scope.setBuilt = function(built) {
            $scope.built = built;
            demo.objToInsert = built;
        };
        $scope.changeFunction(1);
        $scope.built = 0;
        $scope.stops = {};
        $scope.vehicle = {selection: "default"};
    }]);


