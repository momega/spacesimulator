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
	});
}]);