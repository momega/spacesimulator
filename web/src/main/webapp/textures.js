var loopId = null;

function makeLabelTexture( message, parameters ) {
    if ( parameters === undefined ) parameters = {};
    var fontface = parameters.hasOwnProperty("fontface") ? parameters["fontface"] : "arial";
    var fontsize = parameters.hasOwnProperty("fontsize") ? parameters["fontsize"] : 14;
    var borderThickness = parameters.hasOwnProperty("borderThickness") ? parameters["borderThickness"] : 0.5;
    var borderColor = parameters.hasOwnProperty("borderColor") ?parameters["borderColor"] : { r:20, g:20, b:20, a:1.0 };
    var backgroundColor = parameters.hasOwnProperty("backgroundColor") ?parameters["backgroundColor"] : { r:20, g:20, b:20, a:1.0 };
    var textColor = parameters.hasOwnProperty("textColor") ?parameters["textColor"] : { r:255, g:255, b:255, a:1.0 };
    
    var canvas = document.createElement('canvas');
    var context = canvas.getContext('2d');
    context.font = "normal " + fontsize + "px " + fontface;
    var metrics = context.measureText( message );
    var textWidth = metrics.width;
    canvas.width = textWidth + borderThickness;
    canvas.height = fontsize * 1.3;
    canvas.style.width = canvas.width + 'px';
    canvas.style.height = canvas.height + 'px';
    
    context.fillStyle   = "rgba(" + backgroundColor.r + "," + backgroundColor.g + "," + backgroundColor.b + "," + backgroundColor.a + ")";
    context.strokeStyle = "rgba(" + borderColor.r + "," + borderColor.g + "," + borderColor.b + "," + borderColor.a + ")";
    
    context.lineWidth = borderThickness;
    roundRect(context, 0, 0, canvas.width, canvas.height, 3);
    
    context.fillStyle = "rgba("+textColor.r+", "+textColor.g+", "+textColor.b+", 1.0)";
    context.fillText( message, borderThickness+2 , fontsize);

    var texture = new THREE.Texture(canvas);
    texture.needsUpdate = true;
    texture.textureWidth = canvas.width;
    texture.textureHeight = canvas.height;
    return texture;
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

function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    return {
      x: evt.clientX - rect.left,
      y: evt.clientY - rect.top
    };
  }
