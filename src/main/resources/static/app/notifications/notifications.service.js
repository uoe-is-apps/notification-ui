angular.module('notifications')
    .service('NotificationService', ['$http', function($http) {

        /*
         communicates with notification back end
         */
        return {
            all: function() {
                return $http.get('notifications/publisher/notify-ui');
            },
            create: function(notification) {
                return $http.post('notification/', notification);
            },
            update: function(notification) {
                return $http.put('notification/' + notification.notificationId, notification);
            },
            delete: function(notification) {
                return $http.delete('notification/' + notification.notificationId, notification);
            }
        };

    }])
    .service('notification', function() {

        this.notification = {};

        this.setNotification = function(_notification) {
            this.notification = _notification;
        }

        this.getNotification = function() {
            return this.notification;
        }

        this.createNotification = function(_topic) {

            var topic = _topic.charAt(0).toUpperCase() + _topic.slice(1);

            return { publisherId: "notify-ui", topic: topic };
        }

        this.reset = function() {
            this.notification = {};
        }
    });