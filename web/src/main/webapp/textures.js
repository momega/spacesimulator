var texturesMap = {};
var imagesLoaded = 0;
var imagesCount = 0;

function loadTextures(names, sources, callback) {
	imagesCount = sources.length;
	console.log(imagesCount +' about to load');
	for(var i=0; i<imagesCount; i++) {
		var name = names[i];
		console.log('Texture for ' + names[i] + ' sources '+ sources[i]);
		loadTexture(name, sources[i], callback);
	}
}

function loadTexture(name, source, callback) {
	var textureLoader = new THREE.TextureLoader();
	textureLoader.load(
		source,
		function ( texture ) {
			console.log('Texture for ' + texture + ' name=' + name);
			texturesMap[name]=texture;
			imagesLoaded++;
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
