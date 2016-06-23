describe("publisher subscriber controller test suite", function() {
	
	var $httpBackend, $rootScope, $scope, $location, createController;
	
	beforeEach(module('notify-ui-app'));
	
	beforeEach(inject(function($injector){
		
		$httpBackend = $injector.get("$httpBackend");
		$rootScope = $injector.get("$rootScope");
		$location = $injector.get("$location");
		$scope = $rootScope.$new();
		var topicSubscription = $injector.get("topicSubscription");
		
		var $controller = $injector.get("$controller");
		createController = function() {
			return $controller('listPublisherSubscriberDetailsController', {
				$scope: $scope,
				$location: $location,
				topicSubscription: topicSubscription
			});
		};
		
		$httpBackend.expectGET('listEmergencyNotification.html').respond(200);
	}));
	
	it("should exist", function() {
		
		$httpBackend.when('GET','/publishers').respond(200, {});
		$httpBackend.when('GET','/subscribers').respond(200, {});
		$httpBackend.when('GET','/topic-subscriptions').respond(200, {});
		
		var controller = createController();
		$httpBackend.flush();
		
		expect(controller).toBeTruthy();
	});
	
	it("should get data from backend", function() {
		
		var subscriberList = [{"subscriberId":"myed","description":"MyEd Notification Subscriber","type":"Pull","status":"A","lastUpdated":"2015-11-10T11:18:04+0000"}];
		
		var publisherList = [{"publisherId":"notify-ui","description":"Notification Backbone UI","key":null,"publisherType":"BOTH","status":"A","lastUpdated":"2015-10-23T15:59:58+0100"},
		                     {"publisherId":"learn","description":"Learn","key":"005AFE5E177048ABE05400144F00F4CC","publisherType":"PULL","status":"A","lastUpdated":"2015-10-26T15:13:47+0000"}];				
		
		var topicSubscriptionList = [{"subscriptionId":"242EDD10F808F6FBE053071DA8C05A7F","subscriberId":"myed","topic":"Learn Task","status":"A","lastUpdated":"2015-11-10T11:18:04+0000"},
		                             {"subscriptionId":"35B7356A67024A8DE053071DA8C0D11C","subscriberId":"myed","topic":"New subscription instruction","status":"A","lastUpdated":"2016-06-20T15:04:41+0100"}];
	
		$httpBackend.when('GET','/publishers').respond(200, publisherList);
		$httpBackend.when('GET','/subscribers').respond(200, subscriberList);
		$httpBackend.when('GET','/topic-subscriptions').respond(200, topicSubscriptionList);
		
		var controller = createController();
		$httpBackend.flush();
		
		expect($scope.publisherList.length).toBe(2);
		expect($scope.publisherList[1].publisherId).toEqual("learn");
		
		expect($scope.subscriberList.length).toBe(1);
		expect($scope.subscriberList[0].description).toEqual("MyEd Notification Subscriber");
		
		expect($scope.topicSubscriptionList.length).toBe(2);
		expect($scope.topicSubscriptionList[0].topic).toEqual("Learn Task");
	});
	
	it("should redirect to edit topic subscription view", function() {
		
		$httpBackend.when('GET','/publishers').respond(200, {});
		$httpBackend.when('GET','/subscribers').respond(200, {});
		$httpBackend.when('GET','/topic-subscriptions').respond(200, {});
		
		var controller = createController();
		$httpBackend.flush();
		
		$scope.successMessage = "Test initialised";
		$scope.createTopicSubscription();
		
		expect($scope.successMessage).toEqual("");
		expect($location.path()).toEqual("/edit-topic-subscription");
	});
	
	it("should delete topic subscription", function() {
		
		var topicSubscriptionList = [{"subscriptionId":"242EDD10F808F6FBE053071DA8C05A7F","subscriberId":"myed","topic":"Learn Task","status":"A","lastUpdated":"2015-11-10T11:18:04+0000"},
		                             {"subscriptionId":"35B7356A67024A8DE053071DA8C0D11C","subscriberId":"myed","topic":"New subscription instruction","status":"A","lastUpdated":"2016-06-20T15:04:41+0100"}];
		
		$httpBackend.when('GET','/publishers').respond(200, {});
		$httpBackend.when('GET','/subscribers').respond(200, {});
		
		$httpBackend.when('GET','/topic-subscriptions').respond(200, topicSubscriptionList);

		$httpBackend.when('DELETE', /^topic-subscriptions\/?.*/).respond(200);

		var controller = createController();

		$scope.deleteTopicSubscription(topicSubscriptionList[0]);
		
		$httpBackend.flush();
		
		//expect($scope.topicSubscriptionList.length).toBe(1);
		//expect($scope.topicSubscriptionList[0].subscriptionId).toEqual("35B7356A67024A8DE053071DA8C0D11C");
		
		expect($scope.successMessage).toEqual("Topic subscription deleted.")

	});
});