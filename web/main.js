
    var mvMatrix = mat4.create();
    var pMatrix = mat4.create();

    function setMatrixUniforms() {
        gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);
        gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);
    }

    var triangleVertexPositionBuffer;
    var squareVertexPositionBuffer;

    function initBuffers() {
        triangleVertexPositionBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, triangleVertexPositionBuffer);
        var vertices = [
             0.0,  1.0,  0.0,
            -1.0, -1.0,  0.0,
             1.0, -1.0,  0.0
        ];
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
        triangleVertexPositionBuffer.itemSize = 3;
        triangleVertexPositionBuffer.numItems = 3;

        squareVertexPositionBuffer = gl.createBuffer();
        gl.bindBuffer(gl.ARRAY_BUFFER, squareVertexPositionBuffer);
        vertices = [
             1.0,  1.0,  0.0,
            -1.0,  1.0,  0.0,
             1.0, -1.0,  0.0,
            -1.0, -1.0,  0.0
        ];
        gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
        squareVertexPositionBuffer.itemSize = 3;
        squareVertexPositionBuffer.numItems = 4;
    }

    function drawScene () {
        gl.viewport (0, 0, gl.viewportWidth, gl.viewportHeight);
        gl.clear (gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

        mat4.perspective (pMatrix, 45.0, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0);

        mat4.identity (mvMatrix);

        //draw the triangle
        var translation = vec3.fromValues(-1.5, 0.0, -7.0);
        mat4.translate (mvMatrix, mvMatrix, translation);
        gl.bindBuffer (gl.ARRAY_BUFFER, triangleVertexPositionBuffer);
        gl.vertexAttribPointer (shaderProgram.vertexPositionAttribute, triangleVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
        setMatrixUniforms ();
        gl.drawArrays (gl.TRIANGLES, 0, triangleVertexPositionBuffer.numItems);

        // draw the square
        translation = vec3.fromValues(3.0, 0.0, 0.0);
        mat4.translate (mvMatrix, mvMatrix, translation);
        gl.bindBuffer (gl.ARRAY_BUFFER, squareVertexPositionBuffer);
        gl.vertexAttribPointer (shaderProgram.vertexPositionAttribute, squareVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
        setMatrixUniforms ();
        gl.drawArrays (gl.TRIANGLE_STRIP, 0, squareVertexPositionBuffer.numItems);
    }
    
//    function drawScene() {
//        gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
//        gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
//
//        mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, pMatrix);
//
//        mat4.identity(mvMatrix);
//
//        mat4.translate(mvMatrix, [-1.5, 0.0, -7.0]);
//        gl.bindBuffer(gl.ARRAY_BUFFER, triangleVertexPositionBuffer);
//        gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, triangleVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
//        setMatrixUniforms();
//        gl.drawArrays(gl.TRIANGLES, 0, triangleVertexPositionBuffer.numItems);
//
//
//        mat4.translate(mvMatrix, [3.0, 0.0, 0.0]);
//        gl.bindBuffer(gl.ARRAY_BUFFER, squareVertexPositionBuffer);
//        gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, squareVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
//        setMatrixUniforms();
//        gl.drawArrays(gl.TRIANGLE_STRIP, 0, squareVertexPositionBuffer.numItems);
//    }
 

    function webGLStart() {
        var canvas = document.getElementById("main-canvas");
        initGL(canvas);
        initShaders();
        initBuffers();

        gl.clearColor(0.0, 0.0, 0.0, 1.0);
        gl.enable(gl.DEPTH_TEST);

        drawScene();
    }
