angular.module('publishersubscriber')
    .service('ScheduledTaskService', ['$http', function($http) {
        return {
            start: function(scheduledjob) {
                var id = scheduledjob.jobName;
                return $http.post('start-job/' + id);
            },
            stop: function(scheduledjob) {
                var id = scheduledjob.jobName;
                return $http.post('stop-job/' + id);
            },  
            reschedule: function(scheduledjob) {
                var id = scheduledjob.jobName;
                var interval = scheduledjob.newInterval;
                return $http.post('reschedule-job/' + id + '/' + interval);                
            },                           
            all: function() {
                return $http.get('scheduled-tasks');
            }
        };
    }])

    .service('TopicService', ['$http', function($http) {
        return {
            all: function() {
                return $http.get('topics');
            }
        };
    }])

    .service('SubscriberService', ['$http', function($http) {
        return {
            all: function() {
                return $http.get('subscribers');
            }
        };
    }])

    .service('PublisherService', ['$http', function($http) {
        return {
            all: function() {
                return $http.get('publishers');
            }
        };
    }])

    .service('SubscriptionService', ['$http', function($http) {
        return {
            save: function(subscription) {
                return $http.post("/topic-subscriptions", subscription);
            },       
            delete: function(subscription) {
                return $http.delete('/topic-subscriptions/' + subscription.subscriptionId, subscription);
            },                
            all: function() {
                return $http.get('topic-subscriptions');
            }
        };
    }])

    .service('subscription', function() {

        this.subscription = {}; 

        this.setSubscription = function(_subscription) {
            this.subscription = _subscription;
        };

        this.getSubscription = function() {
            return this.subscription;
        };

        this.reset = function() {
            this.subscription = {};
        };
    })

    .service('scheduledjob', function() {

        this.scheduledjob = {}; 

        this.setScheduledjob = function(_scheduledjob) {
            this.scheduledjob = _scheduledjob;
        };

        this.getScheduledjob = function() {
            return this.scheduledjob;
        };

        this.reset = function() {
            this.scheduledjob = {};
        };
     })
;