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
	    $scope.orthoMap = [];
	    console.log("camera target:" + $scope.cameraTarget);
	    
	    $scope.positionProviders = [];
	    var rootObjects = $scope.db.select("//movingObjects/*").values();
	    for(var i=0; i<rootObjects.length; i++) {
	    	var obj = rootObjects[i];
	    	$scope.positionProviders.push(obj);
	    	if ($scope.isSpacecraft(obj)) {
	    		if (obj.trajectory.apoapsis!=null) {
	    			$scope.positionProviders.push(obj.trajectory.apoapsis);
	    		}
	    		if (obj.trajectory.periapsis!=null) {
	    			$scope.positionProviders.push(obj.trajectory.periapsis);
	    		}
	    	}
	    }
	    
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
    		
    		if (celestialBody.radius != null) {
	    		var geometry = new THREE.SphereGeometry( celestialBody.radius, 32, 32 );
	   		
	    		// change orientation, north pole is in Z direction
	    		var t = new THREE.Matrix4();
	    		t.makeRotationX(Math.PI/2);
	    		geometry.applyMatrix(t);
	    		
	    		var texture = $scope.texturesMap[celestialBody.name];
	    		var material = new THREE.MeshBasicMaterial( { map: texture } );
	    		var sphere = new THREE.Mesh( geometry, material );
	    		sphere.body = celestialBody;
	    		sphere.position.copy(position);
	    		$scope.scene.add( sphere );
	    		
	    		$scope.createBodyLocator(celestialBody);
    			$scope.createBodyLabel(celestialBody);
    		}
    		
    		if ($scope.isSpacecraft(celestialBody)) {
    			var spacecraft = celestialBody;
    			$scope.createSpacecraft(spacecraft);
    			
    			if (spacecraft.trajectory.apoapsis) {
    				$scope.createTexturePoint(spacecraft.trajectory.apoapsis, 'APOAPSIS');
    			}
    			
    			if (spacecraft.trajectory.periapsis) {
    				$scope.createTexturePoint(spacecraft.trajectory.periapsis, 'PERIAPSIS');
    			}
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
    			
    			var theColor = arrayToColor(celestialBody.trajectory.color);
    			var material5 = new THREE.LineBasicMaterial( { color: theColor } );
    			var ellipse = new THREE.Line( geometry5, material5 );
    			ellipse.position.add(centerObject.cartesianState.position);
    			$scope.scene.add(ellipse);
    			
    		}
    	}
    	
    	console.log('Scene created');
    	$scope.selectCameraTarget($scope.findByName($scope.cameraTarget));
    	$scope.animate();
    	console.log('Animation started');
    }
    
    $scope.createBodyLabel = function(obj) {
    	var texture = makeLabelTexture(obj.name, {});
    	var spriteMaterial = new THREE.SpriteMaterial( { map: texture } );
        var spriteL = new THREE.Sprite( spriteMaterial );
        
        spriteL.scale.set( texture.textureWidth, texture.textureHeight, 1 );
		var p = $scope.getOrthoPosition(obj);
		spriteL.position.copy(p);
		spriteL.body = obj;
		spriteL.positionOffset = -20;
		$scope.sceneOrtho.add(spriteL);
		$scope.orthoMap.push(spriteL); 
    }
    
    $scope.createBodyLocator = function(obj) {
    	var material = new THREE.LineBasicMaterial({
    		color: 0x808080
    	});
    	
    	var curve = new THREE.EllipseCurve(
    			0,  0,            // ax, aY
    			8, 8,           // xRadius, yRadius
    			0,  2 * Math.PI,  // aStartAngle, aEndAngle
    			false             // aClockwise
    		);
    	
    	var path = new THREE.Path( curve.getPoints( 32 ) );
    	var circleGeometry = path.createPointsGeometry( 32 );
    	
    	var circle = new THREE.Line( circleGeometry, material );
    	var p = $scope.getOrthoPosition(obj);
    	circle.position.copy(p);
    	circle.body = obj;
    	
    	$scope.sceneOrtho.add(circle);
		$scope.orthoMap.push(circle);
    }
    
    $scope.createSpacecraft = function(obj) {
    	$scope.createTexturePoint(obj, obj.name);
    } 
    
    $scope.createTexturePoint = function(obj, textureName) {
    	var texture = $scope.texturesMap[textureName];
    	texture.needsUpdate = true;
    	var material = new THREE.SpriteMaterial( { map: texture } );
    	var spriteTL = new THREE.Sprite( material );
		spriteTL.scale.set( 16, 16, 1 );
		var p = $scope.getOrthoPosition(obj);
		spriteTL.position.copy(p);
		spriteTL.body = obj;
		spriteTL.alwaysVisible = true;
		$scope.sceneOrtho.add(spriteTL);
		$scope.orthoMap.push(spriteTL);
    } 
    
    $scope.getPosition = function(obj) {
    	var position = obj.hasOwnProperty("position") ? obj["position"] : obj.cartesianState.position;
    	return position;
    }
    
    /**
     * Transforms the position of the object in World coordinates into orthographic coordinates
     */
    $scope.getOrthoPosition = function(obj) {
    	var p = new THREE.Vector3();
		p.copy($scope.getPosition(obj));
		p = p.project($scope.camera);
		var result = new THREE.Vector3();
		result.x = p.x;
		result.y = p.y;
		result.z = p.z;
		result = result.unproject($scope.cameraOrtho);
		return result;
    }
    
    $scope.updateOrthoObject = function(sprite) {
    	var obj = sprite.body;
    	var position = $scope.getOrthoPosition(obj);
    	var positionOffset = sprite.hasOwnProperty("positionOffset") ? sprite["positionOffset"] : 0;
    	position.y -= positionOffset; 
    	sprite.position.copy(position);
    }
    
    $scope.findByName = function(name) {
    	var movingObject = $scope.db.select("//*[/name=='" + name + "']").value();
    	return movingObject;
    }
    
    $scope.isSpacecraft = function(obj) {
    	var result = (obj.subsystems != null);
    	return result;
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
  
    $scope.selectCameraTarget = function(obj) {
	  console.log('body = ' + obj.name);
	  $scope.cameraTarget = obj.name;

	  var targetBody = $scope.findByName($scope.cameraTarget);
	  console.log('targetBody='+targetBody.name);
	  var newRadius = targetBody.hasOwnProperty("radius") ? targetBody["radius"] : 100000;
	  $scope.updateCameraPosition($scope.getPosition(targetBody), newRadius * 10);
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
		
		$scope.raycaster = new THREE.Raycaster();
		$scope.mouse = new THREE.Vector2();
		
		container.addEventListener( 'mousedown', $scope.mouseClick, false );
	}  
    
    $scope.mouseClick = function( event ) {
    	event.preventDefault();
    	
    	var mousePos = getMousePos($scope.renderer.domElement, event);
    	$scope.mouse.x = ( mousePos.x / $scope.canvasWidth ) * 2 - 1;
    	$scope.mouse.y = - ( mousePos.y / $scope.canvasHeight ) * 2 + 1;
    	console.log('x = ' + $scope.mouse.x + ' y = ' + $scope.mouse.y);
    	
    	$scope.raycaster.setFromCamera( $scope.mouse, $scope.camera );
    	var intersects = $scope.raycaster.intersectObjects( $scope.scene.children );
    	console.log('touch ' + intersects);
    	for ( var intersect in intersects ) {
    		for (x in intersect) {
    		    console(x);
    		}
    	}
    }
  
  	$scope.render = function() {
  		$scope.renderer.clear();
		$scope.renderer.render( $scope.scene, $scope.camera );
		$scope.renderer.clearDepth();
		$scope.renderer.render( $scope.sceneOrtho, $scope.cameraOrtho );
	}
  	
  	$scope.animate = function() {
  		for(var i=0; i<$scope.orthoMap.length; i++) {
  			var sprite = $scope.orthoMap[i];
  			var visible = $scope.isObjectVisible(sprite.body);
  			if ($scope.cameraTarget == sprite.body.name) {
  				visible = false;
  			}
  			if (sprite.alwaysVisible == true) {
  				visible = true;
  			}
  			if (visible) {
  				sprite.visible = true;
  				$scope.updateOrthoObject(sprite);
  			} else {
  				sprite.visible = false;
  			}
  		}
  		requestAnimationFrame($scope.animate);
  		$scope.controls.update();
  	}
  	
  	/**
  	 * Returns whether or not the object is in front of the camera
  	 */
  	$scope.isObjectVisible = function(obj) {
  		return $scope.isPositionVisible($scope.getPosition(obj), $scope.camera, $scope.controls);
  	}
  	
  	$scope.isPositionVisible = function(position, camera, controls) {
  		var viewVector = new THREE.Vector3();
  		viewVector.subVectors(camera.position, controls.target);
  		
  		var diffVector = new THREE.Vector3();
  		diffVector.subVectors(position, camera.position);
  		
  		return (viewVector.dot(diffVector) < 0);
  	}
  	
}]);

spaceSimulatorControllers.controller('ProjectController', ['$scope', function($scope) {
	
}]);

spaceSimulatorControllers.controller('HelpController', ['$scope', function($scope) {
	
}]);
