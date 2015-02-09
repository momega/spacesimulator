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