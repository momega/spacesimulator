'use strict';

spaceSimulatorApp.factory('modelService', ['Model', function(Model) {
	var factory = {};
	var model;

	factory.load = function(pid, callback) {
		model = Model.get({id:pid, time: Date.now()}, function() {
			callback();  
		});
	}
	
	factory.getCurrentTime = function(pid, callback) {
		var time = Model.time({id:pid, time: Date.now()}, function() {
			callback(time);  
		});
	}
	
	/**
	 * Gets all root objects of the model
	 */
	factory.getRootObjects = function() {
		var rootObjects = jmespath.search(model, 'movingObjects');
		return rootObjects;
	}
	
	/**
	 * Returns all celestial bodies of the model
	 */
	factory.getCelestialBodies = function() {
		var result = jmespath.search(model, 'movingObjects[?textureFileName!=null]');
		return result;
	}
	
	factory.getCelestialBodyByName = function(name) {
		var obj = factory.findByName(name);
		return obj;
	}
	
	factory.getPositionProviders = function(timestamp) {
		var result = [];
		var list = factory.getRootObjects();
	    for(var i=0; i<list.length; i++) {
	    	var obj = list[i];
	    	result.push(obj);
	    	if (factory.isSpacecraft(obj)) {
	    		var spacecraft = obj;
	    		if (spacecraft.trajectory.apoapsis!=null) {
	    			result.push(spacecraft.trajectory.apoapsis);
	    		}
	    		if (spacecraft.trajectory.periapsis!=null) {
	    			result.push(spacecraft.trajectory.periapsis);
	    		}
	    		var maneuver = factory.findActiveOrNextManeuver(spacecraft, timestamp);
    			if (maneuver != null) {
    				result.push(maneuver.start);
    				result.push(maneuver.end);
    			}
    			var target = spacecraft.target;
    			if (target != null) {
    				for(var j=0; j<target.orbitIntersections.length; j++) {
    					var intersection = target.orbitIntersections[j];
    					result.push(intersection);
    				}
    			}
    			var exitSoi = spacecraft.exitSoiOrbitalPoint;
    			if (exitSoi != null) {
    				result.push(exitSoi);
    			}
	    	}
	    }
	    return result;
	}
	
    factory.isSpacecraft = function(obj) {
    	var result = (obj.subsystems != null);
    	return result;
    }

    factory.isCelestialBody = function(obj) {
    	var result = (obj.radius != null);
    	return result;
    }
    
    factory.hasTrajectory = function(obj) {
    	var result = (obj.keplerianElements != null);
    	return result;
    }	
	
	factory.findByName = function(name) {
		var objs = jmespath.search(model, "movingObjects[?name==`" + name +"`]");
		return objs[0];
	}
	
    factory.findActiveOrNextManeuver = function(spacecraft, timestamp) {
        var min = null;
        var result = null;
        for(var i=0; i<spacecraft.maneuvers.length; i++) {
        	var maneuver = spacecraft.maneuvers[i];
            var timeDiff = maneuver.end.timestamp.value - timestamp;
            if (timeDiff > 0) {
                if (min == null || (timeDiff < min)) {
                    result = maneuver;
                    min = timeDiff;
                }
            }
        }
        return result;
    }	
    
    factory.isManeuverActiveOrNext = function(spacecraft, theManeuver, timestamp) {
        var m = factory.findActiveOrNextManeuver(spacecraft, timestamp);
        return (m === theManeuver);
    }	
    
    factory.getPosition = function(obj) {
    	var position = obj.hasOwnProperty("position") ? obj["position"] : obj.cartesianState.position;
    	return position;
    }    
	
	factory.getTime = function() {
		return model.time.value;
	}
	
	factory.getName = function() {
		return model.name;
	}
	
	factory.getCamera = function() {
		return model.camera;
	}
	
	return factory;
}]);