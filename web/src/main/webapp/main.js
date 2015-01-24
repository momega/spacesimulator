var scene;
var renderer;
var camera;
var controls;

var cameraTarget;
var cameraPosition;
var cameraDiff;



function initScene() {
	
	cameraTarget = new THREE.Vector3(0, 0, 0);
	cameraDiff = new THREE.Vector3(0, 0, 5);
	
	controls = new THREE.OrbitControls( camera, container );
	controls.damping = 0.2;
	updateCameraPosition(cameraTarget);
	controls.addEventListener( 'change', render );

	var geometry = new THREE.SphereGeometry( 1, 64, 64 );
	var texture = texturesMap['earth'];
	var material = new THREE.MeshBasicMaterial( { map: texture } );
	var sphere = new THREE.Mesh( geometry, material );
	scene.add( sphere );
	
	var geometry2 = new THREE.BoxGeometry( 0.5, 1, 1 );
	var material2 = new THREE.MeshBasicMaterial( { color: 0x00ff00 } );
	var cube = new THREE.Mesh( geometry2, material2 );
	cube.position.x = 1;
	cube.position.y = 1;
	cube.updateMatrix();
	cube.matrixAutoUpdate = false;
	scene.add( cube );
	
	var crateMaterial = new THREE.SpriteMaterial( { map: texturesMap['1'], useScreenCoordinates: false } );
	var sprite2 = new THREE.Sprite( crateMaterial );
	sprite2.position.set( 2, 2, 0 );
	sprite2.scale.set( 0.1, 0.1, 0.5 ); // imageWidth, imageHeight
	scene.add( sprite2 );
	
	var curve = new THREE.EllipseCurve(
			0,  0,            // ax, aY
			3, 5,           // xRadius, yRadius
			0,  2 * Math.PI,  // aStartAngle, aEndAngle
			false             // aClockwise
		);
	var path = new THREE.Path( curve.getPoints( 50 ) );
	var geometry5 = path.createPointsGeometry( 50 );
	var material5 = new THREE.LineBasicMaterial( { color : 0xff5500 } );
	// Create the final Object3d to add to the scene
	var ellipse = new THREE.Line( geometry5, material5 );
	ellipse.rotation.x = -0.5; 
	scene.add(ellipse);
	
	var geometry3 = new THREE.SphereGeometry( 0.3, 64, 64 );
	var texture2 = texturesMap['mars'];
	var material = new THREE.MeshBasicMaterial( { map: texture2 } );
	var mars = new THREE.Mesh( geometry3, material );
	mars.position.x = -2;
	mars.position.y = 1;
	mars.updateMatrix();
	mars.matrixAutoUpdate = false;
	scene.add( mars );
	
	

	render();
	animate();
}


