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
	    $scope.labelsMap = [];
	    $scope.orthoMap = [];
	    console.log("camera target:" + $scope.cameraTarget);
	    
	    var celestialBodies = $scope.db.select("//movingObjects/*[/textureFileName!=null]").values();
	    var spacecrafts = $scope.db.select("//movingObjects/*[/subsystems!=null]").values();
	    var textureObjects = celestialBodies.concat(spacecrafts);
	    $scope.loadTextures(textureObjects, $scope.texturesLoaded);
   });
   
   $scope.loadTextures = function(textureObjects, callback) {
		imagesCount = textureObjects.length + 3 // + 3 icons;
		console.log(imagesCount +' about to load');
		for(var i=0; i<textureObjects.length; i++) {
			var to = textureObjects[i];
			var name = to.name;
			var source = "";
			if (to.textureFileName!=null) {
				source = "." + to.textureFileName; 
			} else if (to.subsystems != null) {
				source = "./icons/Number-" + to.index + "-icon.png";
			}
			console.log('Texture for ' + name + ' sources '+ source);
			loadTexture(name, source, $scope.texturesMap, callback);
		}
		
		loadTexture('APOAPSIS', 'icons/Letter-A-icon.png', $scope.texturesMap, callback);
		loadTexture('PERIAPSIS', 'icons/Letter-P-icon.png', $scope.texturesMap, callback);
		loadTexture('CIRCLE', 'icons/circle.png', $scope.texturesMap, callback);
	}
   
    $scope.texturesLoaded = function() {
	   console.log('All texture loaded');
	   $scope.createScene();
    }
    
    $scope.createScene = function() {
    	var celestialBodies = $scope.db.select("//movingObjects/*");
    	for(var i=0; i<celestialBodies.length; i++) {
    		var celestialBody = celestialBodies[i].value;
    		var position = celestialBody.cartesianState.position;
    		var isSpacecraft = (celestialBody.subsystems != null);
    		
    		if (celestialBody.radius != null) {
	    		var geometry = new THREE.SphereGeometry( celestialBody.radius, 32, 32 );
	   		
	    		// change orientation, north pole is in Z direction
	    		var t = new THREE.Matrix4();
	    		t.makeRotationX(Math.PI/2);
	    		geometry.applyMatrix(t);
	    		
	    		var texture = $scope.texturesMap[celestialBody.name];
	    		var material = new THREE.MeshBasicMaterial( { map: texture } );
	    		var sphere = new THREE.Mesh( geometry, material );
	    		sphere.position.copy(position);
	    		$scope.scene.add( sphere );
	    		
	    		var circleTexture = $scope.texturesMap['CIRCLE'];
	    		var spriteMaterial = new THREE.SpriteMaterial({map: circleTexture, useScreenCoordinates: false});
        		var sprite = new THREE.Sprite( spriteMaterial );
        		sprite.position.copy(position);
        		$scope.spritesMap.push(sprite);
        		$scope.scene.add( sprite );
        		
        		var planetLabel = makeTextSprite(celestialBody.name, {} );
        		planetLabel.position.copy(position);
        		$scope.labelsMap.push(planetLabel);
        		$scope.scene.add( planetLabel );
    		}
    		
    		if (isSpacecraft) {
    			var spacecraft = celestialBody;
    			$scope.createTexturePoint(spacecraft);
    		}
    		
    		if (celestialBody.keplerianElements!=null) {
    			// the object has a trajectory
    			var ke = celestialBody.keplerianElements;
    			var a = ke.keplerianOrbit.semimajorAxis;
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
    			
    			// ZXZ is not fully supported
				var m1 = new THREE.Matrix4().makeRotationZ(ke.keplerianOrbit.ascendingNode);
				var m2 = new THREE.Matrix4().makeRotationX(ke.keplerianOrbit.inclination);
				var m3 = new THREE.Matrix4().makeRotationZ(ke.keplerianOrbit.argumentOfPeriapsis);
				var m = m1.multiply(m2).multiply(m3);
    			geometry5.applyMatrix(m);
    			
    			var theColor;
    			if (isSpacecraft) {
    				theColor = 0xFFFF00;
    			} else {
    				var theColor = arrayToColor(celestialBody.trajectory.color);
    				theColor = darken(theColor, 0.8);
    			}
    			var material5 = new THREE.LineBasicMaterial( { color: theColor } );
    			var ellipse = new THREE.Line( geometry5, material5 );
    			ellipse.position.add(centerObject.cartesianState.position);
    			$scope.scene.add(ellipse);
    			
    		}
    	}
    	
    	var ptexture = $scope.texturesMap['PERIAPSIS'];
    	var material2 = new THREE.SpriteMaterial( { map: ptexture } );
    	var spriteTL = new THREE.Sprite( material2 );
		spriteTL.scale.set( 16, 16, 1 );
		spriteTL.position.set(300,50,1);
		$scope.sceneOrtho.add( spriteTL );
    	
    	console.log('Scene created');
    	
    	$scope.selectCameraTarget($scope.findByName($scope.cameraTarget));
    	
    	$scope.animate();
    }
    
    $scope.createTexturePoint = function(obj) {
    	var texture = $scope.texturesMap[obj.name];
    	var material = new THREE.SpriteMaterial( { map: texture } );
    	var spriteTL = new THREE.Sprite( material );
		spriteTL.scale.set( 16, 16, 1 );
		var p = $scope.getOrthoPosition(obj);
		spriteTL.position.copy(p);
		spriteTL.body = obj;
		$scope.sceneOrtho.add( spriteTL );
		$scope.orthoMap.push(spriteTL);
    } 
    
    $scope.getOrthoPosition = function(obj) {
    	var p = new THREE.Vector3();
		p.copy(obj.cartesianState.position);
		p = p.project($scope.camera);
		console.log('position = ' + p.toArray());
		
		var result = new THREE.Vector3();
		result.x = p.x;
		result.y = p.y;
		result.z = 1;
		
		console.log('canvas = ' + result.toArray());
		
		result = result.unproject($scope.cameraOrtho);
		
		console.log('ortho = ' + result.toArray());
		return result;
    }
    
    $scope.updateTexturePoint = function(sprite) {
    	var obj = sprite.body;
    	var position = $scope.getOrthoPosition(obj);
    	sprite.position.copy(position);
    }
    
    $scope.findByName = function(name) {
    	var movingObject = $scope.db.select("//movingObjects/*[/name=='" + name + "']").value();
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
		$scope.renderer.autoClear = false;
		container.appendChild( $scope.renderer.domElement );
		
		$scope.controls = new THREE.OrbitControls( $scope.camera, container );
		$scope.controls.noPan = true;
		$scope.controls.addEventListener( 'change', $scope.render );
		
		$scope.sceneOrtho = new THREE.Scene();
		$scope.cameraOrtho = new THREE.OrthographicCamera( - canvasWidth / 2, canvasWidth / 2, canvasHeight / 2, - canvasHeight / 2, 0, 10 );
		$scope.cameraOrtho.position.z = 10;
		
		$scope.canvasWidth = canvasWidth;
		$scope.canvasHeight = canvasHeight;
	}  
  
  	$scope.render = function() {
  		$scope.renderer.clear();
		$scope.renderer.render( $scope.scene, $scope.camera );
		$scope.renderer.clearDepth();
		$scope.renderer.render( $scope.sceneOrtho, $scope.cameraOrtho );
	}
  	
  	$scope.animate = function() {
  		for(var i=0; i<$scope.spritesMap.length; i++) {
  			var sprite = $scope.spritesMap[i];
  			$scope.scaleSprite(sprite, 40);
  		}
  		for(var i=0; i<$scope.orthoMap.length; i++) {
  			var sprite = $scope.orthoMap[i];
  			$scope.updateTexturePoint(sprite);
  		}
  		for(var i=0; i<$scope.labelsMap.length; i++) {
  			var label = $scope.labelsMap[i];
  			$scope.scaleSpriteAllScales(label, 15, 12, 15);
  		}
  		requestAnimationFrame($scope.animate);
  		$scope.controls.update();
  	}
  	
  	$scope.isObjectVisible = function(position, camera, controls) {
  		var viewVector = new THREE.Vector3();
  		viewVector.subVectors(camera.position, controls.target);
  		
  		var diffVector = new THREE.Vector3();
  		diffVector.subVectors(position, camera.position);
  		
  		return (viewVector.dot(diffVector) < 0);
  	}
  	
  	$scope.scaleSprite = function(sprite, scale) {
  		$scope.scaleSpriteAllScales(sprite, scale, scale, scale);
  	}

  	$scope.scaleSpriteAllScales = function(sprite, scaleX, scaleY, scaleZ) {
  		var v = new THREE.Vector3();
  		var dist = v.subVectors( sprite.position, $scope.camera.position ).length();
  		sprite.scale.x = dist / scaleX;
  		sprite.scale.y = dist / scaleY;
  		sprite.scale.z = dist / scaleZ;
  	}
  	
}]);

spaceSimulatorControllers.controller('ProjectController', ['$scope', function($scope) {
	
}]);

spaceSimulatorControllers.controller('HelpController', ['$scope', function($scope) {
	
}]);
