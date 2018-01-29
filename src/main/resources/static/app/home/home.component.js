angular.module('home')
.component('home', {
    templateUrl: 'app/home/home.template.html',
    controller: function HomeController() {
        this.message = 'You are HOME.';
    }

});