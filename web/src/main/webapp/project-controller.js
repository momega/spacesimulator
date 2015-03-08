'use strict';

spaceSimulatorApp.controller('ProjectController', ['$scope', '$upload', '$routeParams', 'Builder', 'Project', 'Model', function($scope, $upload, $routeParams, Builder, Project, Model) {

	$scope.projects = Project.query();
	
	$scope.builders = Builder.query();
	
	$scope.buildProject = function(builder) {
		Builder.build({id: builder.id}, function() {
			$scope.refreshProjects();
		});
	}
	
	$scope.closeProject = function(project) {
		Project.close({id: project.id}, function () {
			$scope.refreshProjects();
		})
	}
	
	$scope.removeBuilder = function(builder) {
		Builder.remove({id: builder.id}, function () {
			$scope.refreshBuilders();
		})
	}
	
	$scope.resumeProject = function(project) {
		Project.resume({id: project.id}, function () {
			$scope.refreshProjects();
		})
	}
	
	$scope.stopProject = function(project) {
		Project.stop({id: project.id}, function () {
			$scope.refreshProjects();
		})
	}
	
	$scope.downloadModel = function(project) {
		Model.download({id: project.id}, function () {
			$scope.refreshProjects();
		})
	}
	
	$scope.refreshProjects = function() {
		$scope.projects = Project.query();
	}
	
	$scope.refreshBuilders = function() {
		$scope.builders = Builder.query();
	}
	
	$scope.uploadModel = function() {
		console.log('upload file = ' + $scope.uploadedFile);
        if ($scope.uploadedFile) {
            $upload.upload({
                url: 'builder/upload',
                file: $scope.uploadedFile
            }).progress(function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                console.log('progress: ' + progressPercentage + '%');
            }).success(function (data, status, headers, config) {
                console.log('file ' + config.file.name + 'uploaded');
                $scope.refreshBuilders();
            });
        }
    };
	
	$scope.$on('$viewContentLoaded', function(){
		$('.filestyle').each(function () {
	        console.log("found button");
	        var $this = $(this), options = {

				'input' : $this.attr('data-input') === 'false' ? false : true,
				'icon' : $this.attr('data-icon') === 'false' ? false : true,
				'buttonBefore' : $this.attr('data-buttonBefore') === 'true' ? true : false,
				'disabled' : $this.attr('data-disabled') === 'true' ? true : false,
				'size' : $this.attr('data-size'),
				'buttonText' : $this.attr('data-buttonText'),
				'buttonName' : $this.attr('data-buttonName'),
				'iconName' : $this.attr('data-iconName'),
				'badge' : $this.attr('data-badge') === 'false' ? false : true
			};

	        $this.filestyle(options);
	    });
	});
	
	if (loopId != null) {
		clearTimeout(loopId);
	}
	
}]);

spaceSimulatorApp.controller('HelpController', ['$scope', function($scope) {
	
	if (loopId != null) {
		clearTimeout(loopId);
	}
	
}]);