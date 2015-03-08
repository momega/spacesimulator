'use strict';

function solveHA(eccentricity, theta) {
	var sinH = (Math.sin(theta) * Math.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
    var HA = asinh(sinH);
    return HA;
}

function asinh(x) {
	if (x === -Infinity) {
		return x;
	} else {
		return Math.log(x + Math.sqrt(x * x + 1));
	}
};

function vectorLength(vector) {
	var l = Math.sqrt(vector.x*vector.x + vector.y*vector.y + vector.z*vector.z);
	return l;
}

function toSphericalCoordinates(vector) {
	var r = vectorLength(vector);
    var theta = Math.acos(vector.z / r);
    var phi = Math.atan2(vector.y, vector.x);
    return {r : r, theta: theta, phi : phi};
}