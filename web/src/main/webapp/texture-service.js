'use strict';

spaceSimulatorApp.factory('textureService', ['$http', 'Model', function($http, Model) {
	
	var factory = {};
	var texturesMap = {};
	var imagesLoaded = 0;
	var imagesCount = 0;
	var project;
	
	factory.load = function(pid, callback) {
		project = Model.item({id: pid}, function() {
			var objs = project.celestialBodies;
			factory.loadTextures(objs, function() {
				console.log('All texture loaded');
				callback();
			});
		});
	}
	
	factory.getProject = function() {
		return project;
	}
	
	factory.getTextureName = function(textureName) {
		return texturesMap[textureName];
	}
	
	factory.loadTextures = function(textureObjects, callback) {
		   imagesLoaded = 0;
		   imagesCount = textureObjects.length + 9 + 9; // + 8 icons;
			console.log(imagesCount +' about to load');
			for(var i=0; i<textureObjects.length; i++) {
				var to = textureObjects[i];
				var name = to.name;
				var source = "." + to.textureFileName;
				console.log('Texture for ' + name + ' sources '+ source);
				factory.loadTexture(name, source, callback);
			}
			
			for(var j=1; j<=9; j++) {
				var source = "./images/Number-" + j + "-icon.png";
				factory.loadTexture('SPACECRAFT' + j, source, callback);
			}
			
			factory.loadTexture('APOAPSIS', 'images/Letter-A-icon.png', callback);
			factory.loadTexture('PERIAPSIS', 'images/Letter-P-icon.png', callback);
			factory.loadTexture('M_START', 'images/Math-lower-than-icon.png', callback);
			factory.loadTexture('M_END', 'images/Math-greater-than-icon.png', callback);
			factory.loadTexture('CIRCLE', 'images/circle.png', callback);
			factory.loadTexture('EXIT_SOI', 'images/Letter-E-icon.png', callback);
			factory.loadTexture('T_INTERSECTION', 'images/Math-divide-icon.png', callback);
			factory.loadTexture('START', 'images/Letter-S-icon.png', callback);
			factory.loadTexture('CRASHSITE', 'images/Letter-X-icon.png', callback);
	}
	
	factory.loadTexture = function(name, source, callback) {
		var textureLoader = new THREE.TextureLoader();
		textureLoader.load(
			source,
			function ( texture ) {
				console.log('Texture for name=' + name + ' loaded.');
				texturesMap[name]=texture;
				imagesLoaded++;
				console.log('imagesLoaded=' + imagesLoaded + ' imagesCount='+imagesCount);
				if (imagesLoaded>=imagesCount) {
					callback();
				}
			},
			// Function called when download progresses
			function ( xhr ) {
				console.log( (xhr.loaded / xhr.total * 100) + '% loaded' );
			},
			// Function called when download errors
			function ( xhr ) {
				console.log( 'An error happened' );
				imagesLoaded++;
			}
		);
	}
	
	return factory;
}]);