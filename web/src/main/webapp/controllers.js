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
	    $scope.spritesMap = [];
	    console.log("camera target:" + $scope.cameraTarget);
	    
	    var textureObjects = $scope.db.select("//movingObjects/*[/textureFileName!=null]");
	    $scope.loadTextures(textureObjects, $scope.texturesLoaded);
   });
   
   $scope.loadTextures = function(textureObjects, callback) {
		imagesCount = textureObjects.length + 1;
		console.log(imagesCount +' about to load');
		for(var i=0; i<textureObjects.length; i++) {
			var to = textureObjects[i].value;
			var name = to.name;
			var source = "." + to.textureFileName;
			console.log('Texture for ' + name + ' sources '+ source);
			loadTexture(name, source, $scope.texturesMap, callback);
		}
		
		loadTexture('P', 'icons/Letter-P-icon.png', $scope.texturesMap, callback);
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
    		
    		var geometrySmall = new THREE.SphereGeometry( 1, 16, 16 );
    		var colorSmall = new THREE.Color(celestialBody.trajectory.color[0],celestialBody.trajectory.color[1],celestialBody.trajectory.color[2]);
    		console.log('color= ' + colorSmall.getHexString());
    		var materialSmall = new THREE.MeshBasicMaterial( { color: colorSmall } );
    		var sprite = new THREE.Mesh( geometrySmall, materialSmall );
    		sprite.position.copy(sphere.position);
    		$scope.spritesMap.push(sprite);
    		$scope.scene.add( sprite );
    		
    		if (celestialBody.keplerianElements!=null) {
    			// the object has a trajectory
    			var ke = celestialBody.keplerianElements;
    			var a = ke.keplerianOrbit.semimajorAxis;
    			console.log('a = ' + a);
    			var ec = ke.keplerianOrbit.eccentricity;
    			var b = a * Math.sqrt(1 - ec*ec);
    			var e = a * ec;
    			var centerObject = $scope.findByName(ke.keplerianOrbit.centralObject);
    			
    			var curve = new THREE.EllipseCurve(
    					-e,  0,            // ax, aY
    					a, b,           // xRadius, yRadius
    					0,  2 * Math.PI,  // aStartAngle, aEndAngle
    					false             // aClockwise
    				);
    			var path = new THREE.Path( curve.getPoints( 3600 ) );
    			var geometry5 = path.createPointsGeometry( 3600 );
    			
    			// ZXZ is not fuly supported
				var m1 = new THREE.Matrix4().makeRotationZ(ke.keplerianOrbit.ascendingNode);
				var m2 = new THREE.Matrix4().makeRotationX(ke.keplerianOrbit.inclination);
				var m3 = new THREE.Matrix4().makeRotationZ(ke.keplerianOrbit.argumentOfPeriapsis);
				var m = m1.multiply(m2).multiply(m3);
    			geometry5.applyMatrix(m);
    			
    			var material5 = new THREE.LineBasicMaterial( { color: colorSmall } );
    			var ellipse = new THREE.Line( geometry5, material5 );
    			ellipse.position.add(centerObject.cartesianState.position);
    			
    			$scope.scene.add(ellipse);
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
  		var v = new THREE.Vector3();
  		for(var i=0; i<$scope.spritesMap.length; i++) {
  			var sprite = $scope.spritesMap[i];
  			sprite.scale.x = sprite.scale.y = sprite.scale.z = v.subVectors( sprite.position, $scope.camera.position ).length() / 200;
  		}
  		requestAnimationFrame($scope.animate);
  		$scope.controls.update();
  	}
  	
}]);

spaceSimulatorControllers.controller('ProjectController', ['$scope', function($scope) {
	
}]);

spaceSimulatorControllers.controller('HelpController', ['$scope', function($scope) {
	
}]);
