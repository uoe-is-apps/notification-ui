describe("Edit topic subscription test suite", function() {
	
	beforeEach(module('notify-ui-app'));
	var $controller, $rootScope;
	
	beforeEach(inject(function(_$controller_, _$rootScope_){
        $controller = _$controller_;
        $rootScope = _$rootScope_;
    }));
	
	it("url to edit topic subscription should be [/edit-topic-subscription]", inject(function(_$httpBackend_, $route, $location, message, topicSubscription) {
		
		var scope = $rootScope.$new();
		var $httpBackend = _$httpBackend_;
		
		
		$httpBackend.expectGET('/subscribers').respond(200, {});
		$httpBackend.flush();
		
		var controller = $controller('editTopicSubscriptionController', {$scope: scope});
		spyOn($rootScope, '$broadcast').and.callThrough();

		expect(scope.subscriberList).toBe({});
	}));
});