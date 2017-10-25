angular.module('publishersubscriber')
    .service('PublisherSubscriberService', ['$http', function($http) {
        return {
            jobs: function() {
                return $http.get('scheduled-tasks');
            }
        };
    }]);