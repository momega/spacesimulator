'use strict';

spaceSimulatorApp.factory('modelService', ['Model', function(Model) {
	var factory = {};
	var model;
	var db;

	factory.load = function(pid, callback) {
		model = Model.get({id:pid, time: Date.now()}, function() {
	      	db = SpahQL.db(model);
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
		var objs = db.select("/movingObjects/*");
		var rootObjects = objs.values();
		return rootObjects;
	}
	
	/**
	 * Returns all celestial bodies of the model
	 */
	factory.getCelestialBodies = function() {
		var objs = factory.getRootObjects();
		var result = [];
		for(var i=0; i<objs.length; i++) {
	    	var obj = objs[i];
	    	if (obj.textureFileName != null) {
	    		result.push(obj);
	    	}
	    }
		return result;
	}
	
	factory.getCelestialBodyByName = function(name) {
		var obj = db.select("/movingObjects/*[/name=='" + name + "']").value();
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
		var movingObject = db.select("//*[/name=='" + name + "']").value();
    	return movingObject;
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
    
    factory.getPosition = function(obj) {
    	var position = obj.hasOwnProperty("position") ? obj["position"] : obj.cartesianState.position;
    	return position;
    }    
	
	factory.getTime = function() {
		return model.time.value;
	}
	
	factory.getCamera = function() {
		return model.camera;
	}
	
	return factory;
}]);