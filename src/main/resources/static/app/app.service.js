angular.module('notify-ui-app')
    .constant('roles', ['USRSUPPORT', 'SYSSUPPORT', 'GROUP', 'SUPERADMIN', 'EMERGENCY',  'USERADMIN'])

    .service('tabPermissions', ['$rootScope', 'roles', 'UserProfile', function($rootScope, roles, UserProfile) {

        return function() {
            UserProfile.then(function(profile) {
                for (var i = 0; i < roles.length; i++) {
                    $rootScope[roles[i]] = profile._hasRole(roles[i]);
                }
            });
        };
    }]);