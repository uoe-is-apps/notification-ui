angular.module('notify-ui-app', [ 'ngRoute' , 'ngCkeditor' , 'ui.bootstrap', 'checklist-model']).config(function($routeProvider, $httpProvider) {

        /*
        Role Definition from WEB010 SDS:
        EMERGENCY – Role which gives access to emergency notification creation screen
        GROUP – Role which gives access to group notification creation screen        
        USRSUPPORT – Role which gives access to user notification support
        SYSSUPPORT – Role which gives access to system support screens
        USERADMIN – Role which gives access to user administrative screens
        SUPERADMIN – Role which gives access to all screens
        */

	$routeProvider.when('/', {
		templateUrl : 'listGroupNotification.html',
		controller : 'listGroupNotificationController',
		activetab : 'group-notifications',
                resolve: { access: ["Access", function (Access) { return Access.hasRole("GROUP"); }] }   
	}).
	when('/edit-group-notification',{
		templateUrl : 'editGroupNotification.html',
		controller : 'editGroupNotificationController',
		activetab : 'group-notifications',
                resolve: { access: ["Access", function (Access) { return Access.hasRole("GROUP"); }] }   
	}).                     
                
                
                
                
                
	when('/list-emergency-notification',{
		templateUrl : 'listEmergencyNotification.html',
		controller : 'listEmergencyNotificationController',
		activetab : 'emergency-notifications',
                resolve: { access: ["Access", function (Access) { return Access.hasRole("EMERGENCY"); }] }   
	}).                                
	when('/edit-emergency-notification',{
		templateUrl : 'editEmergencyNotification.html',
		controller : 'editEmergencyNotificationController',
		activetab : 'emergency-notifications',
                resolve: { access: ["Access", function (Access) { return Access.hasRole("EMERGENCY"); }] }   
	}).
           
                
                
                
                
                
	when('/user-administration',{
    		templateUrl : 'listUiUsers.html',
    		controller : 'listUiUsersController',
    		activetab : 'user-administration',                
                resolve: { access: ["Access", function (Access) { return Access.hasRole("USERADMIN"); }]}                                
        }).                
        when('/edit-user',{
        	templateUrl : 'editUiUser.html',
        	controller : 'editUiUsersController',
        	activetab : 'user-administration',
                resolve: { access: ["Access", function (Access) { return Access.hasRole("USERADMIN"); }]} 
        }).
            
            
            
            
            
            
        when('/scheduled-tasks',{
                    templateUrl : 'listScheduledTasks.html',
                    controller : 'listScheduledTasksController',
                    activetab : 'scheduled-tasks',
                    resolve: { access: ["Access", function (Access) { return Access.hasRole("SYSSUPPORT"); }]}                   
        }).
        when('/publisher-subscriber',{
                    templateUrl : 'listPublisherSubscriberDetails.html',
                    controller : 'listPublisherSubscriberDetailsController',
                    activetab : 'publisher-subscriber',
                    resolve: { access: ["Access", function (Access) { return Access.hasRole("SYSSUPPORT"); }]}    
        }).
        when('/edit-topic-subscription',{
                    templateUrl : 'editTopicSubscription.html',
                    controller : 'editTopicSubscriptionController',
                    activetab : 'publisher-subscriber',
                    resolve: { access: ["Access", function (Access) { return Access.hasRole("SYSSUPPORT"); }]}    
        }).
                
                
                
                
                
        when('/user-notifications',{
                    templateUrl : 'listUserNotifications.html',
                    controller : 'listUserNotificationsController',
                    activetab : 'user-notifications',
                    resolve: { access: ["Access", function (Access) { return Access.hasRole("USRSUPPORT"); }]}    
        }).
           
           
           
                        
        when('/forbidden',{
                    templateUrl : 'forbidden.html',
                    controller : 'forbiddenController'
        }).          
	otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

})

.factory("Access", ["$q", "UserProfile", function ($q, UserProfile) {

  var Access = {
    OK: 200,
    UNAUTHORIZED: 401,
    FORBIDDEN: 403,

    hasRole: function (role) {
      return UserProfile.then(function (userProfile) {
        if (userProfile.$hasRole(role)) {
          return Access.OK;
        } else if (userProfile.$isAnonymous()) {
          return $q.reject(Access.UNAUTHORIZED);
        } else {
          return $q.reject(Access.FORBIDDEN);
        }
      });
    },

    hasAnyRole: function (roles) {
      return UserProfile.then(function (userProfile) {
        if (userProfile.$hasAnyRole(roles)) {
          return Access.OK;
        } else if (userProfile.$isAnonymous()) {
          return $q.reject(Access.UNAUTHORIZED);
        } else {
          return $q.reject(Access.FORBIDDEN);
        }
      });
    },

    isAnonymous: function () {
      return UserProfile.then(function (userProfile) {
        if (userProfile.$isAnonymous()) {
          return Access.OK;
        } else {
          return $q.reject(Access.FORBIDDEN);
        }
      });
    },

    isAuthenticated: function () {
      return UserProfile.then(function (userProfile) {
        if (userProfile.$isAuthenticated()) {
          return Access.OK;
        } else {
          return $q.reject(Access.UNAUTHORIZED);
        }
      });
    }

  };
  return Access;
}])

.factory("UserProfile", ["Auth", function (Auth) {

  var userProfile = {};

  var fetchUserProfile = function () {
    return Auth.getProfile().then(function (response) {
      for (var prop in userProfile) {
        if (userProfile.hasOwnProperty(prop)) {
          delete userProfile[prop];
        }
      }

      return angular.extend(userProfile, response.data, {

        $refresh: fetchUserProfile,

        $hasRole: function (role) {            
            //JSON.stringify(userProfile)
            //alert(JSON.stringify(userProfile.uiRoles));
            var arr = userProfile.uiRoles;
            for(var i = 0; i < arr.length; i++){
                if(arr[i].roleCode.indexOf(role) >= 0 || arr[i].roleCode.indexOf("SUPERADMIN") >= 0 ){
                    return true;
                }
            }            
            return false;
        },
        $hasAnyRole: function (roles) {
          return !!userProfile.roles.filter(function (role) {
            return roles.indexOf(role) >= 0;
          }).length;
        },
        $isAnonymous: function () {
          return userProfile.anonymous;
        },
        $isAuthenticated: function () {
          return !userProfile.anonymous;
        }
      });
    });
  };
  return fetchUserProfile();
}])
.service("Auth", ["$http", function ($http) {
  this.getProfile = function () {
    return $http.get("user-role");
  };
}])
.run(["$rootScope", "Access", "$location", function ($rootScope, Access, $location) {
  $rootScope.$on("$routeChangeError", function (event, current, previous, rejection) { //rejection == Access.UNAUTHORIZED    
    $location.path("/forbidden");
  });  
}])
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
  this.userData = { uun: "", orgUnitDN: "", notificationStatus: "active"};

  this.setUser = function(user)
  {
    this.userData = user;
  }

  this.user = function()
  {
     return this.userData;
  }

})
.service("topicSubscription", function TopicSubscription(){
	this.topicSubscriptionData = {subscriberId: "", topic: "", status: "A", lastUpdated: new Date()};
	
	this.setTopicSubscription = function(topicSubscription) {
		this.topicSubscriptionData = topicSubscription;
	}
	
	this.getTopicSubscription = function() {
		return this.topicSubscriptionData;
	}
	
	this.createNewTopicSubscription = function() {
		return {subscriberId: "", topic: "", status: "A", lastUpdated: new Date()};
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

 
})

.controller('listEmergencyNotificationController', function($rootScope,$scope, $http,$route,notification,message,$location) {

    $scope.successMessage = message.successMessage();
    $scope.errorMessage = message.errorMessage();
    $scope.$on('successMessageUpdated', function() {
        $scope.successMessage = message.successMessage();

      });
    $scope.$on('errorMessageUpdated', function() {
            $scope.errorMessage = message.errorMessage();

          });
    $scope.route = $route;
	$http.get('notifications/publisher/notify-ui').success(function(data) {            
                var emergencyList = [];
                for(var i = 0; i < data.length; i++){
                 var topic = data[i].topic;                
                 if(topic === 'Emergency'){
                     emergencyList.push(data[i]);   
                 }
                }
		$scope.notificationList = emergencyList;
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
         .then(function successCallback(response){
                            message.setSuccessMessage("Notification Deleted");
                            setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
                            $http.get('notifications/publisher/notify-ui').success(function(data) {
                            var emergencyList = [];
                            for(var i = 0; i < data.length; i++){
                             var topic = data[i].topic;                
                             if(topic === 'Emergency'){
                                 emergencyList.push(data[i]);   
                             }
                            }
                            $scope.notificationList = emergencyList;
                            	});
                        },
                        function errorCallback(response)
                        {
                            message.setErrorMessage("Error deleting notification:"+response.status+response.statusText);
                        });

    }

})

.controller('editEmergencyNotificationController', function($rootScope,$scope, $http,$route,$location,message,notification) {

    $('#notificationGroupTree').jstree({
	'core' : {
            'data' : {
                    "url": function (node) {
                           return "findGroups"; 
                    },				
                    data: function (node) {
                           //alert(node.id);
                           var json = {};                                 
                           json["id"] = node.id;
                           return json;
                    },
                    "dataType" : "json" 
            }
	}
    });

    $("#notificationGroupTree").bind('select_node.jstree', function(e) {        
        var $scopeNotificationGroupName = angular.element($('#notificationGroupName')).scope();
        $scopeNotificationGroupName.$apply(function () {
             $scopeNotificationGroupName.notification.notificationGroupName = $('.jstree-clicked').text();
        });    
          
        var $scopeNotificationGroup = angular.element($('#notificationGroup')).scope();
        $scopeNotificationGroup.$apply(function () {
             $scopeNotificationGroup.notification.notificationGroup = $("#notificationGroupTree").jstree("get_selected")[0];
        });  
    });

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
                    setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
                    $location.path("/list-emergency-notification");
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
                    setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
                    $location.path("/list-emergency-notification");
                },
                function errorCallback(response)
                {
                    message.setErrorMessage("Error saving notification:"+response.status+response.statusText);
                });
     }




  };

})
.controller('listGroupNotificationController', function($rootScope,$scope, $http,$route,notification,message,$location) {

    $scope.successMessage = message.successMessage();
    $scope.errorMessage = message.errorMessage();
    $scope.$on('successMessageUpdated', function() {
        $scope.successMessage = message.successMessage();

    });
    $scope.$on('errorMessageUpdated', function() {
            $scope.errorMessage = message.errorMessage();
    });
    $scope.route = $route;
	$http.get('notifications/publisher/notify-ui').success(function(data) {
                var groupList = [];
                for(var i = 0; i < data.length; i++){
                 var topic = data[i].topic;                
                 if(topic === 'Group'){
                     groupList.push(data[i]);   
                 }
                }
            $scope.notificationList = groupList;
	});

	$scope.createNotification = function()
	{
	   newNotification = { publisherId: "notify-ui", topic: "Group", startDate: new Date(), lastUpdated: new Date()};
	   message.setSuccessMessage("");
           message.setErrorMessage("");
	   notification.setNotification(newNotification);
	   $location.path("/edit-group-notification");
	}

	$scope.editNotification = function(notificationToEdit)
	{
	   message.setSuccessMessage("");
           message.setErrorMessage("");
	   notification.setNotification(notificationToEdit);
	   $location.path("/edit-group-notification");
	};
	$scope.deleteNotification = function(notification)
        {

        $http.delete("notification/"+notification.notificationId,notification)
         .then(function successCallback(response)
                        {
                            message.setSuccessMessage("Notification Deleted");
                            setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
                            $http.get('notifications/publisher/notify-ui').success(function(data) {
                            var groupList = [];
                                for(var i = 0; i < data.length; i++){
                                 var topic = data[i].topic;                
                                 if(topic === 'Group'){
                                     groupList.push(data[i]);   
                                 }
                                }
                            $scope.notificationList = groupList;
                        });
                        },
                        function errorCallback(response)
                        {
                            message.setErrorMessage("Error deleting notification:"+response.status+response.statusText);
                        });
    }

})

.controller('editGroupNotificationController', function($rootScope,$scope, $http,$route,$location,message,notification) {

    $('#notificationGroupTree').jstree({
	'core' : {
            'data' : {
                    "url": function (node) {
                           return "findGroups"; 
                    },				
                    data: function (node) {
                           //alert(node.id);
                           var json = {};                                 
                           json["id"] = node.id;
                           return json;
                    },
                    "dataType" : "json" 
            }
	}
    });

    $("#notificationGroupTree").bind('select_node.jstree', function(e) {   
        var $scopeNotificationGroupName = angular.element($('#notificationGroupName')).scope();
        $scopeNotificationGroupName.$apply(function () {
             $scopeNotificationGroupName.notification.notificationGroupName = $('.jstree-clicked').text();
        });    
          
        var $scopeNotificationGroup = angular.element($('#notificationGroup')).scope();
        $scopeNotificationGroup.$apply(function () {
             $scopeNotificationGroup.notification.notificationGroup = $("#notificationGroupTree").jstree("get_selected")[0];
        });            
    });

    $scope.route = $route;
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
      $scope.notification = { publisherId: "notify-ui", topic: "Group", startDate: new Date(), lastUpdated: new Date()};
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
          //alert(JSON.stringify(notification));
          $http.post("checkIfLdapGroupContainMember/",notification).then(function successCallback(response) {
              if(response.data.member == 'yes'){
                 $http.post("notification/",notification).then(function successCallback(response) {
                        message.setSuccessMessage("Notification Saved");
                        setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
                        $location.path("/list-group-notification");
                   }, function errorCallback(response) {
                        message.setErrorMessage("Error saving notification:"+response.status+response.statusText);
                 });
              }else{
                  message.setErrorMessage("Error saving notification: Your selected group does not contain any members");
                  $location.path("/list-group-notification");
              }  
           }, function errorCallback(response) {
                message.setErrorMessage("Error saving notification:"+response.status+response.statusText);
          });           
       }
       else
       {
          $http.post("checkIfLdapGroupContainMember/",notification).then(function successCallback(response) {
              if(response.data.member == 'yes'){
                    $http.put("notification/"+notification.notificationId,notification)
                  .then(function successCallback(response)
                  {
                        message.setSuccessMessage("Notification Saved");
                        setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
                        $location.path("/list-group-notification");
                  },
                  function errorCallback(response)
                  {
                        message.setErrorMessage("Error saving notification:"+response.status+response.statusText);
                  });
              }else{
                  message.setErrorMessage("Error saving notification: Your selected group does not contain any members");
                  $location.path("/list-group-notification");
              }  
           }, function errorCallback(response) {
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
                                setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
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
                           setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
                           $location.path("/user-administration");
                       },
                       function errorCallback(response)
                       {
                           message.setErrorMessage("Error saving user: "+response.status+response.statusText);
                       });



    }

      $scope.checkAll = function() {
        $scope.user.uiRoles = angular.copy($scope.roles);
      };
      $scope.uncheckAll = function() {
        $scope.user.uiRoles = [];
      };

})
.controller('listScheduledTasksController', function($rootScope, $scope, $http, $route, $location, message) {
	
	 $http.get('/scheduled-tasks')
	    .success(function(data) {
 		   $scope.triggerList = data;
 	    }).
 	    error(function(data) {
 	    	$scope.errorMessage = data; //must handle error better
 	    });
})
.controller('listPublisherSubscriberDetailsController', function($rootScope, $scope, $http, $route, $location, message, topicSubscription) {
	
	$scope.successMessage = message.successMessage();
    $scope.errorMessage = message.errorMessage();
    $scope.$on('successMessageUpdated', function() {
        $scope.successMessage = message.successMessage();

      });
    $scope.$on('errorMessageUpdated', function() {
            $scope.errorMessage = message.errorMessage();

          });
    $scope.route = $route;
	
	 $http.get('/publishers')
	    .success(function(data) {
 		   $scope.publisherList = data;
 	    });
	 
	 $http.get('/subscribers')
	    .success(function(data) {
		   $scope.subscriberList = data;
	    });
	 
	 $http.get('/topic-subscriptions')
	    .success(function(data) {
		   $scope.topicSubscriptionList = data;
	    });
	    
	 $scope.createTopicSubscription = function() {
		 
		 message.setSuccessMessage("");
         message.setErrorMessage("");
         
         $location.path("/edit-topic-subscription");
	 }
	 
	 $scope.deleteTopicSubscription = function(topicSubscription) {
		 
		 $http.delete("topic-subscriptions/" + topicSubscription.subscriptionId)
		      .then(function successCallback(response){
		    	  
		    	  message.setSuccessMessage("Topic subscription deleted.");
		    	  setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
		          $http.get('/topic-subscriptions').success(function(data) {
		  		       $scope.topicSubscriptionList = data;
		  	      });  
		    	  
		      }, function errorCallback(response) {
		    	  message.setErrorMessage("Error deleting topic subscription: " +response.status+response.statusText);
		      }); 		
	 }
})
.controller('editTopicSubscriptionController', function($rootScope, $scope, $http, $route, $location, message, topicSubscription) {
	
	$scope.successMessage = message.successMessage();
    $scope.errorMessage = message.errorMessage();
    $scope.$on('successMessageUpdated', function() {
        $scope.successMessage = message.successMessage();

      });
    $scope.$on('errorMessageUpdated', function() {
            $scope.errorMessage = message.errorMessage();

          });
    $scope.route = $route;
	
	$scope.newTopicSubscription = topicSubscription.createNewTopicSubscription();
	$scope.topicMaxLength = 32;
	
	$http.get('/subscribers')
       .success(function(data) {
	      $scope.subscriberList = data;
    });
	
	$scope.create = function(topicSubscription) {
		
		$http.post("/topic-subscriptions", topicSubscription)
			.then(function successCallback(response)
	        {
	            message.setSuccessMessage("Topic subscription saved");
                    setTimeout(function(){ message.setSuccessMessage(""); }, 2000);
	            $location.path("/publisher-subscriber");
	        },
	        function errorCallback(response)
	        {
	            message.setErrorMessage("Error saving topic subscription: "+response.status+response.statusText);
	        });
	}
	
	$scope.reset = function() {
		$scope.newTopicSubscription = topicSubscription.createNewTopicSubscription();
	}
	
})
.controller('listUserNotificationsController', ['$scope', '$http','user', function($scope, $http, user) {
	
	$scope.notificationUser = user.user();
	        
	$scope.getUserNotifications = function(uun, notificationStatus) {
            $http.get('/notifications/user/' + uun).success(function(data) {                  
                    var now = (new Date()).toISOString(); 
                    
                    if(notificationStatus === 'active'){                           
                        var activeList = [];
                        for(var i = 0; i < data.length; i++){
                           var endDate = data[i].endDate;                
                           if(endDate === undefined || now < endDate){
                               activeList.push(data[i]);
                           }                           
                        }                     
                        $scope.userNotificationList = activeList;
                    }else if(notificationStatus === 'expired'){ 
                        var expiredList = [];
                        for(var i = 0; i < data.length; i++){
                           var endDate = data[i].endDate;     
                           if(endDate < now){
                               expiredList.push(data[i]);
                           }                           
                        }
                        $scope.userNotificationList = expiredList;
                    }
                    
            });
	};

}])
.controller('forbiddenController', ['$scope', '$http','user', function($scope, $http, user) {

}])
;

