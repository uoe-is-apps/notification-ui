angular.module('notifications')
    .directive('status', function() {
        return {
            restrict: 'AE',
            scope: {
                status: '=rowData'
            },
            template: '<span class="glyphicon glyphicon-{{ status }}"></span>'
        };
    })
    .directive('notificationEditBtn', function() {
        return {
            restrict: 'EA',
            scope: {
                edit: '&'
            },
            template: '<button class="btn btn-primary btn-sm" ng-click="edit()">Edit</button>'
        }
    });