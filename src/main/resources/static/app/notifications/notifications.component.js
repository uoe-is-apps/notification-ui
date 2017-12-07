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
                        cellTemplate: '<span><notification-edit-btn edit="grid.appScope.$ctrl.editNotification(row.entity)"></notification-edit-btn></span>',                        
                        width: '*'
                    }
                ]
            };

            NotificationService.all().then(function(response) {

                var data = response.data;
                var list = [];
                var now = new Date().toISOString();

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

                if (notification) {
                    notification.lastUpdated = new Date();

                    if (notification.notificationId != null) {

                        NotificationService.update(notification).then(function(response) {

                            messenger.setMessage(messenger.types.SUCCESS, "Notification '" + notification.title + "' has been updated.");

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

                            messenger.setMessage(messenger.types.SUCCESS, "Notification '" + notification.title + "' has been saved.");

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
            };

            this.deleteNotification = function(notification) {
                if (notification.notificationId != null) {

                    NotificationService.delete(notification).then(function(response) {

                        messenger.setMessage(messenger.types.SUCCESS, "Notification '" + notification.title + "' has been deleted.");
                        self.back();
                    })
                        .catch(function(response) {

                            // log error
                            console.error(response.data);
                            self.error = 'Error deleting notification.';
                        });
                }
            };

            self.back = function() {
                $location.path('/notifications/' + self.topic);
            };

            // date time picker settings

            self.startDatePickerOptions = {
                format: 'DD-MM-YYYYTHH:mm:ssZ',
                minDate: (self.notification.startDate != null) ? self.notification.startDate : new Date()
            };

            self.endDatePickerOptions = {
                format: 'DD-MM-YYYYTHH:mm:ssZ',
                useCurrent: false
            };

            self.updateRange = function() {

                self.startDatePickerOptions.maxDate = self.notification.endDate;
                self.endDatePickerOptions.minDate = self.notification.startDate;

            };

            // js tree settings
            // TODO: CONVERT INTO ANGULAR DIRECTIVE (ngJsTree not working)

            self.toggle = function() {
                self.showTree = !self.showTree;
            };

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