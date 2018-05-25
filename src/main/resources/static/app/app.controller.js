angular.module('notify-ui-app')
    .controller('navigation',['$rootScope', '$scope', '$location', 'tabPermissions', function($rootScope, $scope, $location, tabPermissions) {

        $scope.isActiveTab = function(route) {
            return route === $location.path();
        };

        tabPermissions();

        $rootScope.$on("$routeChangeError", function() {

            $location.path("/forbidden");
        });
    }]);