angular.module('publishersubscriber')
    .component('publisherSubscriber', {
        templateUrl: 'app/publisher-subscriber/publisher-subscriber.tpl.html',
        controller: ['SubscriberService', 'PublisherService', 'SubscriptionService', 'TopicService', '$location', 'messenger', '$scope', 'uiGridConstants', 
            function(SubscriberService, PublisherService, SubscriptionService, TopicService, $location, messenger, $scope, uiGridConstants) { //, messenger

 
            $scope.deleteSubscription = function(subscription) {
                  SubscriptionService.delete(subscription).then(function(response) {
                    //messenger.setMessage(messenger.types.SUCCESS, "Subscription '" + subscription.subscriptionId + "' has been deleted.");                      
                    self.refresh();
                }).catch(function(response) {
                    console.error(response.status, response.data);
                });
           };
 
            var self = this;
 
            self.gridOptionsSubscriptions = {
                enableSorting: true,
                columnDefs: [
                    {
                        field: 'subscriptionId',
                        width: '*'
                    },
                    {
                        field: 'topic',
                        width: '*',
                        sort: { direction: uiGridConstants.ASC, priority: 0 }
                    },
                    {
                        field: 'subscriberId',
                        width: '*'
                    },                    
                    {
                        field: 'status',
                        width: '*'
                    },
                    {
                        field: 'lastUpdated',
                        cellFilter: 'date: "dd/MM/yyyy H:mm"',
                        width: '*'
                    },
                    {
                        name: 'view',
                        enableSorting: false, 
                        cellTemplate: '<div><button ng-click="grid.appScope.deleteSubscription(row.entity)" class="btn btn-danger">Delete</button></div>',                                        
                        width: '*'
                    }                   
                    
                ]
            };
                  
                
                
            self.gridOptionsTopics = {
                enableSorting: true,
                columnDefs: [
                    {
                        field: 'topicId',
                        width: '*',
                        sort: { direction: uiGridConstants.ASC, priority: 0 }
                    },
                    {
                        field: 'description',
                        width: '*'
                    },
                    {
                        field: 'publisherId',
                        width: '*'
                    },
                    {
                        field: 'status',
                        width: '*'
                    },
                    {
                        field: 'lastUpdated',
                        cellFilter: 'date: "dd/MM/yyyy H:mm"',
                        width: '*'
                    },                    
                    
                ]
            };
                   
            
            self.gridOptionsPublishers = {
                enableSorting: true,
                columnDefs: [
                    {
                        field: 'publisherId',
                        width: '*',
                        sort: { direction: uiGridConstants.ASC, priority: 0 }
                    },
                    {
                        field: 'description',
                        width: '*'
                    },
                    {
                        field: 'key',
                        width: '*'
                    },
                    
                    {
                        field: 'publisherType',
                        width: '*'
                    },
                    {
                        field: 'status',
                        width: '*'
                    },
                    {
                        field: 'lastUpdated',
                        cellFilter: 'date: "dd/MM/yyyy H:mm"',
                        width: '*'
                    },                    
                    
                ]
            };
                     
            self.gridOptionsSubscribers = {
                enableSorting: true,
                columnDefs: [
                    {
                        field: 'subscriberId',
                        width: '*',
                        sort: { direction: uiGridConstants.ASC, priority: 0 }
                    },
                    {
                        field: 'description',
                        width: '*'
                    },
                    {
                        field: 'type',
                        width: '*'
                    },
                    {
                        field: 'status',
                        width: '*'
                    },
                    {
                        field: 'lastUpdated',
                        cellFilter: 'date: "dd/MM/yyyy H:mm"',
                        width: '*'
                    },                    
                    
                ]
            };            
                  


            self.refresh = function() {
                self.loading  = true;
                
                //get Subscribers
                SubscriberService.all().then(function (response) {
                    self.subscribers = response.data;
                    self.gridOptionsSubscribers.data = response.data;                    
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
                    self.gridOptionsPublishers.data = response.data;
                })
                .catch(function(response) {
                    console.error(response.status, response.data);
                })
                .finally(function(){
                    self.loading = false;
                });
                    
                //get Topics
                TopicService.all().then(function (response) {
                    self.topics = response.data;
                    self.gridOptionsTopics.data = response.data;
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
                    self.gridOptionsSubscriptions.data = response.data;
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


            self.refresh();
        }]
  })
    

    
  .component('scheduledJobs', {
        templateUrl: 'app/publisher-subscriber/scheduled-jobs.tpl.html',
        controller: ['scheduledjob', 'ScheduledTaskService', '$location', 'messenger', '$scope',
                    function(_scheduledjob, ScheduledTaskService, $location, messenger, $scope) {

            var self = this;

            self.status = '';

            self.gridOptions = {
                gracePeriod: 120000, // milliseconds
                enableSorting: true,
                columnDefs: [
                    {
                        field: 'jobName',
                        width: '*'
                    },
                    {
                        field: 'prevRun',
                        cellFilter: 'date: "dd/MM/yyyy H:mm"',
                        width: '*',
                    },
                    {
                        field: 'nextRun',
                        cellFilter: 'date: "dd/MM/yyyy H:mm"',
                        width: '*',
                        cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                            var normalizedCellDate = grid.getCellValue(row,col).replace(/[^a-zA-Z0-9]/g, '-'),
                                dateItems       = normalizedCellDate.split('-'),
                                now = new Date(),
                                cellDate;
                            if (normalizedCellDate === '' || dateItems.length !== 6) return '';
                            if (dateItems[0] > 1000) {
                                // Year first, assume YYYY/MM/DD HH:MM:SS
                                cellDate = new Date(dateItems[0], dateItems[1]-1, dateItems[2],
                                                dateItems[3], dateItems[4], dateItems[5]);
                            } else {
                                // Year first, assume DD/MM/YYYY HH:MM:SS
                                cellDate = new Date(dateItems[2], dateItems[1]-1, dateItems[0],
                                                dateItems[3], dateItems[4], dateItems[5]);
                            }
                            return cellDate.getTime() + grid.options.gracePeriod < now.getTime() ?
                                'glyphicon glyphicon-remove text-danger' : '';
                        }
                    },
                    {
                        field: 'triggerState',
                        width: '*',
                        cellClass: function(grid, row, col, rowRenderIndex, colRenderIndex) {
                            var cellText = grid.getCellValue(row,col);
                            switch (cellText) {
                                case 'ERROR' :
                                case 'BLOCKED' : return 'glyphicon glyphicon-remove text-danger';
                                case 'PAUSED_BLOCKED' :
                                case 'PAUSED' : return 'glyphicon glyphicon-alert text-warning';
                                default :
                            }
                            return '';
                        },
                        cellTemplate: "<div class=\"ui-grid-cell-contents\" title=\"TOOLTIP\"><span>{{COL_FIELD CUSTOM_FILTERS}}</span></div>"
                    },
                    {
                        field: 'repeatInterval',
                        width: '*'
                    },   
                    {
                        field: 'timesExecuted',
                        width: '*'
                    },
                    {
                        name: 'view',
                        enableSorting: false,
                        cellTemplate: '<span><notification-edit-btn edit="grid.appScope.$ctrl.editScheduledjobs(row.entity)"></notification-edit-btn></span>',
                        width: '*'
                    }                   
                    
                ]
            };

            self.refresh = function() {
                self.loading  = true;
                self.status  = 'loading';

                ScheduledTaskService.all().then(function (response) {
                    var normalizedCellDate,
                        dateItems,
                        now = new Date(),
                        cellDate;

                    self.status  = 'ok';
                    self.jobs = response.data;
                    self.gridOptions.data = response.data;
                    var status = 'ok';
                    for(i=0; i<response.data.length; i++) {
                        if (response.data[i].triggerState === 'ERROR' || response.data[i].triggerState === 'BLOCKED') {
                            self.status = 'error';
                            return; // error state: no need to check anything else.
                        }
                        if (response.data[i].triggerState === 'PAUSED' || response.data[i].triggerState === 'PAUSED_BLOCKED') {
                            self.status = 'warning';
                            continue; // don't check the dates on a paused task.
                        }
                        normalizedCellDate = response.data[i].nextRun.replace(/[^a-zA-Z0-9]/g, '-');
                        dateItems = normalizedCellDate.split('-');
                        if (normalizedCellDate === '' || dateItems.length !== 6) continue;
                        if (dateItems[0] > 1000) {
                            // Year first, assume YYYY/MM/DD HH:MM:SS
                            cellDate = new Date(dateItems[0], dateItems[1]-1, dateItems[2], dateItems[3], dateItems[4], dateItems[5]);
                        } else {
                            // Year first, assume DD/MM/YYYY HH:MM:SS
                            cellDate = new Date(dateItems[2], dateItems[1]-1, dateItems[0], dateItems[3], dateItems[4], dateItems[5]);
                        }
                        if (cellDate.getTime() + self.gridOptions.gracePeriod  < now.getTime()) {
                            self.status = 'error'; // error state: no need to check anything else.
                            return;
                        }
                    }
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
        controller: ['user', 'subscription', 'UserService', 'RolesService', 'SubscriberService', 'SubscriptionService', 'TopicService', '$location','messenger', '$scope',
                      function(_user, _subscription, UserService, RolesService, SubscriberService, SubscriptionService, TopicService, $location, messenger, $scope) {

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


                TopicService.all().then(function(response) {
                    self.topics = response.data;
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



            self.isPaused = function(scheduledjob) {
                if(scheduledjob.triggerState === 'PAUSED' || scheduledjob.triggerState === 'PAUSED_BLOCKED'){
                    return true;
                }else{
                    return false;
                }
            }; 



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
