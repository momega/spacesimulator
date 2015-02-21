'use strict';

spaceSimulatorApp.controller('ProjectController', ['$scope', '$routeParams', 'Builder', 'Project', function($scope, $routeParams, Builder, Project) {

	$scope.projects = Project.query();
	
	$scope.builders = Builder.query();
	
	$scope.buildProject = function(builder) {
		Builder.build({builderName: builder.builderName}, function() {
			$scope.projects = Project.query();
		});
	}
	
	$scope.closeProject = function(project) {
		Project.close({id: project.id}, function () {
			$scope.projects = Project.query();
		})
	}
	
}]);

spaceSimulatorApp.controller('HelpController', ['$scope', function($scope) {
	
}]);