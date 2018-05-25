angular.module('usernotifications')
    .service('UserNotificationService', ['$http', function($http) {
        return {
            all: function(uun) {
                return $http.get('notifications/user/' + uun);
            }
        };
    }]);