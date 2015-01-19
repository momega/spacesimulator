var scene;
var renderer;
var camera;
var controls;

function initScene() {
	container = document.getElementById( 'container' );
	document.getElementById( 'container' ).innerHTML = "";
	var canvasWidth = container.offsetWidth;
	var canvasHeight = 480;
	
	scene = new THREE.Scene();
	camera = new THREE.PerspectiveCamera( 45, canvasWidth/canvasHeight, 0.1, 1000 );
	
	renderer = new THREE.WebGLRenderer();
	renderer.setSize( canvasWidth, canvasHeight );
	container.appendChild( renderer.domElement );

	controls = new THREE.OrbitControls( camera );
	controls.damping = 0.2;
	controls.addEventListener( 'change', render );

	var geometry = new THREE.SphereGeometry( 0.5, 64, 64 );
	
	// instantiate a loader
	var loader = new THREE.TextureLoader();
	// load a resource
	loader.load(
		// resource URL
		'textures/earth_hi.jpg',
		// Function when resource is loaded
		function ( texture ) {
			// do something with the texture
			var material = new THREE.MeshBasicMaterial( { map: texture } );
			var sphere = new THREE.Mesh( geometry, material );
			scene.add( sphere );
			console.log("Textures loaded")
			animate();
		},
		// Function called when download progresses
		function ( xhr ) {
			console.log( (xhr.loaded / xhr.total * 100) + '% loaded' );
		},
		// Function called when download errors
		function ( xhr ) {
			console.log( 'An error happened' );
		}
	);
			
	camera.position.z = 5;
//	camera.position.y = 1;
//	camera.position.x = 1;
	
//	vector3 = new THREE.Vector3(-1, 1, 1 );
//	camera.lookAt(vector3);
	
	

/*			var render = function () {
				requestAnimationFrame( render );

				cube.rotation.x += 0.1;
				cube.rotation.y += 0.1;

				
			};
*/
}

function animate() {
	requestAnimationFrame(animate);
	controls.update();
}

function render() {
	renderer.render( scene, camera );
}