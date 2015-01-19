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
	
	camera.position.z = 5;
	camera.position.y = 1;
	camera.position.x = 1;

	controls = new THREE.OrbitControls( camera, container );
	//controls.target = 
	controls.damping = 0.2;
	controls.addEventListener( 'change', render );

	var geometry = new THREE.SphereGeometry( 1, 64, 64 );
	
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
			console.log("Textures loaded");
			render();
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
	
	var geometry2 = new THREE.BoxGeometry( 1, 1, 1 );
	var material2 = new THREE.MeshBasicMaterial( { color: 0x00ff00 } );
	var cube = new THREE.Mesh( geometry2, material2 );
	cube.position.x = 1;
	cube.position.y = 1;
	cube.updateMatrix();
	cube.matrixAutoUpdate = false;
	scene.add( cube );

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
	console.log("Render called");
	renderer.render( scene, camera );
}