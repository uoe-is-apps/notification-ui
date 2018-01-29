angular.module('logout')
.component('logout', {
    templateUrl: 'app/logout/logout.template.html',
    //controller: function LogoutController() {
            controller: ['$location', '$http', function($location, $http) {  
                  
            $http.get('invalidate-session');                    
            
            [].forEach.call(document.querySelectorAll('.navbar-nav'), function (el) {
              el.style.visibility = 'hidden';
            });            
            
    }]

});


