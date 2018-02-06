angular.module('logout')
.component('logout', {
    templateUrl: 'app/logout/logout.template.html',
    //controller: function LogoutController() {
            controller: ['$location', '$http', function($location, $http) {  
                  
            $http.get('invalidate-session');                    
            
            //[].forEach.call(document.querySelectorAll('.navbar-nav'), function (el) {
            //  el.style.visibility = 'hidden';
            //});            
            
            if(window.location.href.indexOf("dev.") != -1){
                 window.location.href = 'https://www-dev.ease.ed.ac.uk/logout.cgi';
            }else if(window.location.href.indexOf("test.") != -1){
                 window.location.href = 'https://www-test.ease.ed.ac.uk/logout.cgi';
            }else{
                 window.location.href = 'https://www.ease.ed.ac.uk/logout.cgi';
            }   
            
    }]

});


