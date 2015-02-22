'use strict';

spaceSimulatorApp.factory('Builder', ['$resource',
  function($resource){
    return $resource('', {}, {
      query: {method:'GET', isArray:true, url:'builder/list'},
      build: {method:'GET', params:{builderName: '@builderName'}, url:'builder/build/:builderName'},
    });
}]);

spaceSimulatorApp.factory('Project', ['$resource',
    function($resource){
	    return $resource('', {}, {
	    	query: {method:'GET', isArray:true, url:'project/list'},
	    	close: {method:'GET', params:{id: '@id'}, url:'project/close/:id'},
	    	item: {method:'GET', params:{id: '@id'}, url:'project/item/:id'},
	    	resume: {method:'GET', params:{id: '@id'}, url:'project/resume/:id'},
	    	stop: {method:'GET', params:{id: '@id'}, url:'project/stop/:id'},
	});
}]);


spaceSimulatorApp.factory('Model', ['$resource',
    function($resource){
	    return $resource('', {}, {
         get: {method:'GET', params:{id: '@id', time: '@time'}, url:'model/get/:id?time=:time'},
         time: {method:'GET', params:{id: '@id', time: '@time'}, url:'model/time/:id?time=:time'},
	});
}]);