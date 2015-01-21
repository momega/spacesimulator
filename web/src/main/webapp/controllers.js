'use strict';

/* Controllers */

var spaceSimulatorApp = angular.module('spaceSimulatorApp', []);

spaceSimulatorApp.controller('CameraCtrl', function($scope) {
	
  $scope.bodies = [
	                 {'name': 'earth',
	                  'value': 'Earth'},
	                 {'name': 'mars',
	                  'value': 'Mars'}
	               ];
  
  $scope.positions = [
					{'name': 'earth',
					    'position': new THREE.Vector3(0.0, 0, 0)},
					   {'name': 'mars',
					    'position': new THREE.Vector3(-2.0, 1.0, 0)}
                     ];
  
  $scope.selectedBody = 'earth';
  $scope.selectedPosition = new THREE.Vector3(-2.0, 1.0, 0);
  $scope.selectedPositionX = $scope.selectedPosition.toArray();
  
  $scope.bodyChanged = function () {
	  console.log('body = ' + $scope.selectedBody);
	  $scope.selectedPosition = $scope.positions[$scope.selectedBody];
	  $scope.selectedPositionX = $scope.selectedPosition.toArray();
  }

});