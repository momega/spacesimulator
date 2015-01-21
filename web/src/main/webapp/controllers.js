'use strict';

/* Controllers */

var spaceSimulatorApp = angular.module('spaceSimulatorApp', []);

spaceSimulatorApp.controller('CameraCtrl', function($scope) {
	
  $scope.bodies = [
	                 {'name': 'earth',
	                  'value': 'Earth', 'position': new THREE.Vector3(0.0, 0, 0)},
	                 {'name': 'mars',
	                  'value': 'Mars', 'position': new THREE.Vector3(-2.0, 1.0, 0)}
	               ];
  
  $scope.positionMap = [];
  for (var i = 0; i < $scope.bodies.length; i++) {
	  var body = $scope.bodies[i];
	  $scope.positionMap[body.name]=body.position;
  }
  
  $scope.selectedBody = 'earth';
  
  $scope.bodyChanged = function () {
	  console.log('body = ' + $scope.selectedBody);
	  console.log('position = ' +  $scope.positionMap[$scope.selectedBody].toArray());
	  
	  cameraTarget = $scope.positionMap[$scope.selectedBody];
	  updateCameraPosition(cameraTarget);
	  render();
  }

});