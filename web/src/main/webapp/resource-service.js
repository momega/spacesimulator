'use strict';

spaceSimulatorApp.factory('Builder', ['$resource',
  function($resource){
    return $resource('', {}, {
      query: {method:'GET', isArray:true, url:'builder/list'},
      build: {method:'GET', params:{id: '@id'}, url:'builder/build/:id'},
      remove: {method:'GET', params:{id: '@id'}, url:'builder/remove/:id'},
    });
}]);

spaceSimulatorApp.factory('Model', ['$resource',
    function($resource){
	    return $resource('', {}, {
			query: {method:'GET', isArray:true, url:'model/list'},
			close: {method:'GET', params:{id: '@id'}, url:'model/close/:id'},
			item: {method:'GET', params:{id: '@id'}, url:'model/item/:id'},
			resume: {method:'GET', params:{id: '@id'}, url:'model/resume/:id'},
			stop: {method:'GET', params:{id: '@id'}, url:'model/stop/:id'},
			get: {method:'GET', params:{id: '@id', time: '@time'}, url:'model/get/:id?time=:time'},
			time: {method:'GET', params:{id: '@id', time: '@time'}, url:'model/time/:id?time=:time'},
			download: {method:'GET', params:{id: '@id'}, url:'model/download/:id'},
			snapshot: {method:'GET', params:{id: '@id'}, url:'model/snapshot/:id'},
	});
}]);