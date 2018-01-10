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

                    profile._hasSuperAdminRole = function() {
                        var roles = profile.uiRoles;
                        for (var i = 0; i < roles.length; i++) {
                            if (roles[i].roleCode == 'SUPERADMIN') {
                                return true;
                            }
                        }
                        return false;
                    }

                    profile._hasDenyAccessRole = function() {
                        var roles = profile.uiRoles;
                        for (var i = 0; i < roles.length; i++) {
                            if (roles[i].roleCode == 'DENYACCESS'){
                                return true;
                            }
                        }
                        return false;
                    }


                    profile._hasRole = function(role) {
                        var roles = profile.uiRoles;

                        for (var i = 0; i < roles.length; i++) {
                            if (roles[i].roleCode == role) {
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
                    console.log('called hasRole - ' + role);
                    
                    if (profile._hasDenyAccessRole()){
                        console.log('_hasDenyAccessRole');
                        return $q.reject(Access.FORBIDDEN);
                    }else if (profile._hasSuperAdminRole()){
                        console.log('_hasSuperAdminRole');
                        return Access.OK;                   
                    }else if (profile._hasRole(role)){
                        console.log('_hasRole - ' + role);
                        return Access.OK;
                    } else {
                        console.log('reject - rest');
                        return $q.reject(Access.FORBIDDEN);
                    }
   
                });
            },

            forbidden: function() {
                return $q.reject(Access.FORBIDDEN);
            }
        };

        return Access;
    }]);