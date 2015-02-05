var spaceSimulatorApp = angular.module('spaceSimulatorApp', ['ngRoute', 'spaceSimulatorControllers', 'ui.bootstrap']);

spaceSimulatorApp.directive( 'goClick', function ( $location ) {
	  return function ( scope, element, attrs ) {
		    var path;

		    attrs.$observe( 'goClick', function (val) {
		      path = val;
		    });

		    element.bind( 'click', function () {
		      scope.$apply( function () {
		        $location.path( path );
		      });
		    });
	 };
});


spaceSimulatorApp.config(['$routeProvider', function($routeProvider) {
	$routeProvider.
	  when('/simulation', {templateUrl: 'partials/simulation.html', controller: 'SimulationController'}).
	  when('/project', {templateUrl: 'partials/project.html', controller: 'ProjectController'}).
	  when('/help', {templateUrl: 'partials/help.html', controller: 'HelpController'}).
	  otherwise({redirectTo: '/simulation'});
}]);
