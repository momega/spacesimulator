'use strict';

var AU = 149597870700.0;

/* Controllers */
var spaceSimulatorControllers = angular.module('spaceSimulatorControllers', []);

spaceSimulatorControllers.controller('SimulationController', ['$scope',  '$http', function($scope, $http) {
	
   $http.get('data/1.json').success(function(data) {
	    $scope.model = data;
	    $scope.db = SpahQL.db(data);
	    $scope.time = data.time.value;
	    $scope.cameraTarget = data.camera.targetObject;
	    $scope.texturesMap = {};
	    console.log("camera target:" + $scope.cameraTarget);
	    
	    var textureObjects = $scope.db.select("//movingObjects/*[/textureFileName!=null]");
	    $scope.loadTextures(textureObjects, $scope.texturesLoaded);
   });
   
   $scope.loadTextures = function(textureObjects, callback) {
		imagesCount = textureObjects.length;
		console.log(imagesCount +' about to load');
		for(var i=0; i<imagesCount; i++) {
			var to = textureObjects[i].value;
			var name = to.name;
			var source = "." + to.textureFileName;
			console.log('Texture for ' + name + ' sources '+ source);
			loadTexture(name, source, $scope.texturesMap, callback);
		}
	}
   
    $scope.texturesLoaded = function() {
	   console.log('All texture loaded');
	   $scope.createScene();
    }
    
    $scope.createScene = function() {
    	var celestialBodies = $scope.db.select("//movingObjects/*[/radius>0]");
    	for(var i=0; i<celestialBodies.length; i++) {
    		var celestialBody = celestialBodies[i].value;
    		var position = celestialBody.cartesianState.position;
    		var geometry = new THREE.SphereGeometry( celestialBody.radius, 32, 32 );
   		
    		// change orientation, north pole is in Z direction
    		var t = new THREE.Matrix4();
    		t.makeRotationX(Math.PI/2);
    		geometry.applyMatrix(t);
    		
    		var texture = $scope.texturesMap[celestialBody.name];
    		var material = new THREE.MeshBasicMaterial( { map: texture } );
    		var sphere = new THREE.Mesh( geometry, material );
    		sphere.position.x = position.x;
    		sphere.position.y = position.y;
    		sphere.position.z = position.z;
    		
    		console.log('position=' + sphere.position.toArray());
    		$scope.scene.add( sphere );
    		
    		if (celestialBody.keplerianElements!=null) {
    			// the object has a trajectory
//    			var ke = celestialBody.keplerianElements;
//    			var a = ke.
//    			var curve = new THREE.EllipseCurve(
//    					0,  0,            // ax, aY
//    					3, 5,           // xRadius, yRadius
//    					0,  2 * Math.PI,  // aStartAngle, aEndAngle
//    					false             // aClockwise
//    				);
//    			var path = new THREE.Path( curve.getPoints( 50 ) );
//    			var geometry5 = path.createPointsGeometry( 50 );
//    			var material5 = new THREE.LineBasicMaterial( { color : 0xff5500 } );
//    			// Create the final Object3d to add to the scene
//    			var ellipse = new THREE.Line( geometry5, material5 );
//    			ellipse.rotation.x = -0.5; 
//    			scene.add(ellipse);
    			
    		}
    	}
    	
    	var axisHelper = new THREE.AxisHelper( AU );
    	$scope.scene.add( axisHelper );
    	
    	console.log('Scene created');
    	
    	$scope.selectCameraTarget($scope.findByName($scope.cameraTarget));
    	
    	$scope.animate();
    }
    
    $scope.findByName = function(name) {
    	var movingObject = $scope.db.select("//movingObjects/*[/name=='" + name + "']").value();
    	console.log('mo:' + movingObject.name);
    	return movingObject;
    }
    
    $scope.updateCameraPosition = function(newCameraPosition, newRadius) {
    	var cameraTarget = new THREE.Vector3(0,0,0);
    	cameraTarget.copy(newCameraPosition);
    	
    	var cameraDiff = new THREE.Vector3(newRadius, 0, 0);
    	
    	var cameraPosition = new THREE.Vector3(0,0,0);
    	cameraPosition.copy(newCameraPosition);
    	cameraPosition.add(cameraDiff);
    	
    	$scope.camera.position.copy(cameraPosition);
    	$scope.controls.target = cameraTarget;
    }
  
    $scope.selectCameraTarget = function(movingObject) {
	  console.log('body = ' + movingObject.name);
	  $scope.cameraTarget = movingObject.name;

	  var targetBody = $scope.findByName($scope.cameraTarget);
	  console.log('targetBody='+targetBody.name);
	  $scope.updateCameraPosition(targetBody.cartesianState.position, targetBody.radius * 10);
	  $scope.render();
    }
  
    $scope.$on('$viewContentLoaded', function(){
		console.log('view loaded');
		$scope.initContainer();
    });
  
  $scope.initContainer = function() {
		var container = document.getElementById( 'container' );
		document.getElementById( 'container' ).innerHTML = "";
		var canvasWidth = container.offsetWidth;
		var canvasHeight = 400;
		
		$scope.scene = new THREE.Scene();
		$scope.camera = new THREE.PerspectiveCamera( 45, canvasWidth/canvasHeight, 1000000, AU * 10 );
		$scope.camera.up.copy(new THREE.Vector3(0,0,1));	
		
		$scope.renderer = new THREE.WebGLRenderer();
		$scope.renderer.setSize( canvasWidth, canvasHeight );
		container.appendChild( $scope.renderer.domElement );
		
		$scope.controls = new THREE.OrbitControls( $scope.camera, container );
		$scope.controls.noPan = true;
		$scope.controls.addEventListener( 'change', $scope.render );
	}  
  
  	$scope.render = function() {
		//console.log("Render called");
		$scope.renderer.render( $scope.scene, $scope.camera );
	}
  	
  	$scope.animate = function() {
  		requestAnimationFrame($scope.animate);
  		$scope.controls.update();
  	}
  	
}]);

spaceSimulatorControllers.controller('ProjectController', ['$scope', function($scope) {
	
}]);

spaceSimulatorControllers.controller('HelpController', ['$scope', function($scope) {
	
}]);
