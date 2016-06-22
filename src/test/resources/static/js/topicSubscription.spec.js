describe("Edit topic subscription controller test suite", function() {
	
	var $httpBackend, $rootScope, createController, lastUpdated, $scope;
	
	beforeEach(module('notify-ui-app'));
	
	beforeEach(inject(function($injector){
		
		$httpBackend = $injector.get('$httpBackend');
		$rootScope = $injector.get('$rootScope');
		var $controller = $injector.get('$controller');
        $scope = $rootScope.$new();
        
        createController = function(name) {
        	return $controller(name, {'$scope' : $scope}); 
        };
        
        lastUpdated = new Date();
 
        $httpBackend.expectGET('listEmergencyNotification.html').respond({});
    }));
	
	afterEach(function() {
        $httpBackend.verifyNoOutstandingExpectation();
        $httpBackend.verifyNoOutstandingRequest();
    });
	
	it("should get data from backend", inject(function($route, $location, message, topicSubscription) {
		
		var subscriberObject = [{"subscriberId":"myed","description":"MyEd Notification Subscriber","type":"Pull","status":"A","lastUpdated":"2015-11-10T11:18:04+0000"}];
		
		$httpBackend.when('GET','/subscribers')
		.respond(200, subscriberObject);
			
		$httpBackend.when('POST', '/topic-subscriptions')
		 .respond(200, true);
		
		var controller = createController('editTopicSubscriptionController');
		spyOn($rootScope, '$broadcast').and.callThrough();

		$scope.create({});
		
		$httpBackend.expectGET('listPublisherSubscriberDetails.html').respond({});
		
		$httpBackend.flush();

		expect($scope.subscriberList.length).toBe(1);
		expect($scope.subscriberList).toEqual(subscriberObject);
		
		expect($scope.successMessage).toBe("Topic subscription saved");
		
	}));
});