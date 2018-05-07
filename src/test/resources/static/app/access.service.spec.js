"use strict";

describe("access controls", function() {
    beforeEach(module('access'));

    describe("authorisation service", function() {

        var Auth, httpBackend;
        beforeEach(inject(function(_Auth_, _$httpBackend_){
            Auth = _Auth_;
            httpBackend = _$httpBackend_;
        }));

        it('returns user data from back end', function() {
            httpBackend.when('GET','/user-role').respond(200, {'uun' : 'user1', 'orgGroupDN': 'ou=ABC', 'roles': []});

            Auth.authenticate().then(function(response) {
                expect(response.data).toEqual({'uun' : 'user1', 'orgGroupDN': 'ou=ABC', 'roles': []});
            });

            httpBackend.flush();
        });
    });

    describe("role checking in user profile", function() {

        var userProfile, httpBackend;
        beforeEach(inject(function(_UserProfile_, _$httpBackend_) {
            userProfile = _UserProfile_;
            httpBackend = _$httpBackend_;
        }));

        it("returns user profile", function() {
            httpBackend.when('GET','/user-role').respond(200,
                {
                    'uun' : 'user1',
                    'orgGroupDN': 'ou=ABC',
                    'uiRoles': [
                        {
                        'roleCode': 'SYSSUPPORT',
                        'roleDescription': 'System support'
                        },
                        {
                            'roleCode': 'GROUP',
                            'roleDescription': 'Group role'
                        }
                    ]
                });


            userProfile.then(function(profile) {
                expect(profile._isAuthenticated).toBe(true);
                expect(profile._hasRole('SYSSUPPORT')).toBe(true);
                expect(profile._hasRole('SUPERADMIN')).toBe(false);
            });

            httpBackend.flush();
        });
    });
});