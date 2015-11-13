angular.module('notify-ui-app', [ 'ngRoute' , 'ngCkeditor' , 'ui.bootstrap', 'checklist-model']).config(function($routeProvider, $httpProvider) {

	$routeProvider.when('/', {
		templateUrl : 'listEmergencyNotification.html',
		controller : 'listEmergencyNotificationController',
		activetab : 'emergency-notifications'
	}).
	when('/edit-emergency-notification',{
		templateUrl : 'editEmergencyNotification.html',
		controller : 'editEmergencyNotificationController',
		activetab : 'emergency-notifications'
	}).
	when('/user-administration',{
    		templateUrl : 'listUiUsers.html',
    		controller : 'listUiUsersController',
    		activetab : 'user-administration'
    }).
    when('/edit-user',{
        		templateUrl : 'editUiUser.html',
        		controller : 'editUiUsersController',
        		activetab : 'user-administration'
    }).
	otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

})
.service("message", function ErrorMessage ($rootScope)
{

  this.successMessageData="";
  this.errorMessageData="";

  this.setSuccessMessage = function(message)
  {
    this.successMessageData = message;
    $rootScope.$broadcast("successMessageUpdated");
  }

  this.successMessage = function()
  {
    return this.successMessageData;

  }


  this.setErrorMessage = function(message)
  {
      this.errorMessageData = message;
       $rootScope.$broadcast("errorMessageUpdated");
  }

  this.errorMessage = function()
  {
      return this.errorMessageData;
  }



})
.service("notification",function Notification()
{

   this.notificationData = { publisherId: "notify-ui", topic: "Emergency", startDate: new Date(), lastUpdated: new Date()};

   this.setNotification = function(notification)
   {
     this.notificationData = notification;
   }

   this.notification = function()
   {
     return this.notificationData;
   }
})
.service("user",function User()
{
  this.userData = { uun: ""};

  this.setUser = function(user)
  {
    this.userData = user;
  }

  this.user = function()
  {
     return this.userData;
  }

})
.controller('navigation',function($rootScope, $scope, $http, $location, $route) {

    $scope.route = $route;

	$scope.tab = function(route) {
		return $route.current && route === $route.current.controller;
	};

	$rootScope.getUser = function()
	{
	    $http.get('user').success(function(data) {
    		if (data.name) {

    			$rootScope.authenticated = true;
    		} else {

    			$rootScope.authenticated = false;
    		}
    	}).error(function() {
    		$rootScope.authenticated = false;
    	});

	}

    if ($rootScope.authenticated ==null)
    {
        $rootScope.getUser();
    }


	$scope.credentials = {};

	$scope.logout = function() {
		$http.post('logout', {}).success(function() {
		    alert("logout called");
			$rootScope.authenticated = false;
			$location.path("/");
		}).error(function(data) {
			console.log("Logout failed")
			$rootScope.authenticated = false;
		});
	}

})

.controller('listEmergencyNotificationController', function($rootScope,$scope, $http,$route,notification,message,$location) {

    $scope.successMessage =
    $scope.errorMessage = message.errorMessage();
    $scope.$on('successMessageUpdated', function() {
        $scope.successMessage = message.successMessage();

      });
    $scope.$on('errorMessageUpdated', function() {
            $scope.errorMessage = message.errorMessage();

          });
    $scope.route = $route;
	$http.get('notification/publisher/notify-ui').success(function(data) {
		$scope.notificationList = data;
	});

	$scope.createNotification = function()
	{
	  newNotification = { publisherId: "notify-ui", topic: "Emergency", startDate: new Date(), lastUpdated: new Date()};
	  message.setSuccessMessage("");
      message.setErrorMessage("");
	  notification.setNotification(newNotification);
	  $location.path("/edit-emergency-notification");
	}

	$scope.editNotification = function(notificationToEdit)
	{
	   message.setSuccessMessage("");
       message.setErrorMessage("");
	   notification.setNotification(notificationToEdit);
	   $location.path("/edit-emergency-notification");
	};

	$scope.deleteNotification = function(notification)
    {

        $http.delete("notification/"+notification.notificationId,notification)
         .then(function successCallback(response)
                        {
                            message.setSuccessMessage("Notification Deleted");
                            $http.get('notification/publisher/notify-ui').success(function(data) {
                            		$scope.notificationList = data;
                            	});
                        },
                        function errorCallback(response)
                        {
                            message.setErrorMessage("Error deleting notification:"+response.status+response.statusText);
                        });

    }

})

.controller('editEmergencyNotificationController', function($rootScope,$scope, $http,$route,$location,message,notification) {

    $scope.route = $route;

    // Set up default notification
    $scope.notification = notification.notification();

    $scope.editorOptions = { };

    $scope.today = function() {
        $scope.dt = new Date();
    };
  $scope.today();

  $scope.clear = function () {
    $scope.dt = null;
  };

  $scope.toggleMin = function() {
    $scope.minDate = $scope.minDate ? null : $scope.today();
  };
  $scope.toggleMin();

  var sixMonthsFromNow = $scope.dt.setMonth($scope.dt.getMonth()+6);
  var dd = sixMonthsFromNow.getDay;
  var mm = sixMonthsFromNow.getMonth;
  var yyyy = sixMonthsFromNow.getFullYear;
  $scope.maxDate = new Date(dd,mm,yyyy);

  $scope.openStartDate = function($event) {
    $scope.startDateStatus.opened = true;
  };

  $scope.openEndDate = function($event) {
      $scope.endDateStatus.opened = true;
    };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };

  $scope.format = 'dd-MMMM-yyyy';

  $scope.startDateStatus = {
    opened: false
  };

  $scope.endDateStatus = {
    opened: false
  };

  $scope.reset = function() {

    $scope.notification = { publisherId: "notify-ui", topic: "Emergency", startDate: new Date(), lastUpdated: new Date()};
  };

  $scope.checkDates = function(notification){

          var curDate = new Date();
          curDate.setHours(0,0,0,0);

          if(new Date(notification.startDate) > new Date(notification.endDate))
          {
            $scope.badEndDate = true;
          }
          else
          {
            $scope.badEndDate = null;
          }
          if(new Date(notification.startDate) < curDate)
          {
             $scope.badStartDate = true;
          }
          else
          {
            $scope.badStartDate = null;
          }

  };

  $scope.update = function(notification)
  {
     if (notification.notificationId == null)
     {
        console.log("Insert called");
        //TODO add validation on variables being set
        $http.post("notification/",notification).then(function successCallback(response) {
                                                 message.setSuccessMessage("Notification Saved");
                                                 $location.path("/");
                                               }, function errorCallback(response) {
                                                 message.setErrorMessage("Error saving notification:"+response.status+response.statusText);
                                               });
     }
     else
     {
        $http.put("notification/"+notification.notificationId,notification)
                .then(function successCallback(response)
                {
                    message.setSuccessMessage("Notification Saved");
                     $location.path("/");
                },
                function errorCallback(response)
                {
                    message.setErrorMessage("Error saving notification:"+response.status+response.statusText);
                });
     }




  };

})
.controller('listUiUsersController', function($rootScope,$scope, $http,$route,$location,message,user) {

     $scope.successMessage = message.successMessage();
     $scope.errorMessage = message.errorMessage();
     $scope.$on('successMessageUpdated', function() {
         $scope.successMessage = message.successMessage();

       });
     $scope.$on('errorMessageUpdated', function() {
             $scope.errorMessage = message.errorMessage();

           });
     $scope.route = $route;

    $http.get('/ui-users').success(function(data) {
		$scope.userList = data;
	});

	$scope.createUser = function()
    	{
    	  newUser = { uun: "" };
    	  message.setSuccessMessage("");
          message.setErrorMessage("");
    	  user.setUser(newUser);
    	  $location.path("/edit-user");
    	}

    	$scope.editUser = function(userToEdit)
    	{
    	   message.setSuccessMessage("");
           message.setErrorMessage("");
    	   user.setUser(userToEdit);
    	   $location.path("/edit-user");
    	};

    	$scope.deleteUser = function(user)
        {

            $http.delete("ui-user/"+user.uun,user)
             .then(function successCallback(response)
                            {
                                message.setSuccessMessage("User Deleted");
                                $http.get('ui-users').success(function(data) {
                                		$scope.userList = data;
                                	});
                            },
                            function errorCallback(response)
                            {
                                message.setErrorMessage("Error deleting user:"+response.status+response.statusText);
                            });

        }

})
.controller('editUiUsersController', function($rootScope,$scope, $http,$route,$location,message,user) {

    $scope.successMessage = message.successMessage();
    $scope.errorMessage = message.errorMessage();
    $scope.$on('successMessageUpdated', function() {
        $scope.successMessage = message.successMessage();

      });
    $scope.$on('errorMessageUpdated', function() {
            $scope.errorMessage = message.errorMessage();

          });
    $scope.route = $route;
    $scope.user = user.user();

    $http.get('/ui-roles').success(function(data) {
    		$scope.roles = data;
    	});

    $scope.update = function(user)
    {
       $http.put("ui-user/"+user.uun,user)
                       .then(function successCallback(response)
                       {
                           message.setSuccessMessage("User Saved");
                           $location.path("/user-administration");
                       },
                       function errorCallback(response)
                       {
                           message.setErrorMessage("Error saving user:"+response.status+response.statusText);
                       });



    }

      $scope.checkAll = function() {
        $scope.user.uiRoles = angular.copy($scope.roles);
      };
      $scope.uncheckAll = function() {
        $scope.user.uiRoles = [];
      };

});