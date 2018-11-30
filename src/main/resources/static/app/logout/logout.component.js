angular.module('logout')
.component('logout', {
    templateUrl: 'app/logout/logout.template.html',
            controller: ['$location', '$http', function($location, $http) {
                  
            window.location.href = '/invalidate-session';
    }]

});


