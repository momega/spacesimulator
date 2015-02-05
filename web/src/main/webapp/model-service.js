'use strict';

spaceSimulatorApp.factory('modelService', ['$http', function($http) {
	var factory = {};
	var model;
	var db;
	var time;
	
	factory.load = function(project, snapshot, callback) {
		 $http.get('data/' + project + '.json').success(function(data) {
			 model = data;
			 db = SpahQL.db(model);
			 time = model.time.value;
			 console.log('time = ' + time);
			 callback(model);
		 });
	}
	
	factory.getModel = function() {
		return model;
	}
	
	factory.getTime = function() {
		return time;
	}
	
	return factory;
}]);