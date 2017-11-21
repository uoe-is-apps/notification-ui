angular.module('publishersubscriber')
    .component('publisherSubscriber', {
        templateUrl: 'app/publisher-subscriber/publisher-subscriber.tpl.html',
        controller: ['SubscriberService', 'PublisherService', 'SubscriptionService', '$location', 'messenger',
            function(SubscriberService, PublisherService, SubscriptionService, $location, messenger) { //, messenger


            var self = this;


            self.refresh = function() {
                self.loading  = true;
                
                //get Subscribers
                SubscriberService.all().then(function (response) {
                    self.subscribers = response.data;
                })
                .catch(function(response) {
                    // error
                })
                .finally(function(){
                    self.loading = false;
                });
                  
                //get Publishers
                PublisherService.all().then(function (response) {
                    self.publishers = response.data;
                    // you can use ui-grid if you need advanced sorting and filtering functionality
                    // using plain HTML table
                    
                    //alert('publishers');
                    //alert(JSON.stringify(self.publishers));
                })
                .catch(function(response) {
                    console.error(response.status, response.data);
                })
                .finally(function(){
                    self.loading = false;
                });
                    
                //get Subscription    
                SubscriptionService.all().then(function (response) {
                    self.subscriptions = response.data;
                })
                .catch(function(response) {
                    console.error(response.status, response.data);
                })
                .finally(function(){
                    self.loading = false;
                });     
            };
            

            self.createSubscription = function() {
                $location.path('/edit-subscription');
            };


            self.deleteSubscription = function(subscription) {                
                    SubscriptionService.delete(subscription).then(function(response) {
                    //messenger.setMessage(messenger.types.SUCCESS, "Subscription '" + subscription.subscriptionId + "' has been deleted.");                      
                    self.refresh();
                }).catch(function(response) {
                    console.error(response.status, response.data);
                });
            };            
            
            
            self.refresh();
        }]
  })
    

    
  .component('scheduledJobs', {
        templateUrl: 'app/publisher-subscriber/scheduled-jobs.tpl.html',
        controller: ['scheduledjob', 'ScheduledTaskService', '$location', 'messenger', 
                    function(_scheduledjob, ScheduledTaskService, $location, messenger) {

            var self = this;

            self.refresh = function() {
                self.loading  = true;

                ScheduledTaskService.all().then(function (response) {
                    self.jobs = response.data;
                })
                .catch(function(response) {
                    console.error(response.status, response.data);
                })
                .finally(function(){
                    self.loading = false;
                });
            };


            // controller API
            self.editScheduledjobs = function(scheduledjob) {               
                _scheduledjob.setScheduledjob(scheduledjob);
                $location.path('/edit-scheduledjobs');
            };
            
            
            self.refresh();
        }]
  })
    
    
    
  .component('editSubscription', {
        templateUrl: 'app/publisher-subscriber/edit-subscription.tpl.html',
        controller: ['user', 'subscription', 'UserService', 'RolesService', 'SubscriberService', 'SubscriptionService', '$location','messenger', '$scope',
                      function(_user, _subscription, UserService, RolesService, SubscriberService, SubscriptionService, $location, messenger, $scope) {

            var self = this;
            self.user = _user.getUser();



            self.refresh = function() {
                self.loading  = true;

                self.subscription = _subscription.getSubscription();

                RolesService.all().then(function(response) {
                    self.roles = response.data;
                })
                .catch(function(response) {
                    console.error(response.status, response.data);
                });


                SubscriberService.all().then(function(response) {
                    self.subscribers = response.data;
                    self.loading = false;
                }).catch(function(response) {
                    console.error(response.status, response.data);
                });

            };
            
            

            self.saveSubscription = function(subscription) {
                subscription.status = 'A';
                subscription.lastUpdated = (new Date()).toJSON();

                SubscriptionService.save(subscription).then(function(response) {
                    _subscription.reset();
                    $location.path('/publisher-subscriber');
                }).catch(function(response) {
                    console.error(response.status, response.data);
                });
            };
            
            
            
            self.cancel = function() {
                _subscription.reset();
                $location.path('/publisher-subscriber');
            };



            self.refresh();
        }]
 })
    
    
 .component('editScheduledjobs', {
        templateUrl: 'app/publisher-subscriber/edit-scheduledjobs.tpl.html',
        controller: ['user', 'scheduledjob', 'ScheduledTaskService', 'UserService', 'RolesService', 'SubscriberService', 'SubscriptionService', '$location','messenger', '$scope',
                      function(_user, _scheduledjob, ScheduledTaskService, UserService, RolesService, SubscriberService, SubscriptionService, $location, messenger, $scope) {


            var self = this;
            self.user = _user.getUser();
            self.scheduledjob = _scheduledjob.getScheduledjob();


            self.startJob = function(scheduledjob) {
                ScheduledTaskService.start(scheduledjob).then(function(response) {
                    //messenger.setMessage(messenger.types.SUCCESS, "Subscription '" + subscription.subscriptionId + "' has been deleted.");
                    _scheduledjob.reset();
                $location.path('/scheduled-jobs');
                }).catch(function(response) {                        
                    console.error(response.status, response.data);
                });
      
            };    


            self.stopJob = function(scheduledjob) {
                ScheduledTaskService.stop(scheduledjob).then(function(response) {
                    //messenger.setMessage(messenger.types.SUCCESS, "Subscription '" + subscription.subscriptionId + "' has been deleted.");
                    _scheduledjob.reset();
                $location.path('/scheduled-jobs');
                }).catch(function(response) {
                    console.error(response.status, response.data);
                });
      
            };    
            
            
            self.rescheduleJob = function(scheduledjob) {
                ScheduledTaskService.reschedule(scheduledjob).then(function(response) {
                    //messenger.setMessage(messenger.types.SUCCESS, "Subscription '" + subscription.subscriptionId + "' has been deleted.");
                    _scheduledjob.reset();
                $location.path('/scheduled-jobs');
                }).catch(function(response) {
                    console.error(response.status, response.data);
                });
            };    
            
            
            self.cancel = function() {
                _scheduledjob.reset();
                $location.path('/scheduled-jobs');
            };
        }]
    }) 
;    
