var spaceSimulatorApp = angular.module('spaceSimulatorApp', ['ngRoute', 'spaceSimulatorControllers']);

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
	  when('/main', {templateUrl: 'partials/main.html', controller: 'MainController'}).
	  otherwise({redirectTo: '/main'});
}]);
