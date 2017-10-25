angular.module('useradmin')
    .component('userAdmin', {
        templateUrl: 'app/user-admin/useradmin.tpl.html',
        controller: ['user', 'UserService', '$location', 'messenger', function(_user, UserService, $location, messenger) {
            var self = this;
            self.loading = true; // progress bar

            UserService.all().then(function(response) {

                self.users = response.data;
            })
                .catch(function(response) {
                    // log error
                    console.error(response.status, response.data);
                })
                .finally(function() {
                    self.loading = false;
                });

            // controller API
            self.editUser = function(user) {
                _user.setUser(user);
                $location.path('/edit-user');
            };

            self.createUser = function() {
                _user.reset();
                $location.path('/edit-user');
            };

            self.deleteUser = function(user) {
                UserService.delete(user).then(function(response) {
                    messenger.setSuccess('User deleted');
                })
                    .catch(function(response) {
                        // log error
                        console.error(response.status, response.data);
                    });
            };
        }]
    })
    .component('editUser', {
        templateUrl: 'app/user-admin/edit-user.tpl.html',
        controller: ['user', 'UserService', 'RolesService', '$location','messenger', '$scope',
                      function(_user, UserService, RolesService, $location, messenger, $scope) {

            var self = this;
            self.user = _user.getUser();

            RolesService.all().then(function(response) {
                self.roles = response.data;

            })
                .catch(function(response) {
                    // log error
                    console.error(response.status, response.data);
                });

            // controller API
            self.saveUser = function(user) {

                UserService.save(user).then(function(response) {
                    messenger.setSuccess('User saved');

                    _user.reset();
                    $location.path('/user-administration');
                })
                    .catch(function(response) {
                        // log error
                        console.error(response.status, response.data);
                    });
            };

            self.cancel = function() {
                _user.reset();
                $location.path('/user-administration');
            };

            // jstree settings
            self.toggle = function() {
                self.showTree = !self.showTree;
            };

            $('#groupTree').jstree({
                'core' : {
                    'data' : {
                        "url": function (node) {
                            return "findGroups";
                            },
                        data: function (node) {

                            var json = {};
                            json["id"] = node.id;
                            return json;
                            },
                        "dataType" : "json"
                    }
                }
            });

            $("#groupTree").bind('select_node.jstree', function(e) {
                $scope.$apply(function() {
                    self.user.orgGroupDN = $("#groupTree").jstree("get_selected")[0];
                    // TODO add ldap group name to ui user (needs database patching, code breaking change!)
                    // self.user.orgGroupName = $('.jstree-clicked').text();
                });
            });
        }]
    });