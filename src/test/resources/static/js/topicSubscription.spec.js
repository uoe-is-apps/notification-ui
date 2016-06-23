describe("edit topic subscription controller test suite", function() {
	
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
        lastUpdated.setHours(8,0,0);
 
        $httpBackend.expectGET('listEmergencyNotification.html').respond(200);
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

		spyOn($rootScope, '$broadcast').and.callThrough();
		var controller = createController();
		
		$httpBackend.flush();

		expect($scope.subscriberList.length).toBe(1);
		expect($scope.subscriberList).toEqual(subscriberObject);
		
	});
	
	it("should send data to backend", function() {
		
		var postTopicSubscription = {'subscriberId': 'myEd', 'topic': 'Notification X', 'status': 'A', 'lastUpdated': lastUpdated};
		
		$httpBackend.when('GET','/subscribers').respond({});
		
		$httpBackend.expectGET('listPublisherSubscriberDetails.html').respond(200);
		
		$httpBackend.when('POST', '/topic-subscriptions', function(postData){
			var topicSubscriptionObj = angular.fromJson(postData);
			
			expect(topicSubscriptionObj.subscriberId).toEqual("myEd");
			expect(topicSubscriptionObj.topic).toEqual("Notification X");
			expect(topicSubscriptionObj.status).toEqual("A");
			expect(topicSubscriptionObj.lastUpdated).not.toBeNull();
			
			return true;
			}
		).respond(200, true);
		
		var controller = createController();
		
		$scope.create(postTopicSubscription);
		$httpBackend.flush();

		expect($scope.successMessage).toBe("Topic subscription saved");
		expect($location.path()).toEqual('/publisher-subscriber');
	});
	
	it("should reset topic subscription object", function() {
		
		$httpBackend.when('GET','/subscribers').respond({});
		
		var controller = createController();
		
		$httpBackend.flush();
		
		$scope.newTopicSubscription = {'subscriberId': 'myEd', 'topic': 'Notification Z', 'status': 'I', 'lastUpdated': lastUpdated};
		$scope.reset();
		
		expect($scope.newTopicSubscription.subscriberId).toEqual('');
		expect($scope.newTopicSubscription.topic).toEqual('');
		expect($scope.newTopicSubscription.status).toEqual('A');
		expect($scope.newTopicSubscription.lastUpdated).not.toEqual(lastUpdated);
		
	});
});