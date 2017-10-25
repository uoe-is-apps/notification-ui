angular.module('useradmin')
    .service('user', function() {

        this.user = {};

        this.setUser = function(_user) {
            this.user = _user;
        };

        this.getUser = function() {
            return this.user;
        };

        this.reset = function() {
            this.user = {};
        };
    })
    .service('UserService', ['$http', function($http) {
        return {
            all: function() {
                return $http.get('ui-users');
            },
            save: function(user) {
                return $http.put('ui-user/' + user.uun, user);
            },
            delete: function(user) {
                return $http.delete('ui-user/' + user.uun, user);
            }
        };

    }])
    .service('RolesService', ['$http', function($http) {
        return {
            all: function() {
                return $http.get('/ui-roles', {cache: true});
            }
        };
    }]);