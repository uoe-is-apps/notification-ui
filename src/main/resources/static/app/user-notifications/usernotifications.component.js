angular.module('usernotifications')
    .component('userNotifications', {
        templateUrl: 'app/user-notifications/usernotifications.tpl.html',
        controller: ['UserNotificationService', function(UserNotificationService) {

            var self  = this;

            self.gridOptions = {
                enableSorting: true,
                columnDefs: [
                    {
                        field: 'title',
                        width: '*'
                    },
                    {
                        field: 'topic',
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
                    // TODO view notification
                ]
            };

            self.fetchNotifications = function(uun) {
                self.loading = true; // show loading bar

                UserNotificationService.all(uun).then(function(response) {

                    self.gridOptions.data = response.data;
                })
                    .catch(function(response) {
                        self.error = 'Error occurred while retrieving user notifications';
                        console.error(response.status, response.data);
                    })

                    .finally(function() {
                        self.loading = false;
                    });
            }


        }]
    });