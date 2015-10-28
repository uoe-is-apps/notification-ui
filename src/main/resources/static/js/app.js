angular.module('hello', [ 'ngRoute' , 'ngCkeditor' , 'ui.bootstrap']).config(function($routeProvider, $httpProvider) {

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
	otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

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

.controller('listEmergencyNotificationController', function($rootScope,$scope, $http,$route,notification,$location) {
    $scope.route = $route;
    //$rootScope.getUser();
	$http.get('notification/publisher/notify-ui').success(function(data) {
		$scope.notificationList = data;
	});

	$scope.editNotification = function(notificationToEdit)
	{
	   notification.setNotification(notificationToEdit);
	   $location.path("/edit-emergency-notification");
	};

})

.controller('editEmergencyNotificationController', function($rootScope,$scope, $http,$route,notification) {

    $scope.route = $route;

    //$rootScope.getUser();

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

    $scope.notification = { topic: "Emergency", startDate: new Date() };
  }

  $scope.update = function(notification)
  {
    //TODO add validation on variables being set
     $http.post("notification/",notification).then(function successCallback(response) {
                                                 $scope.successMessage="Notification Saved";
                                               }, function errorCallback(response) {
                                                 $scope.errorMessage="Error saving notification:"+response.status+response.statusText;
                                               });
  }

});