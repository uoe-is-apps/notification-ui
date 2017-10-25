angular.module('publishersubscriber')
    .component('publisherSubscriber', {
        templateUrl: 'app/publisher-subscriber/publisher-subscriber.tpl.html',
        controller: ['PublisherSubscriberService', function(PublisherSubscriberService) {

            /*
              the current publisher-subsciber screen needs more thought
              TODO remove the comment after implementing this controller
             */


        }]
    })
    .component('scheduledJobs', {
        templateUrl: 'app/publisher-subscriber/scheduled-jobs.tpl.html',
        controller: ['PublisherSubscriberService', function(PublisherSubscriberService) {

            /*
              ideally we should display jobs by publisher
              and if possible, add job controls (stop, reschedule etc.)
              added dummy edit button
              TODO remove the comment after implementing this controller
             */

            var self = this;

            self.refresh = function() {
                self.loading  = true;

                PublisherSubscriberService.jobs().then(function (response) {

                    self.jobs = response.data;
                    // you can use ui-grid if you need advanced sorting and filtering functionality
                    // using plain HTML table
                })
                    .catch(function(response) {
                        // error
                    })
                    .finally(function(){
                        self.loading = false;
                    });
            };

            self.refresh();
        }]
    });