'use strict';

function solveHA(eccentricity, theta) {
	var sinH = (Math.sin(theta) * Math.sqrt(eccentricity*eccentricity -1)) / (1 + eccentricity * Math.cos(theta));
    var HA = Math.asinh(sinH);
    return HA;
}