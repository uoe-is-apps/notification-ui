angular.module('common')
    .factory('messenger', function() {

        const message = {
            type: '',
            value: ''
        };

        return {
            types: {
                SUCCESS: 'success',
                ERROR: 'error'
            },

            setMessage: function(type, value) {
                message.type = type;
                message.value = value;
            },

            getMessage: function() {
                return {
                    type: message.type,
                    value: message.value
                };
            },

            reset: function() {
                message.type = '';
                message.value = '';
            }
        };
    });