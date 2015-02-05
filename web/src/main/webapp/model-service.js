'use strict';

spaceSimulatorApp.factory('modelService', ['$http', function($http) {
	var factory = {};
	var model;
	var db;
	var time;
	
	factory.load = function(project, snapshot, callback) {
		 $http.get('data/' + project + '.json').success(function(data) {
			 model = data;
			 db = SpahQL.db(model);
			 time = model.time.value;
			 console.log('time = ' + time);
			 callback(model);
		 });
	}
	
	factory.getRootObjects = function() {
		var rootObjects = db.select("//movingObjects/*").values();
		return rootObjects;
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
		return time;
	}
	
	return factory;
}]);