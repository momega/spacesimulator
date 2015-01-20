var scene;
var renderer;
var camera;
var controls;

function init() {
	container = document.getElementById( 'container' );
	document.getElementById( 'container' ).innerHTML = "";
	var canvasWidth = container.offsetWidth;
	var canvasHeight = 480;
	
	scene = new THREE.Scene();
	camera = new THREE.PerspectiveCamera( 45, canvasWidth/canvasHeight, 0.1, 1000 );

	renderer = new THREE.WebGLRenderer();
	renderer.setSize( canvasWidth, canvasHeight );
	container.appendChild( renderer.domElement );
	
	loadAllTextures();
}

function loadAllTextures() {
	var names = ['earth', 'mars'];
	var sources = ['textures/earth_hi.jpg', 'textures/mars_hi.jpg'];
	loadTextures(names, sources, initScene);
}

function initScene() {
	
	camera.position.z = 5;
	camera.position.y = 1;
	camera.position.x = 1;

	controls = new THREE.OrbitControls( camera, container );
	//controls.target = 
	controls.damping = 0.2;
	controls.addEventListener( 'change', render );

	var geometry = new THREE.SphereGeometry( 1, 64, 64 );
	var texture = texturesMap['earth'];
	var material = new THREE.MeshBasicMaterial( { map: texture } );
	var sphere = new THREE.Mesh( geometry, material );
	scene.add( sphere );
	
	var geometry2 = new THREE.BoxGeometry( 1, 1, 1 );
	var material2 = new THREE.MeshBasicMaterial( { color: 0x00ff00 } );
	var cube = new THREE.Mesh( geometry2, material2 );
	cube.position.x = 1;
	cube.position.y = 1;
	cube.updateMatrix();
	cube.matrixAutoUpdate = false;
	scene.add( cube );
	
	var geometry3 = new THREE.SphereGeometry( 0.3, 64, 64 );
	var texture2 = texturesMap['mars'];
	var material = new THREE.MeshBasicMaterial( { map: texture2 } );
	var mars = new THREE.Mesh( geometry3, material );
	mars.position.x = -2;
	mars.position.y = 0;
	mars.updateMatrix();
	mars.matrixAutoUpdate = false;
	scene.add( mars );

/*			var render = function () {
				requestAnimationFrame( render );

				cube.rotation.x += 0.1;
				cube.rotation.y += 0.1;

				
			};
*/
	render();
	animate();
}

function animate() {
	requestAnimationFrame(animate);
	controls.update();
}

function render() {
	console.log("Render called");
	renderer.render( scene, camera );
}