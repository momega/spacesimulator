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

function makeTextSprite2(message, parameters) {
    if ( parameters === undefined ) parameters = {};
    var fontface = parameters.hasOwnProperty("fontface") ? parameters["fontface"] : "sans-serif";
    var fontsize = parameters.hasOwnProperty("fontsize") ? parameters["fontsize"] : 8;
    var borderThickness = parameters.hasOwnProperty("borderThickness") ? parameters["borderThickness"] : 0.5;
    var borderColor = parameters.hasOwnProperty("borderColor") ?parameters["borderColor"] : { r:200, g:200, b:200, a:1.0 };
    var backgroundColor = parameters.hasOwnProperty("backgroundColor") ?parameters["backgroundColor"] : { r:20, g:20, b:20, a:1.0 };
    var textColor = parameters.hasOwnProperty("textColor") ?parameters["textColor"] : { r:255, g:255, b:255, a:1.0 };
	
    var canvas = document.createElement('canvas');
    var context = canvas.getContext('2d');
    context.font = "normal" + fontsize + "px " + fontface ;

    canvas.width = 50;
    canvas.height = 50;
    canvas.style.width = canvas.width + 'px';
    canvas.style.height = canvas.height + 'px';
    
    context.lineWidth = borderThickness;
    context.fillStyle   = "rgba(" + backgroundColor.r + "," + backgroundColor.g + "," + backgroundColor.b + "," + backgroundColor.a + ")";
    context.strokeStyle = "rgba(" + borderColor.r + "," + borderColor.g + "," + borderColor.b + "," + borderColor.a + ")";
    
    //context.fillStyle = "rgba("+textColor.r+", "+textColor.g+", "+textColor.b+", 1.0)";
    //context.fillText( message, 0 , 20);
    
    roundRect(context, 0, 0, canvas.width, canvas.height, 3);
    
    var texture = new THREE.Texture(canvas);
    texture.needsUpdate = true;
    var spriteMaterial = new THREE.SpriteMaterial( { map: texture, useScreenCoordinates: true } );
    var sprite = new THREE.Sprite( spriteMaterial );
    return sprite;  
}

function makeTextSprite( message, parameters ) {
    if ( parameters === undefined ) parameters = {};
    var fontface = parameters.hasOwnProperty("fontface") ? parameters["fontface"] : "sans-serif";
    var fontsize = parameters.hasOwnProperty("fontsize") ? parameters["fontsize"] : 8;
    var borderThickness = parameters.hasOwnProperty("borderThickness") ? parameters["borderThickness"] : 0.5;
    var borderColor = parameters.hasOwnProperty("borderColor") ?parameters["borderColor"] : { r:200, g:200, b:200, a:1.0 };
    var backgroundColor = parameters.hasOwnProperty("backgroundColor") ?parameters["backgroundColor"] : { r:20, g:20, b:20, a:1.0 };
    var textColor = parameters.hasOwnProperty("textColor") ?parameters["textColor"] : { r:255, g:255, b:255, a:1.0 };
    
    var canvas = document.createElement('canvas');
    var context = canvas.getContext('2d');
    context.font = "normal" + fontsize + "px " + fontface ;
    var metrics = context.measureText( message );
    var textWidth = metrics.width;
    canvas.width = (textWidth + borderThickness + 1) * 1.2;
    canvas.height = 4 * fontsize * 1.4;
    canvas.style.width = canvas.width + 'px';
    canvas.style.height = canvas.height + 'px';

    context.fillStyle   = "rgba(" + backgroundColor.r + "," + backgroundColor.g + "," + backgroundColor.b + "," + backgroundColor.a + ")";
    context.strokeStyle = "rgba(" + borderColor.r + "," + borderColor.g + "," + borderColor.b + "," + borderColor.a + ")";
    
    context.lineWidth = borderThickness;
    roundRect(context, borderThickness/2, borderThickness/2, (textWidth + borderThickness) * 1.2, fontsize * 1.4 + borderThickness, 3);
    
    context.fillStyle = "rgba("+textColor.r+", "+textColor.g+", "+textColor.b+", 1.0)";
    context.fillText( message, borderThickness+1 , fontsize + borderThickness + 1);

    var texture = new THREE.Texture(canvas);
    texture.needsUpdate = true;
    var spriteMaterial = new THREE.SpriteMaterial( { map: texture, useScreenCoordinates: false } );
    var sprite = new THREE.Sprite( spriteMaterial );
    return sprite;  
}

function roundRect(ctx, x, y, w, h, r) 
{
    ctx.beginPath();
    ctx.moveTo(x+r, y);
    ctx.lineTo(x+w-r, y);
    ctx.quadraticCurveTo(x+w, y, x+w, y+r);
    ctx.lineTo(x+w, y+h-r);
    ctx.quadraticCurveTo(x+w, y+h, x+w-r, y+h);
    ctx.lineTo(x+r, y+h);
    ctx.quadraticCurveTo(x, y+h, x, y+h-r);
    ctx.lineTo(x, y+r);
    ctx.quadraticCurveTo(x, y, x+r, y);
    ctx.closePath();
    ctx.fill();
	ctx.stroke();   
}

function arrayToColor(array) {
	var c = new THREE.Color();
	c.setRGB(array[0], array[1], array[2]);
	return c;
}

function darken(color, factor){
	var c = new THREE.Color();
	c.setRGB(color.r * 1-factor, color.g * factor, color.b * factor);
	return c;
}
