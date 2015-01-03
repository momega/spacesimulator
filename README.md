Space Simulator
===============

The Space Simulator is OPENGL application written in Java. It simulates the motion (and rotation) of the planets
and spacecraft(s).

The planets are moving based on Keplerian law (simplified elliptical) trajectory.

The position and velocity of spacecraft(s) are computed based on Newtonian gravitational law. The gravitation of all the planets
influences the trajectory of the spacecrafts.

How to run application?
=======================
1. Application used maven to build, so download and install it.
2. Type the following command to compile the application, run few tests and execute the main application.  
```
	mvn clean test exec:java
```

If you do not want to execute the tests, run the following command:
```
	mvn clean compile exec:java
```

Features
========

* Orbital maneuvers with fuel consumption
* Display closes point and planes intersections of the spacecraft and target celestial body
* Time warp
* Display popup dialogs with detail information about all objects
* All 8 planets and their moon + two minor bodies included in model
* User defined points for on the spacecraft and planets trajectory
* Take a screenshot
* Headless computations
* Sphere of Influence
* Load and save models
* Run the simulation from prepared models.
* Interplanetary flight planner

Screenshots
===========

![Spacecraft orbiting earth](/images/earth.png "Spacecraft orbiting earth")

![High eccentricity orbit](/images/spacecraft.png "High eccentricity orbit")

![Near the Moon](/images/moon.png "Near the Moon")

![Leaving Earth Orbit](/images/leavingearth.png "Leaving Earth Orbit")

![Arriving to Mars](/images/mars1.png "Arriving to Mars")

![Arriving to Mars](/images/mars1.png "Arriving to Mars")

![Maneuver at Periapsis](/images/mars2.png "Maneuver at Periapsis")

![Orbit around Mars](/images/mars3.png "Orbit around Mars")

![Maneuver at Periapsis](/images/venus.png "Orbiting Venus")

![Orbit around Mars](/images/arrivetovenus.png "Approaching Venus")

![Hi-resolution Earth](/images/earth_hi.png "Hi-resolution Earth")

![All type of the points](/images/allpoints.png "All types of the points")

![Titan and Saturn](/images/titan.png "Titan and Saturn")

Build
=====

[![wercker status](https://app.wercker.com/status/262d561454952437fddc94f925ffc667/m/master "wercker status")](https://app.wercker.com/project/bykey/262d561454952437fddc94f925ffc667)




