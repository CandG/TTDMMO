var userApp = angular.module('userApp', ['firebase']);

userApp.controller('userListCtrl', ['$scope', '$firebase', function($scope, $firebase) {
        var ref = new Firebase('https://ttdmmo1.firebaseio-demo.com/users/');
        $scope.users = $firebase(ref);

        $scope.changeFunction = function(user_id) {
            if (user_id != "") {
                var ref2 = new Firebase('https://ttdmmo1.firebaseio-demo.com/users/' + user_id);
                $scope.authUser = $firebase(ref2);
            }
        }
        $scope.changeFunction(1);

    }]);


