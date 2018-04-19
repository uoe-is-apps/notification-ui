angular.module('notifications')
    .component('notifications', {
        templateUrl: 'app/notifications/notifications.tpl.html',
        controller: ['$routeParams', 'NotificationService', 'notification', '$location', 'messenger',
                      function($routeParams, NotificationService, _notification, $location, messenger) {

            var self = this;

            self.broadcast = messenger.getMessage();
            messenger.reset();

            self.topic = $routeParams.topic; // notification type
            self.loading = true; // show loading bar

            self.gridOptions = {
                enableSorting: true,
                columnDefs: [
                    {
                        field: 'title',
                        width: '*'
                    },
                    {
                        field: 'startDate',
                        type: 'date',
                        cellFilter: 'date: "dd/MM/yyyy H:mm"',
                        width: '*' // TODO why auto width does not work ?
                    },
                    {
                        field: 'endDate',
                        type: 'date',
                        cellFilter: 'date: "dd/MM/yyyy H:mm"',
                        width: '*'
                    },
                    {
                        name: 'status',
                        enableSorting: false,
                        cellTemplate: '<status row-data="row.entity.status"></status>',
                        width: '*'
                    },
                    {
                        name: 'view',
                        enableSorting: false,
                        //cellTemplate: '<span ng-if="row.entity.status != \'remove\' "><notification-edit-btn edit="grid.appScope.$ctrl.editNotification(row.entity)"></notification-edit-btn></span>',
                        //works cellTemplate: '<span><notification-edit-btn edit="grid.appScope.$ctrl.editNotification(row.entity)"></notification-edit-btn></span>',                        
                        //works <notification-edit-btn edit="grid.appScope.$ctrl.editNotification(row.entity)"></notification-edit-btn>
                        cellTemplate: 
                        '<span>' + 
                        '<button ng-click="grid.appScope.$ctrl.editNotification(row.entity)" class="btn btn-primary">Edit</button>' + 
                        '<button ng-click="grid.appScope.$ctrl.deleteNotification(row.entity)" class="btn btn-danger">Terminate</button>' + 
                        '</span>',                        
                        width: '*'
                    }
                ]
            };

            function lastSunday(month, year) {
              var d = new Date();
              var lastDayOfMonth = new Date(Date.UTC(year || d.getFullYear(), month+1, 0));
              var day = lastDayOfMonth.getDay();
              return new Date(Date.UTC(lastDayOfMonth.getFullYear(), lastDayOfMonth.getMonth(), lastDayOfMonth.getDate() - day));
            }

            function isBST(date) {
              var d = date || new Date();
              var starts = lastSunday(2, d.getFullYear());
              starts.setHours(1);
              var ends = lastSunday(9, d.getFullYear());
              starts.setHours(1);
              return d.getTime() >= starts.getTime() && d.getTime() < ends.getTime();
            }

            NotificationService.all().then(function(response) {

                var data = response.data;
                var list = [];
                
                var now;
                if(isBST(new Date())){                    
                    var nowDate = new Date();
                    nowDate.setHours(nowDate.getHours() + 1);
                    now = nowDate.toISOString();
                    console.log("isBST - yes - " + nowDate);                                   
                }else{                    
                    now = new Date().toISOString();
                    console.log("isBST - no - " + new Date());     
                }
                                
                for (var i = 0; i < data.length; i++) {

                    if (data[i].topic.toLowerCase() == self.topic) {

                        /*
                          flag notification status
                         */
                        if (data[i].endDate < now) {
                            data[i].status = 'remove'; // expired
                        }
                        else if (now < data[i].startDate) {
                            data[i].status = 'time'; // pending
                        }
                        else {
                            data[i].status = 'ok'; // current
                        }

                        list.push(data[i]);
                    }
                }
                self.gridOptions.data = list;

            }).catch(function(response) {
                self.error = 'Error occurred while retrieving notifications';
                console.error(response.status, response.data);
            })

            .finally(function() {
                self.loading = false;
            });

            // controller API

            this.createNotification = function(topic) {
                var notification = _notification.createNotification(topic);
                _notification.setNotification(notification);
                $location.path('/notification/edit/' + topic);
            };

            this.editNotification = function(notification) {
                _notification.setNotification(notification);
                $location.path('/notification/edit/' + notification.topic.toLowerCase());
            };
            
            this.deleteNotification = function(notification) {
                if (notification.notificationId != null) {
                    NotificationService.delete(notification).then(function(response) {

                        messenger.setMessage(messenger.types.SUCCESS, "Notification '" + notification.title + "' has been deleted.");
                        location.reload();
                    })
                        .catch(function(response) {
                            // log error
                            console.error(response.data);
                            self.error = 'Error deleting notification.';
                        });
                }
            };            
            
        }]
    })
    .component('editNotification', {
        templateUrl: 'app/notifications/edit-notification.tpl.html',
        controller: ['$routeParams', 'NotificationService', 'notification', '$location', '$scope', 'messenger',
                      function($routeParams, NotificationService, _notification, $location, $scope, messenger) {

            messenger.reset();

            var self = this;

            self.topic = $routeParams.topic;
            self.notification = _notification.getNotification();

            /*
              check if notification exists, create default if coming here directly from browser URL
             */
            if (angular.equals({}, self.notification)) {

                self.notification = _notification.createNotification(self.topic);
                _notification.setNotification(self.notification);
            }

            // controller API

            self.save = function(notification) {
                processingIndicator = true;        




setTimeout(function(){  
                if (notification) {
                    notification.lastUpdated = new Date();

                    if (notification.notificationId != null) {

                        NotificationService.update(notification).then(function(response) {

                            if(response.data.title == 'ERROR_GROUP_NOTIFICATION_CREATION_INCORRECT_LEVEL'){
                                messenger.setMessage(messenger.types.DANGER, "Error creating notification '" + notification.title + "', incorrect orgunit level selected, only level 4, 5, 6 are allowed.");
                            }else if(response.data.title == 'ERROR_GROUP_NOTIFICATION_CREATION_NO_MEMBER'){
                                messenger.setMessage(messenger.types.DANGER, "Error creating notification '" + notification.title + "', no group member found.");
                            }else if(response.data.title == 'ERROR_GROUP_NOTIFICATION_CREATION_TOO_MANY_MEMBER'){
                                messenger.setMessage(messenger.types.DANGER, "Error creating notification '" + notification.title + "', more than 2000 group members found, please contact administrator.");
                            }else{
                                messenger.setMessage(messenger.types.SUCCESS, "Notification '" + notification.title + "' has been updated.");
                            }

                            _notification.reset();
                            self.back();
                        })
                            .catch(function(response) {

                                // log error
                                console.error(response.data);
                                self.error = 'Error saving notification.';
                            });
                    }

                    else {

                        NotificationService.create(notification).then(function(response) {

                            if(response.data.title == 'ERROR_GROUP_NOTIFICATION_CREATION_INCORRECT_LEVEL'){
                                messenger.setMessage(messenger.types.DANGER, "Error creating notification '" + notification.title + "', incorrect orgunit level selected, only level 4, 5, 6 are allowed.");
                            }else if(response.data.title == 'ERROR_GROUP_NOTIFICATION_CREATION_NO_MEMBER'){
                                messenger.setMessage(messenger.types.DANGER, "Error creating notification '" + notification.title + "', no group member found.");
                            }else if(response.data.title == 'ERROR_GROUP_NOTIFICATION_CREATION_TOO_MANY_MEMBER'){
                                messenger.setMessage(messenger.types.DANGER, "Error creating notification '" + notification.title + "', more than 2000 group members found, please contact administrator.");
                            }else{
                                messenger.setMessage(messenger.types.SUCCESS, "Notification '" + notification.title + "' has been created.");
                            }
        
                            _notification.reset();
                            self.back();
                        })
                            .catch(function(response) {

                                // log error
                                console.error(response.data);
                                self.error = 'Error saving notification.';
                            });
                    }
                }
}, 1000);                
                
                
                
                
                
                
                
            };

            self.back = function() {
                $location.path('/notifications/' + self.topic);
            };

            // date time picker settings
            self.startDatePickerOptions = {
                format: 'DD-MM-YYYYTHH:mm:ssZ'
            };

            self.endDatePickerOptions = {
                format: 'DD-MM-YYYYTHH:mm:ssZ',
                useCurrent: false
            };

            self.updateRange = function(startDate, endDate) {
                
                if (typeof startDate === "undefined") {
                    return true;
                }
                if (typeof endDate === "undefined") {
                    return true;
                }                
                                                
                try{              
                  if(startDate < endDate){
                      return false;
                  }else{                   
                      //alert('show error');
                      return true;
                  }
                }catch(e){
                }                                  
            };

            self.toggle = function() {
                self.showTree = !self.showTree;
            };

            self.ifHideStartDate = function(startDate, endDate, ifNew) {
                if(ifNew === 'new'){
                    return false;
                }else{
                    return true;
                }
                
                var currentTime = (new Date()).getTime();
                try{              
                  if(startDate < currentTime && currentTime < endDate){                  
                      return true;
                  } 
                  return false;
                }catch(e){
                  return false;
              }              
            };

            self.ifShowGroupSelectionTree = function(topic) {
                if(topic === 'Emergency'){
                    return false;
                }else{
                    return true;
                }                
            }
            
            var processingIndicator = false;
            self.ifShowProcessingIndicator = function() {               
                return processingIndicator;               
            }            

            $('#groupTree').jstree({
                'core' : {
                    'data' : {
                        "url": function (node) {
                            return "findGroups";
                        },
                        data: function (node) {

                            var json = {};
                            json["id"] = node.id;
                            return json;
                        },
                        "dataType" : "json"
                    }
                }
            });

            $("#groupTree").bind('select_node.jstree', function(e) {

                $scope.$apply(function() {
                    self.notification.notificationGroup = $("#groupTree").jstree("get_selected")[0];
                    self.notification.notificationGroupName = $('.jstree-clicked').text();
                });

            });
        }]
    });