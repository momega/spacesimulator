var imagesLoaded = 0;
var imagesCount = 0;

function loadTexture(name, source, texturesMap, callback) {
	var textureLoader = new THREE.TextureLoader();
	textureLoader.load(
		source,
		function ( texture ) {
			console.log('Texture for name=' + name + ' loaded.');
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
