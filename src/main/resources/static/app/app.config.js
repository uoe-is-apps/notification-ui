angular.module('notify-ui-app')
    .config(['$routeProvider', function($routeProvider) {

        $routeProvider
            .when('/', {
                template: '<home></home>',
                activetab: 'home'
            })
            .when('/home', {
                template: '<home></home>',
                activetab: 'home'
            })
            .when('/notifications/:topic', {
                template: '<notifications></notifications>',

                resolve: {access: ['$route', 'Access', '$location', function($route, Access, $location) {

                    if ($route.current.params.topic == 'group') {
                        return Access.hasRole('GROUP');
                    }
                    else if ($route.current.params.topic == 'emergency') {
                        return Access.hasRole('EMERGENCY');
                    }
                    else {
                        return $location.path('/404');
                    }

                }]},

                activetab: 'notifications'
            })
            .when('/notification/edit/:topic', {
                template: '<edit-notification></edit-notification>',
                resolve: {access: ['$route', 'Access', '$location', function($route, Access, $location) {

                    if ($route.current.params.topic == 'group') {
                        return Access.hasRole('GROUP');
                    }
                    else if ($route.current.params.topic == 'emergency') {
                        return Access.hasRole('EMERGENCY');
                    }
                    else {
                        return $location.path('/404');
                    }

                }]},

                activetab: 'notifications'
            })
            .when('/user-administration', {
                template: '<user-admin></user-admin>',
                resolve: {access: ['Access', function(Access) {
                    return Access.hasRole('USERADMIN');
                }]},

                activetab: 'user-administration'
            })
            .when('/edit-user', {
                template: '<edit-user></edit-user>',
                resolve: {access: ['Access', function(Access) {
                    return Access.hasRole('USERADMIN');
                }]},

                activetab: 'user-administration'
            })
            .when('/user-notifications', {
                template: '<user-notifications></user-notifications>',
                resolve: {access: ['Access', function(Access) {
                    return Access.hasRole('USRSUPPORT');
                }]},

                activetab: 'user-notifications'
            })
            .when('/scheduled-jobs', {
                template: '<scheduled-jobs></scheduled-jobs>',
                resolve: {access: ['Access', function(Access) {
                    return Access.hasRole('SYSSUPPORT');
                }]},

                activetab: 'scheduled-jobs'
            })
            .when('/publisher-subscriber', {
                template: '<publisher-subscriber></publisher-subscriber>',
                resolve: {access: ['Access', function(Access) {
                    return Access.hasRole('SYSSUPPORT');
                }]},

                activetab: 'publisher-subscriber'
            })
            .when('/forbidden', {
                template: '<forbidden></forbidden>',
                activetab: 'home'
            })
            .when('/404', {
                template: '<http404></http404>'
            })
            .otherwise({ redirectTo: '/'});
    }]);