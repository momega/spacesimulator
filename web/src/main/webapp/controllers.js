'use strict';

var AU = 149597870700.0;

spaceSimulatorApp.controller('SimulationController', ['$scope', 'modelService', function($scope, modelService) {
	
    $scope.details = {
    		basic: {open: true, disabled: true},
    		spacecraft: {open: false, disabled: true},
    		physical: {open: false, disabled: true},
    	    orbital: {open: false, disabled: true}
    };
    
    $scope.prepareModel = function(model) {
	    $scope.time = model.time.value;
	    $scope.timeInMillis = new Date($scope.time * 1000);
	    $scope.cameraTarget = model.camera.targetObject;
	    $scope.selectedObject = null;
	    $scope.texturesMap = {};
	    $scope.orthoMap = [];
	    $scope.pointsMap = [];

	    console.log("camera target:" + $scope.cameraTarget);
	    
	    var textureObjects = [];
	    $scope.positionProviders = modelService.getPositionProviders($scope.time);
	    for(var i=0; i<$scope.positionProviders.length; i++) {
	    	var obj = $scope.positionProviders[i];
	    	if (obj.textureFileName != null) {
	    		textureObjects.push(obj);
	    	} else if (obj.subsystems != null) {
	    		textureObjects.push(obj);
	    	}
	    }
	    
	    $scope.loadTextures(textureObjects, $scope.texturesLoaded);
   };
   
   modelService.load('1', '10', $scope.prepareModel);
   
   $scope.loadTextures = function(textureObjects, callback) {
		imagesCount = textureObjects.length + 7 // + 7 icons;
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
		loadTexture('M_START', 'icons/Math-lower-than-icon.png', $scope.texturesMap, callback);
		loadTexture('M_END', 'icons/Math-greater-than-icon.png', $scope.texturesMap, callback);
		loadTexture('CIRCLE', 'icons/circle.png', $scope.texturesMap, callback);
		loadTexture('EXIT_SOI', 'icons/Letter-E-icon.png', $scope.texturesMap, callback);
		loadTexture('T_INTERSECTION', 'icons/Math-divide-icon.png', $scope.texturesMap, callback);
	}
   
    $scope.texturesLoaded = function() {
	   console.log('All texture loaded');
	   $scope.createScene();
    }
    
    $scope.createScene = function() {
		$scope.scene = new THREE.Scene();
		$scope.sceneOrtho = new THREE.Scene();

		var celestialBodies = modelService.getRootObjects();
    	for(var i=0; i<celestialBodies.length; i++) {
    		var celestialBody = celestialBodies[i];
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
    		
    		if (modelService.isSpacecraft(celestialBody)) {
    			var spacecraft = celestialBody;
    			$scope.createSpacecraft(spacecraft);
    			
    			if (spacecraft.trajectory.apoapsis) {
    				$scope.createTexturePoint(spacecraft.trajectory.apoapsis, 'APOAPSIS');
    			}
    			
    			if (spacecraft.trajectory.periapsis) {
    				$scope.createTexturePoint(spacecraft.trajectory.periapsis, 'PERIAPSIS');
    			}
    			
    			var maneuver = modelService.findActiveOrNextManeuver(spacecraft, $scope.time);
    			if (maneuver != null) {
    				$scope.createTexturePoint(maneuver.start, 'M_START');
    				$scope.createTexturePoint(maneuver.end, 'M_END');
    			}
    			
    			var target = spacecraft.target;
    			if (target != null) {
    				for(var j=0; j<target.orbitIntersections.length; j++) {
    					var intersection = target.orbitIntersections[j];
    					$scope.createTexturePoint(intersection, 'T_INTERSECTION');
    				}
    			}
    			
    			var exitSoi = spacecraft.exitSoiOrbitalPoint;
				if (exitSoi != null) {
					$scope.createTexturePoint(exitSoi, 'EXIT_SOI');
				}
    		}
    		
    		if (celestialBody.keplerianElements!=null) {
    			// the object has a trajectory
    			var ke = celestialBody.keplerianElements;
    			var a = ke.keplerianOrbit.semimajorAxis;
    			var ec = ke.keplerianOrbit.eccentricity;
    			var e = a * ec;
    			var centerObject = modelService.findByName(ke.keplerianOrbit.centralObject);
    			
    			var geometry5;
    			if (ec<1) {
    				var b = a * Math.sqrt(1 - ec*ec);
    				var curve = new THREE.EllipseCurve(
	    					-e,  0,           // ax, aY
	    					a, b,             // xRadius, yRadius
	    					0,  2 * Math.PI,  // aStartAngle, aEndAngle
	    					false             // aClockwise
	    				);
	    			var path = new THREE.Path( curve.getPoints( 3600 ) );
	    			geometry5 = path.createPointsGeometry( 3600 );
    			} else {
    				var b = a * Math.sqrt(ec*ec-1);
    				var startAngle = -2 * Math.PI;
    				if (celestialBody.exitSoiOrbitalPoint != null) {
    					startAngle = -solveHA(ec, celestialBody.exitSoiOrbitalPoint.keplerianElements.trueAnomaly);
    				}
    				var stopAngle = -solveHA(ec, ke.trueAnomaly);
    				geometry5 = $scope.createHyperbola(a, b, startAngle, stopAngle, 3600, e);
    			}
    			
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
    	$scope.selectCameraTarget(modelService.findByName($scope.cameraTarget));
    	$scope.animate();
    	console.log('Animation started');
    }
    
    $scope.createHyperbola = function(a, b, startAngle, stopAngle, num_segments, e) {
    	var DEG2RAD = 2 * Math.PI / num_segments;
        var startIndex = (startAngle / DEG2RAD)|0;
        var stopIndex = (stopAngle / DEG2RAD)|0;
        var points = [];
        for (var i= startIndex; i<=stopIndex; i++) {
            var degInRad = DEG2RAD * i;
            points.push({x : Math.cosh(degInRad) * a - e, y : Math.sinh(degInRad) * b, z:0});
        }
        var path = new THREE.CurvePath();
        var result = path.createGeometry(points);
        return result;
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
		var spriteMaterial = new THREE.SpriteMaterial({map: texture});
		var sprite = new THREE.Sprite( spriteMaterial );

		var p = new THREE.Vector3();
		p.copy(modelService.getPosition(obj));

		sprite.position.copy(p);
		sprite.body = obj;
		sprite.scale.set( 16, 16, 1 );
		$scope.pointsMap.push(sprite);
		$scope.scene.add( sprite );
    } 
    
    /**
     * Transforms the position of the object in World coordinates into orthographic coordinates
     */
    $scope.getOrthoPosition = function(obj) {
    	var p = new THREE.Vector3();
		p.copy(modelService.getPosition(obj));
		p = p.project($scope.camera);
		var result = new THREE.Vector3();
		result.x = p.x;
		result.y = p.y;
		result.z = p.z;
		result = result.unproject($scope.cameraOrtho);
		return result;
    }
    
    $scope.toDegrees = function(rad) {
    	return rad * 180 / Math.PI;
    }
    
    $scope.updateOrthoObject = function(sprite) {
    	var obj = sprite.body;
    	var position = $scope.getOrthoPosition(obj);
    	var positionOffset = sprite.hasOwnProperty("positionOffset") ? sprite["positionOffset"] : 0;
    	position.y -= positionOffset; 
    	sprite.position.copy(position);
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
    
    $scope.selectObject = function(obj) {
    	$scope.selectedObject = obj;
    	$scope.setDetails(obj);
    }
    
    $scope.setDetails = function(obj) {
    	$scope.details.basic.disabled = false;
    	$scope.details.orbital.disabled = !modelService.hasTrajectory(obj);
    	$scope.details.spacecraft.disabled = !modelService.isSpacecraft(obj);
    	$scope.details.physical.disabled = !modelService.isCelestialBody(obj);
    	$scope.details.basic.open = true;
    }
    
    $scope.openWiki = function() {
    	var wiki = 'http://en.wikipedia.org/wiki/' + $scope.selectedObject.wiki;
    	window.open(wiki, '_blank');
    }
    
    $scope.selectCameraByTargetName = function(name) {
    	var body = modelService.findByName(name);
    	$scope.selectCameraTarget(body);
    }
  
    $scope.selectCameraTarget = function(obj) {
	  console.log('body = ' + obj.name);
	  $scope.cameraTarget = obj.name;
	  var targetBody = obj;
	  var newRadius = targetBody.hasOwnProperty("radius") ? targetBody["radius"] : 100000;
	  $scope.updateCameraPosition(modelService.getPosition(targetBody), newRadius * 10);
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
		
		$scope.camera = new THREE.PerspectiveCamera( 45, canvasWidth/canvasHeight, 100000, AU * 10 );
		$scope.camera.up.copy(new THREE.Vector3(0,0,1));	
		
		$scope.renderer = new THREE.WebGLRenderer();
		$scope.renderer.setSize( canvasWidth, canvasHeight );
		$scope.renderer.autoClear = false;
		container.appendChild( $scope.renderer.domElement );
		
		$scope.controls = new THREE.OrbitControls( $scope.camera, container );
		$scope.controls.noPan = true;
		$scope.controls.mouseButtons.ORBIT = THREE.MOUSE.RIGHT;
		$scope.controls.addEventListener( 'change', $scope.render );
		
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
    	if (event.which==1) {
	    	var mousePos = getMousePos($scope.renderer.domElement, event);
	    	$scope.mouse.x = ( mousePos.x / $scope.canvasWidth ) * 2 - 1;
	    	$scope.mouse.y = - ( mousePos.y / $scope.canvasHeight ) * 2 + 1;
	    	console.log('x = ' + $scope.mouse.x + ' y = ' + $scope.mouse.y);
	    	
	    	$scope.raycaster.setFromCamera( $scope.mouse, $scope.camera );
	    	var intersects = $scope.raycaster.intersectObjects( $scope.scene.children );
	    	if (intersects.length>0) {
	    		var intersect = intersects[0];
	    		$scope.$apply(function() {
	    			console.log('selected object = ' + intersect.object.body.name);
	    			$scope.selectObject(intersect.object.body);
	    		});
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
  		for(var i=0; i<$scope.pointsMap.length; i++) {
  			var pointSprite = $scope.pointsMap[i];
  			$scope.scaleSprite(pointSprite, 32);
  		}
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
  	
  	$scope.scaleSprite = function(sprite, scale) {
  		var v = new THREE.Vector3();
  		sprite.scale.x = sprite.scale.y = sprite.scale.z = v.subVectors( sprite.position, $scope.camera.position ).length() / scale;
  	}
  	
  	/**
  	 * Returns whether or not the object is in front of the camera
  	 */
  	$scope.isObjectVisible = function(obj) {
  		return $scope.isPositionVisible(modelService.getPosition(obj), $scope.camera, $scope.controls);
  	}
  	
  	$scope.isPositionVisible = function(position, camera, controls) {
  		var viewVector = new THREE.Vector3();
  		viewVector.subVectors(camera.position, controls.target);
  		
  		var diffVector = new THREE.Vector3();
  		diffVector.subVectors(position, camera.position);
  		
  		return (viewVector.dot(diffVector) < 0);
  	}
  	
}]);

spaceSimulatorApp.controller('ProjectController', ['$scope', function($scope) {
	
}]);

spaceSimulatorApp.controller('HelpController', ['$scope', function($scope) {
	
}]);
