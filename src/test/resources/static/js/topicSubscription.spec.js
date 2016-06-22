describe("Edit topic subscription controller test suite", function() {
	
	var $httpBackend, $rootScope, createController, lastUpdated, $scope, $location;
	
	beforeEach(module('notify-ui-app'));
	
	beforeEach(inject(function($injector){
		
		$httpBackend = $injector.get('$httpBackend');
		$rootScope = $injector.get('$rootScope');
		$location = $injector.get('$location');
		var $controller = $injector.get('$controller');
        $scope = $rootScope.$new();
        var topicSubscription = $injector.get("topicSubscription");
        
        createController = function() {
        	return $controller('editTopicSubscriptionController', {
        		$scope : $scope, 
        		topicSubscription: topicSubscription,
        		$location: $location
        		}); 
        };
        
        lastUpdated = new Date();
 
        $httpBackend.expectGET('listEmergencyNotification.html').respond(200,{});
    }));
	
	afterEach(function() {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });
	
	 it('should exist', function() {
		 
		 $httpBackend.when('GET','/subscribers').respond({});
		 
		 var controller = createController();
		 
		 $httpBackend.flush();
		 
		 expect(controller).toBeTruthy();
	 });
	
	it("should get data from backend", function() {
		
		var subscriberObject = [{"subscriberId":"myed","description":"MyEd Notification Subscriber","type":"Pull","status":"A","lastUpdated":"2015-11-10T11:18:04+0000"}];
		
		$httpBackend.when('GET','/subscribers')
		.respond(200, subscriberObject);
			
		$httpBackend.when('POST', '/topic-subscriptions')
		 .respond(200);
		
		$httpBackend.expectGET('listPublisherSubscriberDetails.html').respond(200,{});
		
		spyOn($rootScope, '$broadcast').and.callThrough();
		var controller = createController();
		
		$scope.create({});
		
		$httpBackend.flush();

		expect($scope.subscriberList.length).toBe(1);
		expect($scope.subscriberList).toEqual(subscriberObject);
		
		expect($scope.successMessage).toBe("Topic subscription saved");
		expect($location.path()).toEqual('/publisher-subscriber');
		
	});
	
	it("should reset topic subscription object", function() {
		
		$httpBackend.when('GET','/subscribers').respond({});
		
		var controller = createController();
		
		$httpBackend.flush();
		
		var newTopicSubscription = $scope.newTopicSubscription;
		$scope.reset();
		
		expect($scope.newTopicSubscription).not.toEqual(newTopicSubscription);
		
	});
});