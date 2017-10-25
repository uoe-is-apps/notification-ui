angular.module('access')
    .service('Auth', ['$http', function($http) {
        return {
            authenticate: function() {
                return $http.get('/user-role');
            }
        };
    }])

    .service('UserProfile', ['Auth', function UserProfile(Auth) {

        var getUserProfile = function() {

            var userProfile = {
                _isAuthenticated: false,

                _hasRole: function(role) {
                    return false;
                }
            };

            return Auth.authenticate().then(function(response) {

                var profile = response.data;

                if (profile) {
                    profile._isAuthenticated = true;

                    profile._hasRole = function(role) {
                        var roles = profile.uiRoles;

                        for (var i = 0; i < roles.length; i++) {
                            if (roles[i].roleCode == role || roles[i].roleCode == 'SUPERADMIN') {
                                return true;
                            }
                        }
                        return false;
                    };
                }

                return angular.extend(userProfile, profile);
            });
        };

        return getUserProfile();
    }])

    .factory('Access', ['$q', 'UserProfile', function($q, UserProfile) {

        var Access = {
            OK: 200,
            UNAUTHORIZED: 401,
            FORBIDDEN: 403,

            isAuthenticated: function() {
                return UserProfile.then(function(profile) {
                    if (profile._isAuthenticated) {
                        return Access.OK;
                    }
                    return $q.reject(Access.UNAUTHORIZED); // for now, not sure http code 401 fits but need to distinguish between no user and user with no role
                });
            },

            hasRole: function(role) {
                return UserProfile.then(function(profile) {

                    if (profile._isAuthenticated && profile._hasRole(role)) {
                        return Access.OK;
                    }
                    return $q.reject(Access.FORBIDDEN);
                });
            },

            forbidden: function() {
                return $q.reject(Access.FORBIDDEN);
            }
        };

        return Access;
    }]);